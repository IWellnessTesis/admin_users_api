package com.iwellness.admin_users_api.Entidades;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Turista")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@ToString(exclude = {"usuarios"})
public class Turista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuarios_turista")
    Long id;


    @OneToOne
    @JoinColumn(name = "usuarios_id", referencedColumnName = "id_usuarios", nullable = false)
    @JsonBackReference
    private Usuarios usuarios;


    @Column (nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String pais;

    @Column
    private String genero;

    @Column
    private Date fechaNacimiento;
    
    
}
