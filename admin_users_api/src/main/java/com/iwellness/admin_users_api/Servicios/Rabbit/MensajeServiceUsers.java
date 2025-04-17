package com.iwellness.admin_users_api.Servicios.Rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.stereotype.Service;

@Service
public class MensajeServiceUsers {
    private final RabbitTemplate rabbitTemplate;

    public MensajeServiceUsers(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarMensajeUsuarios(String mensaje) {
        try{
            this.rabbitTemplate.convertAndSend(MensajeServiceUsersConfig.EXCHANGE_NAME, MensajeServiceUsersConfig.ROUTING_KEY_USERS, mensaje);
            System.out.println("Mensaje enviado: " + mensaje);
        } catch (Exception e) {
            System.err.println("Error al enviar el mensaje: " + e.getMessage());
        }
    }

    public void enviarMensajeProveedor(String mensaje) {
        try{
            this.rabbitTemplate.convertAndSend(MensajeServiceUsersConfig.EXCHANGE_NAME, MensajeServiceUsersConfig.ROUTING_KEY_PROVEEDOR, mensaje);
            System.out.println("Mensaje enviado: " + mensaje);
        } catch (Exception e) {
            System.err.println("Error al enviar el mensaje: " + e.getMessage());
        }
    }

    public void enviarMensajeTurista(String mensaje) {
        try{
            this.rabbitTemplate.convertAndSend(MensajeServiceUsersConfig.EXCHANGE_NAME, MensajeServiceUsersConfig.ROUTING_KEY_TURISTA, mensaje);
            System.out.println("Mensaje enviado: " + mensaje);
        } catch (Exception e) {
            System.err.println("Error al enviar el mensaje: " + e.getMessage());
        }
    }
}
