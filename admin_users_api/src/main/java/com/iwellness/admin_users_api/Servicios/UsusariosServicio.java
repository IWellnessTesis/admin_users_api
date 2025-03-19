package com.iwellness.admin_users_api.Servicios;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;

@Service
public class UsusariosServicio implements CrudService<Usuarios, Long> {

    @Autowired
    private UsuariosRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuarios save(Usuarios userEntity) {
        String password = userEntity.getContraseña();
    
        // Codifica solo si la contraseña no está en formato codificado
        if (!isEncoded(password)) {
            String encodedPassword = passwordEncoder.encode(password);
            userEntity.setContraseña(encodedPassword);
        }
    
        return usuarioRepositorio.saveAndFlush(userEntity);
    }

    private boolean isEncoded(String password) {
        // Verifica si la contraseña ya está codificada en Base64 (ejemplo)
        return password.matches("^[A-Za-z0-9+/=]+$") && password.length() >= 44;
    }
    
    public Usuarios update(Usuarios usuario) {
        return usuarioRepositorio.saveAndFlush(usuario);
    }

    @Override
    public Usuarios findById(Long id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        usuarioRepositorio.deleteById(id);
    }

    public boolean existsByNombre(String nombre) {
        return usuarioRepositorio.existsByNombre(nombre);
    }

    public boolean existsByCorreo(String correo) {
        return usuarioRepositorio.existsByCorreo(correo);
    }

    public Optional<Usuarios> findByCorre(String correo) {
        return usuarioRepositorio.findByCorreo(correo);
    }

    public int countByRoleName(String roleName) {
        return usuarioRepositorio.countByRolName(roleName);
    }


}
