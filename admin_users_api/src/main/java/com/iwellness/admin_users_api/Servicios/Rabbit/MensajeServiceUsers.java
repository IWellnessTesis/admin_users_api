package com.iwellness.admin_users_api.Servicios.Rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MensajeServiceUsers {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MensajeServiceUsers(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarMensaje(String mensaje) {
        String exchange = "my_exchange";       // Nombre del exchange
        String routingKey = "my_routing_key";  // Clave de enrutamiento

        rabbitTemplate.convertAndSend(exchange, routingKey, mensaje);
        System.out.println("Mensaje enviado: " + mensaje);
    }
}
