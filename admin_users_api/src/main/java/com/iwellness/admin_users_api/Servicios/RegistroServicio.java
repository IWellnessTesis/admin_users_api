package com.iwellness.admin_users_api.Servicios;

import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistroServicio {

    private final UsuariosRepositorio usuariosRepositorio;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection (no need for @Autowired in newer Spring versions)
    public RegistroServicio(UsuariosRepositorio usuariosRepositorio, PasswordEncoder passwordEncoder) {
        this.usuariosRepositorio = usuariosRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean verificarCorreo(String correo) {
        return usuariosRepositorio.findByCorreo(correo).isPresent();
    }
    

    public void registrarUsuario(Usuarios usuario) {
        // Guardar el usuario
        usuariosRepositorio.save(usuario);
    }
}