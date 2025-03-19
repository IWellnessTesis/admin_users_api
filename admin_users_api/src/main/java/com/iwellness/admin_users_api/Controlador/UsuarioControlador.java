package com.iwellness.admin_users_api.Controlador;

import com.iwellness.admin_users_api.DTO.UsuariosDTO;
import com.iwellness.admin_users_api.Entidades.*;
import com.iwellness.admin_users_api.Repositorios.*;
import com.iwellness.admin_users_api.Servicios.RegistroServicio;
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
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.Optional;

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
    private UsuariosRepositorio usuariosRepositorio;

    @Autowired
    private TuristaRepositorio turistaRepositorio;

    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTProveedor jwtProveedor;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        try {
            // Add some logging or debugging statements here
            System.out.println("Registering turista with datos: " + datos);
            return registrarUsuario(datos, "Turista");
        } catch (Exception e) {
            // Log the exception
            System.out.println("Error registering turista: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering turista");
        }
    }

    @PostMapping(value = "/registro/Proveedor", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registrarProveedor(@RequestBody Map<String, Object> datos) {
        return registrarUsuario(datos, "Proveedor");
    }

    private ResponseEntity<String> registrarUsuario(Map<String, Object> datos, String tipo) {
        try {
            String correo = (String) datos.get("correo");
            logger.info("Intento de registro de {} con correo: {}", tipo, correo);
    
            if (usuariosRepositorio.existsByCorreo(correo)) {
                logger.warn("El correo {} ya está registrado", correo);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El correo ya está registrado");
            }
    
            String contrasenaEncriptada = passwordEncoder.encode((String) datos.get("contraseña"));
    
            // Obtener el rol según el tipo de usuario
            Rol rol = rolRepositorio.findByNombre(tipo).orElse(null);
            if (rol == null) {
                logger.error("El rol '{}' no existe en la base de datos", tipo);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: No se encontró el rol adecuado");
            }
    
            Usuarios nuevoUsuario = new Usuarios();
            nuevoUsuario.setNombre((String) datos.get("nombre"));
            nuevoUsuario.setCorreo(correo);
            nuevoUsuario.setContraseña(contrasenaEncriptada);
            nuevoUsuario.setFoto(datos.get("foto") != null ? (String) datos.get("foto") : "default.jpg");
            nuevoUsuario.setRol(rol);
            usuariosRepositorio.save(nuevoUsuario);
            logger.info("Usuario {} registrado exitosamente", correo);
    
            if ("Turista".equals(tipo)) {
                Turista turista = new Turista();
                turista.setUsuarios(nuevoUsuario);
                turista.setTelefono((Integer) datos.get("telefono"));
                turista.setDireccion((String) datos.get("direccion"));
                turista.setCiudad((String) datos.get("ciudad"));
                turista.setPais((String) datos.get("pais"));
                turista.setActividadesInteres((String) datos.get("actividadesInteres"));
                turistaRepositorio.save(turista);
                logger.info("Turista registrado con éxito: {}", correo);
            } else if ("Proveedor".equals(tipo)) {
                Proveedor proveedor = new Proveedor();
                proveedor.setUsuarios(nuevoUsuario);
                proveedor.setNombre_empresa((String) datos.get("nombreEmpresa"));
                proveedor.setDireccion((String) datos.get("direccion"));
                proveedor.setCargoContacto((String) datos.get("cargoContacto"));
                proveedor.setTelefono((String) datos.get("telefono"));
                proveedor.setIdentificacionFiscal((String) datos.get("identificacionFiscal"));
                proveedor.setTelefonoEmpresa((String) datos.get("telefonoEmpresa"));
                proveedor.setLicenciasPermisos((String) datos.get("licenciasPermisos"));
                proveedor.setCertificadosCalidad((String) datos.get("certificadosCalidad"));
                proveedorRepositorio.save(proveedor);
                logger.info("Proveedor registrado con éxito: {}", correo);
            }
    
            return ResponseEntity.status(HttpStatus.CREATED).body("Registro exitoso");
        } catch (Exception e) {
            logger.error("Error en el registro de {} con correo: {}", tipo, datos.get("correo"), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el registro");
        }
    }
    
    
}