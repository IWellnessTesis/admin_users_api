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
        
        // Log para ver todos los datos recibidos
        System.out.println("Datos recibidos para registro de turista: " + datos);
        
        // Extraer y establecer datos específicos del turista
        if (datos.containsKey("telefono")) {
            try {
                turista.setTelefono(datos.get("telefono").toString());
                System.out.println("Teléfono establecido: " + turista.getTelefono());
            } catch (NumberFormatException e) {
                // Si el valor no es un número válido, podríamos usar un valor predeterminado
                turista.setTelefono("");
                System.out.println("Error al procesar teléfono, establecido valor vacío");
            }
        }
    
        if (datos.containsKey("fechaNacimiento")) {
            try {
                Object fechaNacObj = datos.get("fechaNacimiento");
                System.out.println("Objeto fechaNacimiento recibido: " + fechaNacObj + " de tipo: " + fechaNacObj.getClass().getName());
                
                if (fechaNacObj instanceof String) {
                    // Parse the string into a Date object
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    Date fechaNac = format.parse((String) fechaNacObj);
                    turista.setFechaNacimiento(fechaNac);
                    System.out.println("Fecha de nacimiento parseada desde String: " + fechaNac);
                } else if (fechaNacObj instanceof Date) {
                    turista.setFechaNacimiento((Date) fechaNacObj);
                    System.out.println("Fecha de nacimiento establecida desde Date: " + fechaNacObj);
                }
            } catch (Exception e) {
                // Handle parsing error, possibly set a default value or log error
                turista.setFechaNacimiento(null);
                System.err.println("Error parsing date: " + e.getMessage());
            }
        } else {
            turista.setFechaNacimiento(null);
            System.out.println("No se proporcionó fecha de nacimiento");
        }
            
        turista.setCiudad((String) datos.getOrDefault("ciudad", ""));
        System.out.println("Ciudad establecida: " + turista.getCiudad());
        
        turista.setPais((String) datos.getOrDefault("pais", ""));
        System.out.println("País establecido: " + turista.getPais());
        
        turista.setGenero((String) datos.getOrDefault("genero", ""));
        System.out.println("Género establecido: " + turista.getGenero());
        
        // Verificar específicamente el estado civil
        if (datos.containsKey("estadoCivil")) {
            String estadoCivil = (String) datos.get("estadoCivil");
            turista.setEstadoCivil(estadoCivil);
            System.out.println("Estado civil establecido explícitamente: " + estadoCivil);
        } else {
            turista.setEstadoCivil("");
            System.out.println("No se encontró 'estadoCivil' en los datos, establecido a vacío");
        }
        
        // Guardar el turista
        Turista turistaGuardado = turistaRepositorio.save(turista);
        System.out.println("Turista guardado con ID: " + turistaGuardado.getId() + ", Estado Civil: " + turistaGuardado.getEstadoCivil());
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
        Proveedor proveedorGuardado = proveedorRepositorio.save(proveedor);

        //Asociar el proveedor con el usuario
        usuario.setProveedor(proveedorGuardado);
        usuariosRepositorio.save(usuario);
    }
}