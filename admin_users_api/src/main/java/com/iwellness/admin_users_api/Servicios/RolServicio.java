package com.iwellness.admin_users_api.Servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iwellness.admin_users_api.Entidades.Rol;
import com.iwellness.admin_users_api.Repositorios.RolRepositorio;

@Service
public class RolServicio {

    @Autowired
    private RolRepositorio rolRepositorio;

    public Optional<Rol> findByNombre(String nombre) {
        return rolRepositorio.findByNombre(nombre);
    }

    public boolean existsByName(String nombre) {
        return rolRepositorio.existsByNombre(nombre);
    }

    public boolean existsById(Long id) {
        return rolRepositorio.existsById(id);
    }

    public Rol save(Rol rol) {
        return rolRepositorio.save(rol);
    }

    public List<Rol> findAll() {
        return rolRepositorio.findAll();
    }

    
}
