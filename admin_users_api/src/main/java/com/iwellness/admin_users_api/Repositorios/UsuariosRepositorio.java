package com.iwellness.admin_users_api.Repositorios;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    
}
