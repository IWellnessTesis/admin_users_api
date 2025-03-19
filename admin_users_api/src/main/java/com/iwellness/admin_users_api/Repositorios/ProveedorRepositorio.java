package com.iwellness.admin_users_api.Repositorios;

import com.iwellness.admin_users_api.Entidades.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepositorio extends JpaRepository<Proveedor, Long> {

}
