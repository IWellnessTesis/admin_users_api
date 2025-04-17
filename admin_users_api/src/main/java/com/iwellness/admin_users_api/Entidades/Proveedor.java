package com.iwellness.admin_users_api.Entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @Column(name = "id_usuarios_proveedor")
    Long id;

    @OneToOne
    @JoinColumn(name = "usuarios_id", referencedColumnName = "id_usuarios", nullable = false)
    @JsonBackReference
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
