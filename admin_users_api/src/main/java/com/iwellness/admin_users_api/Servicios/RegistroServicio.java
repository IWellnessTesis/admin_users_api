package com.iwellness.admin_users_api.Servicios;

import com.iwellness.admin_users_api.Entidades.*;
import com.iwellness.admin_users_api.Repositorios.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class RegistroServicio {

    private final UsuariosRepositorio usuariosRepositorio;
    private final RolRepositorio rolRepositorio;
    private final TuristaRepositorio turistaRepositorio;
    private final ProveedorRepositorio proveedorRepositorio;
    private final PasswordEncoder passwordEncoder;

    public RegistroServicio(
            UsuariosRepositorio usuariosRepositorio,
            RolRepositorio rolRepositorio,
            TuristaRepositorio turistaRepositorio,
            ProveedorRepositorio proveedorRepositorio,
            PasswordEncoder passwordEncoder) {
        this.usuariosRepositorio = usuariosRepositorio;
        this.rolRepositorio = rolRepositorio;
        this.turistaRepositorio = turistaRepositorio;
        this.proveedorRepositorio = proveedorRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean verificarCorreo(String correo) {
        return usuariosRepositorio.findByCorreo(correo).isPresent();
    }

    @Transactional
    public String registrarUsuario(Map<String, Object> datos, String tipo) {
        String correo = (String) datos.get("correo");
        String contraseña = (String) datos.get("contraseña");
        String nombre = (String) datos.get("nombre");
        String foto = (String) datos.get("foto");
    
        // Verificar si algún dato obligatorio es nulo
        if (correo == null || contraseña == null || nombre == null) {
            return "Error: Faltan datos obligatorios";
        }
    
        // Verificar si el usuario ya existe
        if (usuariosRepositorio.existsByCorreo(correo)) {
            return "Error: El correo ya está registrado";
        }
    
        // Obtener el rol correspondiente
        Rol rol = rolRepositorio.findByNombre(tipo).orElse(null);
        if (rol == null) {
            return "Error: No se encontró el rol adecuado";
        }
    
        // Encriptar contraseña
        String contraseñaEncriptada = passwordEncoder.encode(contraseña);
    
        // Crear usuario
        Usuarios usuario = new Usuarios();
        usuario.setCorreo(correo);
        usuario.setContraseña(contraseñaEncriptada);
        usuario.setNombre(nombre);
        usuario.setFoto(foto);
        usuario.setRol(rol);
    
        // Guardar usuario primero para obtener su ID
        usuario = usuariosRepositorio.save(usuario);
        
        // Según el tipo, crear y guardar la entidad correspondiente
        if ("Turista".equals(tipo)) {
            crearTurista(usuario, datos);
        } else if ("Proveedor".equals(tipo)) {
            crearProveedor(usuario, datos);
        } else if ("Admin".equals(tipo)) {
            
        }
    
        return "Registro exitoso";
    }
    
    private void crearTurista(Usuarios usuario, Map<String, Object> datos) {
        Turista turista = new Turista();
        turista.setUsuarios(usuario);
        
        // Extraer y establecer datos específicos del turista
        if (datos.containsKey("telefono")) {
            try {
                turista.setTelefono(datos.get("telefono").toString());
            } catch (NumberFormatException e) {
                // Si el valor no es un número válido, podríamos usar un valor predeterminado
                turista.setTelefono("");
            }
        }
    
        if (datos.containsKey("fechaNacimiento")) {
            try {
                Object fechaNacObj = datos.get("fechaNacimiento");
                if (fechaNacObj instanceof String) {
                    // Parse the string into a Date object
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    Date fechaNac = format.parse((String) fechaNacObj);
                    turista.setFechaNacimiento(fechaNac);
                } else if (fechaNacObj instanceof Date) {
                    turista.setFechaNacimiento((Date) fechaNacObj);
                }
            } catch (Exception e) {
                // Handle parsing error, possibly set a default value or log error
                turista.setFechaNacimiento(null);
                System.err.println("Error parsing date: " + e.getMessage());
            }
        } else {
            turista.setFechaNacimiento(null);
        }
            
        turista.setCiudad((String) datos.getOrDefault("ciudad", ""));
        turista.setPais((String) datos.getOrDefault("pais", ""));
        turista.setGenero((String) datos.getOrDefault("genero", ""));
        turista.setEstadoCivil((String) datos.getOrDefault("estadoCivil", ""));
        
        // Guardar el turista
        turistaRepositorio.save(turista);
    }
    
    private void crearProveedor(Usuarios usuario, Map<String, Object> datos) {
        Proveedor proveedor = new Proveedor();
        proveedor.setUsuarios(usuario);
        
        // Extraer y establecer datos específicos del proveedor
        proveedor.setNombre_empresa((String) datos.getOrDefault("nombre_empresa", ""));
        proveedor.setCoordenadaX((String) datos.getOrDefault("coordenadaX", "0"));
        proveedor.setCoordenadaY((String) datos.getOrDefault("coordenadaY", "0"));
        proveedor.setCargoContacto((String) datos.getOrDefault("cargoContacto", ""));
        proveedor.setTelefono((String) datos.getOrDefault("telefono", ""));
        proveedor.setTelefonoEmpresa((String) datos.getOrDefault("telefonoEmpresa", ""));
        
        // Guardar el proveedor
        proveedorRepositorio.save(proveedor);
    }
}