package com.phananh.e_commerce.product.infrastructure.messaging;

import com.phananh.e_commerce.product.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.phananh.e_commerce.product.domain.document.ProductDocument;
import com.rabbitmq.client.Channel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventConsumer {

    private final ElasticsearchOperations elasticsearchOperations;
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Transactional(readOnly = true)
    @RabbitListener(queues = RabbitMQConfig.PRODUCT_SYNC_QUEUE)
    public void handleProductSavedEvent(String jsonPayload, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("Received CDC event: {}", jsonPayload);
        try {
            ProductDocument doc = objectMapper.readValue(jsonPayload, ProductDocument.class);
            if (doc.getId() != null) {

                // Bỏ qua nếu message cũ hơn version đang có trên ES
                if(doc.getVersion() != null){
                    ProductDocument existingDoc = elasticsearchOperations.get(doc.getId(), ProductDocument.class);
                    if (existingDoc != null && existingDoc.getVersion() != null) {
                        if (existingDoc.getVersion() >= doc.getVersion()) {
                            log.info("Ignored outdated message for product {}. ES version: {}, Msg version: {}",
                                    doc.getId(), existingDoc.getVersion(), doc.getVersion());
                            channel.basicAck(tag, false);
                            return;
                        }
                    }
                }

                // Lấy thêm minPrice và maxPrice từ DB bằng câu query SQL thuần (chạy siêu nhanh, né Hibernate)
                if (doc.getId() != null) {
                    Long productId = Long.parseLong(doc.getId());
                    String sql = "SELECT MIN(price) AS min_price, MAX(price) AS max_price FROM product_variants WHERE product_id = ?";
                    
                    try {
                        Map<String, Object> result = jdbcTemplate.queryForMap(sql, productId);
                        BigDecimal min = (BigDecimal) result.get("min_price");
                        BigDecimal max = (BigDecimal) result.get("max_price");
                        
                        doc.setMinPrice(min != null ? min.doubleValue() : 0.0);
                        doc.setMaxPrice(max != null ? max.doubleValue() : 0.0);
                    } catch (org.springframework.dao.EmptyResultDataAccessException e) {
                        doc.setMinPrice(0.0);
                        doc.setMaxPrice(0.0);
                    }
                }
            }

            elasticsearchOperations.save(doc);
            log.info("Successfully synced product {} to Elasticsearch", doc.getId());
            
            // Xử lý thành công -> Gửi ACK
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("Error syncing product to Elasticsearch. Để Spring tự đếm Retry...", e);
            // Quăng Exception ra ngoài để Spring AMQP Retry đếm đủ 5 lần. 
            // Vượt 5 lần nó sẽ tự động NACK (requeue=false) đá vào DLQ.
            throw e;
        }
    }
}
