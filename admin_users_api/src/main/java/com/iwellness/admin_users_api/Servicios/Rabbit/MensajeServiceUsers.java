package com.iwellness.admin_users_api.Servicios.Rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.stereotype.Service;

@Service
public class MensajeServiceUsers {
    private final RabbitTemplate rabbitTemplate;

    public MensajeServiceUsers(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;

        try {
            System.out.println("=== VERIFICANDO CONEXIÓN A RABBITMQ ===");
            rabbitTemplate.execute(channel -> {
                System.out.println("Conexión a RabbitMQ establecida correctamente");
                return null;
            });
        } catch (Exception e) {
            System.err.println("Error al conectar con RabbitMQ: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void enviarMensajeProveedor(String mensaje) {
        try{
            System.out.println("=== INICIO ENVÍO A RABBITMQ ===");
            System.out.println("Exchange: " + MensajeServiceUsersConfig.EXCHANGE_NAME);
            System.out.println("Routing Key: " + MensajeServiceUsersConfig.ROUTING_KEY_PROVEEDOR);
            System.out.println("Mensaje a enviar: " + mensaje);

            // Enviar mensaje
            rabbitTemplate.convertAndSend(
                MensajeServiceUsersConfig.EXCHANGE_NAME,
                MensajeServiceUsersConfig.ROUTING_KEY_PROVEEDOR,
                mensaje
            );

            // Log después de enviar
            System.out.println("=== MENSAJE ENVIADO EXITOSAMENTE ===");
        } catch (Exception e) {
            System.err.println("Error al enviar el mensaje: " + e.getMessage());
        }
    }

    public void enviarMensajeTurista(String mensaje){
        try{
            System.out.println("=== INICIO ENVÍO A RABBITMQ ===");
            System.out.println("Exchange: " + MensajeServiceUsersConfig.EXCHANGE_NAME);
            System.out.println("Routing Key: " + MensajeServiceUsersConfig.ROUTING_KEY_TURISTA);
            System.out.println("Mensaje a enviar: " + mensaje);

            // Enviar mensaje
            rabbitTemplate.convertAndSend(
                MensajeServiceUsersConfig.EXCHANGE_NAME,
                MensajeServiceUsersConfig.ROUTING_KEY_TURISTA,
                mensaje
            );

            // Log después de enviar
            System.out.println("=== MENSAJE ENVIADO EXITOSAMENTE ===");
        } catch (Exception e) {
            System.err.println("Error al enviar el mensaje: " + e.getMessage());
        }

    }

}
