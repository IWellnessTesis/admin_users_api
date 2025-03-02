package com.iwellness.admin_users_api.Servicios;


import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuariosRepositorio usuariosRepositorio;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; 

    public Usuarios autenticarUsuario(String correo, String contraseña) {
        // Buscar al usuario en la base de datos por correo
        Optional<Usuarios> usuario = usuariosRepositorio.findByCorreo(correo);
    
        if (usuario.isPresent() && passwordEncoder.matches(contraseña, usuario.get().getContraseña())) {
            return usuario.get(); // Si la contraseña es correcta, retornar el usuario
        } else {
            return null; // Si no existe o la contraseña no es correcta, retornar null
        }
    }
}
