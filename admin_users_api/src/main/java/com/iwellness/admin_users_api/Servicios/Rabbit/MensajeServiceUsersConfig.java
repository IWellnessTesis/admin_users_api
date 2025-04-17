package com.iwellness.admin_users_api.Servicios.Rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class MensajeServiceUsersConfig {
    public static final String EXCHANGE_NAME = "message_exchange";
    public static final String QUEUE_NAME_USUARIO = "queue_users"; //cola para usuarios
    public static final String ROUTING_KEY_USERS = "my_routing_key_users";
    public static final String QUEUE_NAME_PROVEEDOR = "queue_proveedor"; //cola para proveedores
    public static final String QUEUE_NAME_TURISTA = "queue_turista"; //cola para turistas
    public static final String ROUTING_KEY_PROVEEDOR = "my_routing_key_proveedor";
    public static final String ROUTING_KEY_TURISTA = "my_routing_key_turista";



    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }
    
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connection) {
        final var template = new RabbitTemplate(connection);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public Queue queueUsuario() {
        return new Queue(QUEUE_NAME_USUARIO, true);
    }

    @Bean
    public Queue queueProveedor() {
        return new Queue(QUEUE_NAME_PROVEEDOR, true);
    }

    @Bean
    public Queue queueTurista() {
        return new Queue(QUEUE_NAME_TURISTA, true);
    }


    @Bean
    public Binding bindingUsuario(Queue queueUsuario, TopicExchange exchange) {
        return BindingBuilder.bind(queueUsuario).to(exchange).with(ROUTING_KEY_USERS);
    }

    @Bean
    public Binding bindingProveedor(Queue queueProveedor, TopicExchange exchange) {
        return BindingBuilder.bind(queueProveedor).to(exchange).with(ROUTING_KEY_PROVEEDOR);
    }

    @Bean
    public Binding bindingTurista(Queue queueTurista, TopicExchange exchange) {
        return BindingBuilder.bind(queueTurista).to(exchange).with(ROUTING_KEY_TURISTA);
    }
}
