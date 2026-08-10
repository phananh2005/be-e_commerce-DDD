package com.phananh.e_commerce.product.infrastructure.cdc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.phananh.e_commerce.product.infrastructure.messaging.ProductEventPublisher;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
@RequiredArgsConstructor
public class MysqlBinlogListener {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    private final ProductEventPublisher productEventPublisher;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JdbcTemplate jdbcTemplate;

    private final Map<Long, String> tableMap = new HashMap<>();
    private List<String> productColumns = new CopyOnWriteArrayList<>();
    private final List<String> transactionBuffer = new ArrayList<>();

    private void loadProductColumns() {
        log.info("CDC: Đang nạp cấu trúc cột của bảng products...");
        List<String> columns = jdbcTemplate.queryForList(
                "SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE TABLE_NAME = 'products' AND TABLE_SCHEMA = DATABASE() ORDER BY ORDINAL_POSITION",
                String.class
        );
        this.productColumns = new java.util.ArrayList<>(columns);
        log.info("CDC: Cấu trúc cột hiện tại: {}", productColumns);
    }

    private String convertUuidBytesToString(Object uuidObj) {
        if (uuidObj instanceof byte[] bytes) {
            if (bytes.length == 16) {
                java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(bytes);
                return new java.util.UUID(bb.getLong(), bb.getLong()).toString();
            }
        }
        return null;
    }

    private Map<String, Object> mapRowToJson(Object[] rowData) {
        Map<String, Object> jsonMap = new HashMap<>();
        for (int i = 0; i < rowData.length && i < productColumns.size(); i++) {
            String colName = productColumns.get(i);
            Object value = rowData[i];

            if ("uuid".equalsIgnoreCase(colName)) {
                jsonMap.put(colName, convertUuidBytesToString(value));
            } else if (value instanceof byte[] bytes) {
                // Convert TEXT/BLOB byte[] to String to prevent Jackson base64 encoding
                jsonMap.put(colName, new String(bytes, java.nio.charset.StandardCharsets.UTF_8));
            } else {
                jsonMap.put(colName, value);
            }
        }
        return jsonMap;
    }

    @PostConstruct
    public void startListening() {
        // Load schema lần đầu
        loadProductColumns();

        new Thread(() -> {
            try {
                // Parse url: jdbc:mysql://localhost:3306/e_commerce?serverTimezone...
                String cleanUrl = dbUrl.replace("jdbc:mysql://", "").split("\\?")[0];
                String[] hostPortDb = cleanUrl.split("/");
                String[] hostPort = hostPortDb[0].split(":");
                String host = hostPort[0];
                int port = hostPort.length > 1 ? Integer.parseInt(hostPort[1]) : 3306;

                BinaryLogClient client = new BinaryLogClient(host, port, dbUser, dbPassword);
                client.setServerId(1001); // Unique server id

                // Khôi phục Offset từ Database
                try {
                    Map<String, Object> offset = jdbcTemplate.queryForMap("SELECT binlog_filename, binlog_position FROM binlog_tracking WHERE id = 1");
                    String savedFilename = (String) offset.get("binlog_filename");
                    long savedPosition = ((Number) offset.get("binlog_position")).longValue();
                    
                    if (savedFilename != null && !savedFilename.isEmpty() && savedPosition > 0) {
                        client.setBinlogFilename(savedFilename);
                        client.setBinlogPosition(savedPosition);
                        log.info("CDC: Đã phục hồi Offset -> File: {}, Position: {}", savedFilename, savedPosition);
                    }
                } catch (Exception e) {
                    log.warn("CDC: Không tìm thấy offset cũ, bắt đầu đọc từ hiện tại.");
                }

                client.registerEventListener(event -> {
                    EventData data = event.getData();

                    // Bắt sự kiện XID (Commit Transaction) để đẩy message và lưu Offset
                    if (data instanceof XidEventData) {
                        if (!transactionBuffer.isEmpty()) {
                            log.info("CDC: Phát hiện COMMIT (XID). Đẩy {} event lên RabbitMQ", transactionBuffer.size());
                            for (String json : transactionBuffer) {
                                productEventPublisher.publishProductSavedEvent(json);
                            }
                            transactionBuffer.clear();
                        }

                        String currentFilename = client.getBinlogFilename();
                        long currentPosition = client.getBinlogPosition();
                        jdbcTemplate.update("UPDATE binlog_tracking SET binlog_filename = ?, binlog_position = ? WHERE id = 1", currentFilename, currentPosition);
                    }
                    // Bắt sự kiện Query (BEGIN, DDL)
                    else if (data instanceof QueryEventData queryData) {
                        String sql = queryData.getSql().toUpperCase();
                        if ("BEGIN".equals(sql)) {
                            transactionBuffer.clear();
                        } else if (sql.contains("ALTER TABLE PRODUCTS") || sql.contains("ALTER TABLE `PRODUCTS`")) {
                            log.info("CDC: Phát hiện ALTER TABLE products. Nạp lại cấu trúc cột...");
                            loadProductColumns();
                        }
                    } 
                    // Map TableId với TableName
                    else if (data instanceof TableMapEventData tableData) {
                        tableMap.put(tableData.getTableId(), tableData.getTable());
                    } 
                    // Bắt sự kiện INSERT
                    else if (data instanceof WriteRowsEventData writeData) {
                        String tableName = tableMap.get(writeData.getTableId());

                        if ("products".equals(tableName)) {
                            for (Object[] row : writeData.getRows()) {
                                Map<String, Object> jsonMap = mapRowToJson(row);
                                log.info("CDC: Phát hiện INSERT ở bảng {}, ID: {}", tableName, jsonMap.get("id"));
                                
                                try {
                                    String json = objectMapper.writeValueAsString(jsonMap);
                                    transactionBuffer.add(json);
                                } catch (Exception ex) {
                                    log.error("Lỗi parse JSON", ex);
                                }
                            }
                        }
                    } 
                    // Bắt sự kiện UPDATE
                    else if (data instanceof UpdateRowsEventData updateData) {
                        String tableName = tableMap.get(updateData.getTableId());

                        if ("products".equals(tableName)) {
                            for (Map.Entry<Serializable[], Serializable[]> row : updateData.getRows()) {
                                // Serializable[] oldRow = row.getKey();
                                Serializable[] newRow = row.getValue();

                                Map<String, Object> jsonMap = mapRowToJson(newRow);
                                log.info("CDC: Phát hiện UPDATE ở bảng {}, ID: {}", tableName, jsonMap.get("id"));
                                
                                try {
                                    String json = objectMapper.writeValueAsString(jsonMap);
                                    transactionBuffer.add(json);
                                } catch (Exception ex) {
                                    log.error("Lỗi parse JSON", ex);
                                }
                            }
                        }
                    }
                });

                log.info("CDC: Đang kết nối tới MySQL Binlog tại {}:{} ...", host, port);
                client.connect();
            } catch (IOException e) {
                log.error("CDC: Lỗi kết nối Binlog", e);
            } catch (Exception e) {
                log.error("CDC: Lỗi không xác định", e);
            }
        }).start();
    }
}
