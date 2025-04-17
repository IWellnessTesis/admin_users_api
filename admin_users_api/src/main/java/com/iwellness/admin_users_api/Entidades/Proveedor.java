package com.iwellness.admin_users_api.Entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Proveedor")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "usuarios_id") // FK 
    private Usuarios usuarios;

    @Column(nullable = false)
    private String nombre_empresa;

    @Column(nullable = false)
    private String coordenadaX;  
    
    @Column(nullable = false)
    private String coordenadaY;   

    @Column
    private String cargoContacto;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String telefonoEmpresa;

}
