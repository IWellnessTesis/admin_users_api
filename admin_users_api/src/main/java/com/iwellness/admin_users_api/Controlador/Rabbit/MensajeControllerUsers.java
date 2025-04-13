package com.iwellness.admin_users_api.Controlador.Rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwellness.admin_users_api.DTO.UsuariosDTO;
import com.iwellness.admin_users_api.Entidades.Proveedor;
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
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProveedor = objectMapper.writeValueAsString(proveedor);
            this.template.convertAndSend(MensajeServiceUsersConfig.EXCHANGE_NAME, MensajeServiceUsersConfig.ROUTING_KEY_USERS, jsonProveedor);
            return "Proveedores publicados: " + proveedor.getNombre_empresa() + " con id: " + proveedor.getId();
        } catch (JsonProcessingException e) {
            return "Error al serializar la preferencia: " + e.getMessage();
        }
    }

    @PostMapping("/mensaje/enviar/usuarios")
    public String enviarMensajeUsuarios(@RequestBody Usuarios usuarios) {
         try {
        UsuariosDTO dto = new UsuariosDTO(

            usuarios.getCorreo(),
            usuarios.getContraseña(),
            usuarios.getId(),
            usuarios.getNombre(),
            usuarios.getRol().getNombre() // Solo el nombre del rol, no el objeto completo

        );

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUsuarios = objectMapper.writeValueAsString(dto);
        this.template.convertAndSend(
            MensajeServiceUsersConfig.EXCHANGE_NAME,
            MensajeServiceUsersConfig.ROUTING_KEY_USERS,
            jsonUsuarios
        );
        return "Usuarios publicados: " + dto.getNombre() + " con id: " + dto.getId();
    } catch (JsonProcessingException e) {
        return "Error al serializar el usuario: " + e.getMessage();
    }
    }



}
