package com.iwellness.admin_users_api.Controlador;

import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debug")
public class DebugControlador {

    @Autowired
    private UsuariosRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Endpoint para verificar si un usuario existe en la base de datos
     */
    @GetMapping("/check-user")
    public ResponseEntity<?> checkUser(@RequestParam String correo) {
        Usuarios usuario = usuarioRepositorio.findByCorreo(correo).orElse(null);
        if (usuario != null) {
            return ResponseEntity.ok("Usuario encontrado: " + usuario.getNombre() + 
                                    ", Correo: " + usuario.getCorreo() +
                                    ", Rol ID: " + usuario.getRol().getId());
        } else {
            return ResponseEntity.ok("Usuario no encontrado en la base de datos");
        }
    }

    /**
     * Endpoint para verificar si una contraseña coincide con la almacenada
     */
    /**
     * Endpoint para verificar si una contraseña coincide con la almacenada
     */
    @GetMapping("/check-password")
    public ResponseEntity<?> checkPassword(@RequestParam String correo, @RequestParam String contraseña) {
        Optional<Usuarios> usuario = usuarioRepositorio.findByCorreo(correo);
        if (usuario == null || !usuario.isPresent()) {
            return ResponseEntity.ok("Usuario no encontrado");
        }
        
        Usuarios user = usuario.get();
        boolean matches = passwordEncoder.matches(contraseña, user.getContraseña());
        
        return ResponseEntity.ok("Resultado de verificación: " + matches + 
                                "\nContraseña almacenada (hash): " + user.getContraseña().substring(0, 10) + "...");
    }

    @GetMapping("/view-password")
    public ResponseEntity<?> viewStoredPassword(@RequestParam String correo) {
        Optional<Usuarios> usuarioOpt = usuarioRepositorio.findByCorreo(correo);
        if (!usuarioOpt.isPresent()) {
            return ResponseEntity.ok("Usuario no encontrado en la base de datos");
        }
        
        Usuarios usuario = usuarioOpt.get();
        String storedHash = usuario.getContraseña();
        
        // Crear respuesta con detalles sobre el hash almacenado
        StringBuilder response = new StringBuilder();
        response.append("Información del usuario: ").append(usuario.getNombre()).append("\n");
        response.append("Correo: ").append(usuario.getCorreo()).append("\n");
        response.append("Hash de contraseña completo: ").append(storedHash).append("\n");
        response.append("Longitud del hash: ").append(storedHash.length()).append("\n");
        
        // Determinar tipo de hash (si es posible)
        String hashType = "Desconocido";
        if (storedHash.startsWith("$2a$") || storedHash.startsWith("$2b$") || storedHash.startsWith("$2y$")) {
            hashType = "BCrypt";
        } else if (storedHash.startsWith("$argon2")) {
            hashType = "Argon2";
        } else if (storedHash.startsWith("$pbkdf2")) {
            hashType = "PBKDF2";
        } else if (storedHash.startsWith("{SHA}")) {
            hashType = "SHA";
        } else if (storedHash.length() == 60 && storedHash.startsWith("$")) {
            hashType = "Probablemente BCrypt";
        }
        
        response.append("Tipo de hash probable: ").append(hashType);
        
        return ResponseEntity.ok(response.toString());
    }
    
    /**
     * Endpoint para actualizar directamente la contraseña de un usuario para pruebas
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String correo, @RequestParam String nuevaContraseña) {
        Optional<Usuarios> usuario = usuarioRepositorio.findByCorreo(correo);
        if (usuario == null) {
            return ResponseEntity.ok("Usuario no encontrado");
        }
        
        String hashedPassword = passwordEncoder.encode(nuevaContraseña);
        usuario.get().setContraseña(hashedPassword);
        usuarioRepositorio.save(usuario.get());
        
        return ResponseEntity.ok("Contraseña actualizada con éxito. Nuevo hash: " + hashedPassword.substring(0, 10) + "...");
    }
}