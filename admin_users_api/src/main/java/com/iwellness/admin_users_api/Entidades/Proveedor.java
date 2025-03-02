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
    @JsonIgnore
    @JoinColumn(name = "usuarios_id") // FK 
    private Usuarios usuarios;

    @Column(nullable = false)
    private String nombre_empresa;

    @Column(nullable = false)
    private String direccion;    
    
}
