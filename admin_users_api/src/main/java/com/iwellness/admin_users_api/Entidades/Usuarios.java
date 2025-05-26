package com.iwellness.admin_users_api.Entidades;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"turista", "proveedor"})
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuarios")
    private Long id;

    @Column
    private String nombre;

    @Column(nullable = false)
    private String correo;

    @Column(nullable = false)
    private String contraseña;

    @Column
    private String foto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @OneToOne(mappedBy = "usuarios", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Turista turista;

    @OneToOne(mappedBy = "usuarios", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Proveedor proveedor;


}
