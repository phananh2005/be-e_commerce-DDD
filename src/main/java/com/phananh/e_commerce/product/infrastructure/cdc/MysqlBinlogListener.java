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
    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;

    private final Map<Long, String> tableMap = new HashMap<>();
    private List<String> productColumns = new CopyOnWriteArrayList<>();

    private void loadProductColumns() {
        log.info("CDC: Đang nạp cấu trúc cột của bảng products...");
        this.productColumns = jdbcTemplate.queryForList(
                "SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE TABLE_NAME = 'products' AND TABLE_SCHEMA = DATABASE() ORDER BY ORDINAL_POSITION",
                String.class
        );
        log.info("CDC: Cấu trúc cột hiện tại: {}", productColumns);
    }

    private String convertUuidBytesToString(Object uuidObj) {
        if (uuidObj instanceof byte[]) {
            byte[] bytes = (byte[]) uuidObj;
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

                client.registerEventListener(event -> {
                    EventData data = event.getData();

                    // Bắt sự kiện DDL để nạp lại cột (ALTER TABLE)
                    if (data instanceof QueryEventData) {
                        QueryEventData queryData = (QueryEventData) data;
                        String sql = queryData.getSql().toUpperCase();
                        if (sql.contains("ALTER TABLE PRODUCTS") || sql.contains("ALTER TABLE `PRODUCTS`")) {
                            log.info("CDC: Phát hiện ALTER TABLE products. Nạp lại cấu trúc cột...");
                            loadProductColumns();
                        }
                    } 
                    // Map TableId với TableName
                    else if (data instanceof TableMapEventData) {
                        TableMapEventData tableData = (TableMapEventData) data;
                        tableMap.put(tableData.getTableId(), tableData.getTable());
                    } 
                    // Bắt sự kiện INSERT
                    else if (data instanceof WriteRowsEventData) {
                        WriteRowsEventData writeData = (WriteRowsEventData) data;
                        String tableName = tableMap.get(writeData.getTableId());

                        if ("products".equals(tableName)) {
                            for (Object[] row : writeData.getRows()) {
                                Map<String, Object> jsonMap = mapRowToJson(row);
                                log.info("CDC: Phát hiện INSERT ở bảng products, ID: {}", jsonMap.get("id"));
                                
                                try {
                                    String json = objectMapper.writeValueAsString(jsonMap);
                                    productEventPublisher.publishProductSavedEvent(json);
                                } catch (Exception ex) {
                                    log.error("Lỗi parse JSON", ex);
                                }
                            }
                        }
                    } 
                    // Bắt sự kiện UPDATE
                    else if (data instanceof UpdateRowsEventData) {
                        UpdateRowsEventData updateData = (UpdateRowsEventData) data;
                        String tableName = tableMap.get(updateData.getTableId());

                        if ("products".equals(tableName)) {
                            for (Map.Entry<Serializable[], Serializable[]> row : updateData.getRows()) {
                                // Serializable[] oldRow = row.getKey();
                                Serializable[] newRow = row.getValue();

                                Map<String, Object> jsonMap = mapRowToJson(newRow);
                                log.info("CDC: Phát hiện UPDATE ở bảng products, ID: {}", jsonMap.get("id"));
                                
                                try {
                                    String json = objectMapper.writeValueAsString(jsonMap);
                                    productEventPublisher.publishProductSavedEvent(json);
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
