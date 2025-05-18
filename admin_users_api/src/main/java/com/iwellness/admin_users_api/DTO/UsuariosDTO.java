package com.iwellness.admin_users_api.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuariosDTO {
    private String correo;
    private String contraseña;
    private Long id;
    private String nombre;
    private String rol;  // Solo el nombre del rol, no el objeto completo
}
