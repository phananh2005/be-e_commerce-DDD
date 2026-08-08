package com.phananh.e_commerce.product.infrastructure.messaging;

import com.phananh.e_commerce.product.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventConsumer {

    // private final ElasticsearchRepository (hoặc service đẩy lên ES)

    @RabbitListener(queues = RabbitMQConfig.PRODUCT_SYNC_QUEUE)
    public void handleProductSavedEvent(Long productId) {
        log.info("Received product saved event for productId: {}", productId);
        try {
            // TODO: Query DB lấy thông tin Product
            // TODO: Map sang Elasticsearch Document
            // TODO: Save vào Elasticsearch
            log.info("Successfully synced product {} to Elasticsearch", productId);
        } catch (Exception e) {
            log.error("Error syncing product {} to Elasticsearch", productId, e);
            // TODO: Cấu hình retry/Dead Letter Queue (DLQ) ở đây nếu cần
            throw e; // Ném ra lỗi để RabbitMQ requeue hoặc chuyển vào DLQ
        }
    }
}
