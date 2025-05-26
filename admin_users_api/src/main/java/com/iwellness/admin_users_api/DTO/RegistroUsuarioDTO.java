package com.iwellness.admin_users_api.DTO;

import lombok.Data;

@Data
public class RegistroUsuarioDTO {
    private String nombre;
    private String correo;
    private String contraseña;
    private String foto;
}
