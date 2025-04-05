package com.iwellness.admin_users_api.Servicios.Rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.stereotype.Service;

@Service
public class MensajeServiceUsers {
    private final RabbitTemplate rabbitTemplate;

    public MensajeServiceUsers(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarMensaje(String mensaje) {
        rabbitTemplate.convertAndSend(MensajeServiceUsersConfig.EXCHANGE_NAME, MensajeServiceUsersConfig.ROUTING_KEY_USERS, mensaje);
        System.out.println("Mensaje enviado: " + mensaje);
    }
}
