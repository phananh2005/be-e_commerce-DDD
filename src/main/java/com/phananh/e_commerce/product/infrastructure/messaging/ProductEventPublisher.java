package com.phananh.e_commerce.product.infrastructure.messaging;

import com.phananh.e_commerce.product.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishProductSavedEvent(Object productData) {
        log.info("Publishing product event: {}", productData);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCT_EXCHANGE,
                RabbitMQConfig.PRODUCT_SYNC_ROUTING_KEY,
                productData 
        );
    }
}
