package com.iwellness.admin_users_api.DTO;

import lombok.Data;

@Data
public class EditarProveedorDTO {
    private String nombre;
    private String nombre_empresa;
    private String foto;
    private String coordenadaX;
    private String coordenadaY;
    private String cargoContacto;
    private String telefono;
    private String telefonoEmpresa;
}
