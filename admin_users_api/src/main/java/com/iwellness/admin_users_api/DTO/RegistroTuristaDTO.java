package com.iwellness.admin_users_api.DTO;

import java.util.Date;

import lombok.Data;

@Data
public class RegistroTuristaDTO {
    private String nombre;
    private String correo;
    private String contraseña;
    private String foto;
    private String telefono;
    private String ciudad;
    private String pais;
    private String genero;
    private Date fechaNacimiento;
}
