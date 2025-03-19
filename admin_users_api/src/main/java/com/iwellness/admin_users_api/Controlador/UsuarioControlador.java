package com.iwellness.admin_users_api.Controlador;

import com.iwellness.admin_users_api.DTO.UsuariosDTO;
import com.iwellness.admin_users_api.Entidades.Rol;
import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Repositorios.RolRepositorio;
import com.iwellness.admin_users_api.Servicios.RegistroServicio;
import com.iwellness.admin_users_api.Servicios.UsusariosServicio;
import com.iwellness.admin_users_api.Seguridad.JWTProveedor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class UsuarioControlador {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);

    @Autowired
    private RegistroServicio registroServicio;

    @Autowired
    private RolRepositorio rolRepositorio;

    @Autowired
    UsusariosServicio ususariosServicio;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTProveedor jwtProveedor;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody UsuariosDTO user) {
        try {
            logger.info("Intento de login para usuario: {}", user.getCorreo());
            
            // IMPORTANTE: Corrección del orden de los parámetros
            // El primer parámetro debe ser el nombre de usuario (correo) y el segundo la contraseña
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getCorreo(), user.getContraseña()));
    
            // Establecer la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);
    
            // Generar el JWT Token
            String token = jwtProveedor.TokenGenerado(authentication);
            
            logger.info("Login exitoso para usuario: {}", user.getCorreo());
            
            // Devolver el JWT generado
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (AuthenticationException e) {
            // Registrar el error
            logger.error("Fallo de autenticación para usuario: {}", user.getCorreo(), e);
            
            // Manejar fallo de autenticación
            return new ResponseEntity<>("Autenticación fallida: credenciales inválidas", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            // Registrar cualquier otro error
            logger.error("Error durante el login", e);
            
            // Manejar otros errores
            return new ResponseEntity<>("Error durante el login: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/registro", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registrarUsuario(@RequestBody Map<String, Object> datos) {
        String nombre = (String) datos.get("nombre");
        String correo = (String) datos.get("correo");
        String contraseña = (String) datos.get("contraseña");
        String foto = (String) datos.get("foto");
        Long rolId = ((Number) datos.get("rolId")).longValue();

        try {
            logger.info("Intentando registrar usuario con correo: {}", correo);

            if (registroServicio.verificarCorreo(correo)) {
                logger.warn("Intento de registro con correo ya existente: {}", correo);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El correo ya está registrado");
            }

            logger.info("Buscando rol con ID: {}", rolId);
            Rol rol = rolRepositorio.findById(rolId).orElse(null);

            if (rol == null) {
                logger.error("Error: No se encontró el rol con ID: {}", rolId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El rol no existe");
            }

            //  Encriptar la contraseña con SHA-256
            String contrasenaEncriptada = passwordEncoder.encode(contraseña);

            Usuarios nuevoUsuario = new Usuarios();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setCorreo(correo);
            nuevoUsuario.setContraseña(contrasenaEncriptada);
            nuevoUsuario.setFoto(foto);
            nuevoUsuario.setRol(rol);

            registroServicio.registrarUsuario(nuevoUsuario);

            logger.info("Usuario registrado exitosamente: {}", correo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito");
        } catch (Exception e) {
            logger.error("Error durante el registro del usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during registration");
        }
    }


}
