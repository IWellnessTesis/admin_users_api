package com.iwellness.admin_users_api.Servicios;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;

@Service
public class UsusariosServicio implements CrudService<Usuarios, Long> {

    @Autowired
    private UsuariosRepositorio UsuarioRepositorio;

    public Usuarios save(Usuarios usuarios) {
        return UsuarioRepositorio.saveAndFlush(usuarios);
    }
    
    public Usuarios update(Usuarios usuario) {
        return UsuarioRepositorio.saveAndFlush(usuario);
    }

    @Override
    public Usuarios findById(Long id) {
        return UsuarioRepositorio.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        UsuarioRepositorio.deleteById(id);
    }

    public boolean existsByNombre(String nombre) {
        return UsuarioRepositorio.existsByNombre(nombre);
    }

    public boolean existsByCorreo(String correo) {
        return UsuarioRepositorio.existsByCorreo(correo);
    }

    public Optional<Usuarios> findByCorre(String correo) {
        return UsuarioRepositorio.findByCorreo(correo);
    }

    public int countByRoleName(String roleName) {
        return UsuarioRepositorio.countByRolName(roleName);
    }


}
