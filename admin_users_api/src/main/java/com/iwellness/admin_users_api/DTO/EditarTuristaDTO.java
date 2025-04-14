package com.iwellness.admin_users_api.DTO;

import lombok.Data;

@Data
public class EditarTuristaDTO {
    private String nombre;
    private int telefono;
    private String ciudad;
    private String pais;
}
