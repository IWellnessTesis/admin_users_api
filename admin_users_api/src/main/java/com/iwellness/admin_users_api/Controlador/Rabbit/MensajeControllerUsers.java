package com.iwellness.admin_users_api.Controlador.Rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import com.iwellness.admin_users_api.Entidades.Proveedor;
import com.iwellness.admin_users_api.Entidades.Turista;
import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Servicios.Rabbit.MensajeServiceUsersConfig;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("usuarios/publish")
@RequiredArgsConstructor
public class MensajeControllerUsers {
    private final RabbitTemplate template;

    @PostMapping("/mensaje/enviar/proveedor")
    public String enviarMensajeProveedor(@RequestBody Proveedor proveedor) {
        try {
            this.template.convertAndSend(MensajeServiceUsersConfig.EXCHANGE_NAME, 
            MensajeServiceUsersConfig.ROUTING_KEY_PROVEEDOR, 
            proveedor);
            return "Proveedores publicados: " + proveedor.getNombre_empresa() + " con id: " + proveedor.getId();
        } catch (Exception e) {
            return "Error al serializar la preferencia: " + e.getMessage();
        }
    }

    @PostMapping("/mensaje/enviar/usuarios")
    public String enviarMensajeUsuarios(@RequestBody Usuarios usuarios) {
        try {

        this.template.convertAndSend(
            MensajeServiceUsersConfig.EXCHANGE_NAME,
            MensajeServiceUsersConfig.ROUTING_KEY_USERS,
            usuarios
        );
        return "Usuarios publicados: " + usuarios.getNombre() + " con id: " + usuarios.getId();
    } catch (Exception e) {
        return "Error al serializar el usuario: " + e.getMessage();
    }
    }

    @PostMapping("/mensaje/enviar/turista")
    public String enviarMensajeTurista (@RequestBody Turista turista) {
        try {
            this.template.convertAndSend(MensajeServiceUsersConfig.EXCHANGE_NAME, 
            MensajeServiceUsersConfig.ROUTING_KEY_TURISTA, 
            turista);
            return "Turistas publicados: " + turista.getCiudad() + " con id: " + turista.getId();
        } catch (Exception e) {
            return "Error al serializar el turista: " + e.getMessage();
        }

    }

}
