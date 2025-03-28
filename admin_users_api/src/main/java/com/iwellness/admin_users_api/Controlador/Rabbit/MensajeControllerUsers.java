package com.iwellness.admin_users_api.Controlador.Rabbit;

import org.springframework.web.bind.annotation.*;

import com.iwellness.admin_users_api.Servicios.Rabbit.MensajeServiceUsers;

@RestController
@RequestMapping("/mensajes")
public class MensajeControllerUsers {
    private final MensajeServiceUsers mensajeServiceUsers;

    public MensajeControllerUsers(MensajeServiceUsers mensajeServiceUsers) {
        this.mensajeServiceUsers = mensajeServiceUsers;
    }

    @PostMapping("/enviar")
    public String enviarMensaje(@RequestBody String mensaje) {
        mensajeServiceUsers.enviarMensaje(mensaje);
        return "Mensaje enviado: " + mensaje;
    }
}
