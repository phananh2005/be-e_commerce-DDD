package com.phananh.e_commerce.product.infrastructure.messaging;

import com.phananh.e_commerce.product.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void setupConfirms() {
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                log.error("RabbitMQ NACK! Gửi message thất bại. Lý do: {}", cause);
                // Có thể tích hợp Outbox pattern hoặc đưa vào Dead Letter DB sau
            }
        });

        rabbitTemplate.setReturnsCallback(returned -> {
            log.error("RabbitMQ Return: Sai RoutingKey/Exchange. Nội dung: {}", returned.getMessage());
        });
    }

    public void publishProductSavedEvent(Object productData) {
        log.info("Publishing product event: {}", productData);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCT_EXCHANGE,
                RabbitMQConfig.PRODUCT_SYNC_ROUTING_KEY,
                productData 
        );
    }
}
