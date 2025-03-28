package com.iwellness.admin_users_api.Controlador;

import com.iwellness.admin_users_api.DTO.UsuariosDTO;
import com.iwellness.admin_users_api.Servicios.RegistroServicio;
import com.iwellness.admin_users_api.Seguridad.JWTProveedor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class LogInControlador {

    private static final Logger logger = LoggerFactory.getLogger(LogInControlador.class);

    @Autowired
    private RegistroServicio registroServicio;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTProveedor jwtProveedor;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody UsuariosDTO user) {
        try {
            logger.info("Intento de inicio de sesión para el usuario: {}", user.getCorreo());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getCorreo(), user.getContraseña()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProveedor.TokenGenerado(authentication);
            logger.info("Inicio de sesión exitoso para el usuario: {}", user.getCorreo());
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error de autenticación para el usuario: {}", user.getCorreo(), e);
            return new ResponseEntity<>("Autenticación fallida", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/registro/Turista", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registrarTurista(@RequestBody Map<String, Object> datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registroServicio.registrarUsuario(datos, "Turista"));
    }

    @PostMapping(value = "/registro/Proveedor", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registrarProveedor(@RequestBody Map<String, Object> datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registroServicio.registrarUsuario(datos, "Proveedor"));
    }
}
