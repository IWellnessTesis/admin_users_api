package com.iwellness.admin_users_api.Controlador;

import com.iwellness.admin_users_api.Entidades.Rol;
import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Servicios.RegistroServicio;
import com.iwellness.admin_users_api.Repositorios.RolRepositorio;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import com.iwellness.admin_users_api.Seguridad.JWTProveedor;

@RestController
@RequestMapping("/auth")
public class UsuarioController {


    @Autowired
    private RegistroServicio registroServicio; // Servicio de registro

    @Autowired
    private RolRepositorio rolRepositorio; // Repositorio de roles

    @Autowired
    private AuthenticationManager authenticationManager; // Para la autenticación

    @Autowired
    private JWTProveedor jwtProveedor; // Para generar el JWT

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Para encriptar la contraseña

    // Endpoint para el Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String correo, @RequestParam String contraseña) {
        try {
            // Autenticamos al usuario con los datos proporcionados
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(correo, contraseña)
            );
    
            // Generar el JWT para el usuario autenticado
            String token = jwtProveedor.TokenGenerado(authentication);
    
            // Devolver el JWT generado directamente, sin el prefijo "Bearer"
            return new ResponseEntity<>(token, HttpStatus.OK); // Enviar solo el token
    
        } catch (AuthenticationException e) {
            // Manejar fallo de autenticación: credenciales incorrectas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: Invalid credentials");
        } catch (Exception e) {
            // Manejar otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during login");
        }
    }
    
   
    // Endpoint para el Registro de Usuario
    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuario(@RequestParam String nombre, 
                                                   @RequestParam String correo, 
                                                   @RequestParam String contraseña, 
                                                   @RequestParam String foto, 
                                                   @RequestParam Long rolId) {
        try {
            // Verificar si el correo ya está registrado
            if (registroServicio.verificarCorreo(correo)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El correo ya está registrado");
            }

            // Buscar el rol por ID
            Rol rol = rolRepositorio.findById(rolId).orElse(null);
            if (rol == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El rol no existe");
            }

            // Encriptar la contraseña
            String contrasenaEncriptada = passwordEncoder.encode(contraseña);

            // Crear el usuario
            Usuarios nuevoUsuario = new Usuarios();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setCorreo(correo);
            nuevoUsuario.setContraseña(contrasenaEncriptada);
            nuevoUsuario.setFoto(foto);
            nuevoUsuario.setRol(rol);

            // Guardar el usuario en la base de datos
            registroServicio.registrarUsuario(nuevoUsuario);

            // Devolver respuesta de éxito
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito");

        } catch (Exception e) {
            // Manejar otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during registration");
        }
    }

}
