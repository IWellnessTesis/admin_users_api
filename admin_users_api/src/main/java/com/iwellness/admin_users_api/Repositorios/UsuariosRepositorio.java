package com.iwellness.admin_users_api.Repositorios;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iwellness.admin_users_api.Entidades.Usuarios;

@Repository
public interface  UsuariosRepositorio extends JpaRepository<Usuarios, Long> {

    Usuarios findByNombre(String nombre);

    Optional<Usuarios> findByCorreo(String correo);
    
    boolean existsByNombre(String nombre);

    boolean existsByCorreo(String correo);

    @Query("SELECT COUNT(u) FROM Usuarios u WHERE u.rol.nombre = :rolNombre")
    int countByRolName(String rolNombre);

    @Query("SELECT u FROM Usuarios u LEFT JOIN FETCH u.turista LEFT JOIN FETCH u.proveedor JOIN FETCH u.rol")
    List<Usuarios> findAllWithDetails();
    
    @Query("SELECT u FROM Usuarios u LEFT JOIN FETCH u.turista LEFT JOIN FETCH u.proveedor JOIN FETCH u.rol WHERE u.id = :id")
    Optional<Usuarios> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT u, t, r FROM Usuarios u " +
    "LEFT JOIN u.turista t " +
    "JOIN u.rol r " +
    "WHERE u.id = :id AND r.nombre = 'Turista'")
    List<Object[]> findUsuarioTuristaById(@Param("id") Long id);

    @Query("SELECT u, p, r FROM Usuarios u " +
        "LEFT JOIN u.proveedor p " +
        "JOIN u.rol r " +
        "WHERE u.id = :id AND r.nombre = 'Proveedor'")
    List<Object[]> findUsuarioProveedorById(@Param("id") Long id);

    // Consulta para obtener todos los usuarios con sus detalles según el rol
    @Query("SELECT u, t, p, r FROM Usuarios u " +
        "LEFT JOIN u.turista t " +
        "LEFT JOIN u.proveedor p " +
        "JOIN u.rol r")
    List<Object[]> findAllWithRoleInfo();

}
