package com.iwellness.admin_users_api.Entidades;


import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // Modificado: Eliminar @JsonIgnore y especificar el mapeo correcto
    @OneToOne(mappedBy = "usuarios", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Turista turista;

    // Modificado: Eliminar @JsonIgnore y especificar el mapeo correcto
    @OneToOne(mappedBy = "usuarios", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Proveedor proveedor;

}
