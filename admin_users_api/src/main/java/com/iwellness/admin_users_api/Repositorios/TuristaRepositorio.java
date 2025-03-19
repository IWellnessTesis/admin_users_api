package com.iwellness.admin_users_api.Repositorios;

import com.iwellness.admin_users_api.Entidades.Turista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TuristaRepositorio extends JpaRepository<Turista, Long> {

}