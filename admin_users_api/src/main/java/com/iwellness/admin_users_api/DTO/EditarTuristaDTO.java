package com.iwellness.admin_users_api.DTO;

import java.util.Date;

import lombok.Data;

@Data
public class EditarTuristaDTO {
    private String nombre;
    private String telefono;
    private String ciudad;
    private String pais;
    private String foto;
    private String genero;
    private Date fechaNacimiento;
    private String estadoCivil;
}
