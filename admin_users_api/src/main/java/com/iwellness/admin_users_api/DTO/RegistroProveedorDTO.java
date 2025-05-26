package com.iwellness.admin_users_api.DTO;

import lombok.Data;

@Data
public class RegistroProveedorDTO {
    private String nombre;
    private String correo;
    private String contraseña;
    private String foto;
    private String nombre_empresa;
    private String coordenadaX;
    private String coordenadaY;
    private String cargoContacto;
    private String telefono;
    private String telefonoEmpresa;
}
