package com.iwellness.admin_users_api.DTO;

import java.sql.Date;

import lombok.Data;


@Data
public class TuristaDTO {
    private Long idTurista;
      private String nombre;
    private String telefono;
    private String ciudad;
    private String pais;
    private String genero;
    private String estadoCivil;
}
