package com.iwellness.admin_users_api.Repositorios;


import com.iwellness.admin_users_api.Entidades.Turista;
import com.iwellness.admin_users_api.Entidades.Usuarios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TuristaRepositorio extends JpaRepository<Turista, Long> {
    Turista findByUsuariosId(Long usuariosId);

    Optional<Turista> findByUsuarios(Usuarios usuario);
}