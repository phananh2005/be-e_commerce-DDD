package com.phananh.e_commerce.product.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PRODUCT_EXCHANGE = "product.exchange";
    public static final String PRODUCT_SYNC_QUEUE = "product.es.sync.queue";
    public static final String PRODUCT_SYNC_ROUTING_KEY = "product.es.sync.routing.key";

    @Bean
    public DirectExchange productExchange() {
        return new DirectExchange(PRODUCT_EXCHANGE);
    }

    @Bean
    public Queue productSyncQueue() {
        return new Queue(PRODUCT_SYNC_QUEUE, true); // durable
    }

    @Bean
    public Binding bindingProductSyncQueue(Queue productSyncQueue, DirectExchange productExchange) {
        return BindingBuilder.bind(productSyncQueue).to(productExchange).with(PRODUCT_SYNC_ROUTING_KEY);
    }
}
