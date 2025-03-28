package com.iwellness.admin_users_api.Servicios.Rabbit;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class MensajeServiceUsersConfig {
    private static final String EXCHANGE_NAME = "my_exchange";
    private static final String QUEUE_NAME = "my_queue";
    private static final String ROUTING_KEY = "my_routing_key";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}
