package com.iwellness.admin_users_api.Entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Turista")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Turista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "usuarios_id", referencedColumnName = "id", nullable = false) // FK 
    private Usuarios usuarios;

    @Column (nullable = false)
    private int telefono;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String pais;

    @Column
    private String actividadesInteres;
    
    
}
