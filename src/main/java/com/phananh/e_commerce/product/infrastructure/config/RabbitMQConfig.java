package com.phananh.e_commerce.product.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PRODUCT_EXCHANGE = "product.exchange";
    public static final String PRODUCT_SYNC_QUEUE = "product.es.sync.queue";
    public static final String PRODUCT_SYNC_ROUTING_KEY = "product.es.sync.routing.key";

    public static final String PRODUCT_DLX = "product.dlx";
    public static final String PRODUCT_DLQ = "product.dlq";
    public static final String PRODUCT_DLX_ROUTING_KEY = "product.dlx.routing.key";

    @Bean
    public DirectExchange productExchange() {
        return new DirectExchange(PRODUCT_EXCHANGE);
    }

    @Bean
    public DirectExchange productDlx() {
        return new DirectExchange(PRODUCT_DLX);
    }

    @Bean
    public Queue productSyncQueue() {
        return QueueBuilder.durable(PRODUCT_SYNC_QUEUE)
                .withArgument("x-dead-letter-exchange", PRODUCT_DLX)
                .withArgument("x-dead-letter-routing-key", PRODUCT_DLX_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue productDlq() {
        return new Queue(PRODUCT_DLQ, true);
    }

    @Bean
    public Binding bindingProductSyncQueue(Queue productSyncQueue, DirectExchange productExchange) {
        return BindingBuilder.bind(productSyncQueue).to(productExchange).with(PRODUCT_SYNC_ROUTING_KEY);
    }

    @Bean
    public Binding bindingProductDlq(Queue productDlq, DirectExchange productDlx) {
        return BindingBuilder.bind(productDlq).to(productDlx).with(PRODUCT_DLX_ROUTING_KEY);
    }
}
