package com.iwellness.admin_users_api.Servicios;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iwellness.admin_users_api.DTO.EditarProveedorDTO;
import com.iwellness.admin_users_api.DTO.RegistroProveedorDTO;
import com.iwellness.admin_users_api.DTO.RegistroTuristaDTO;
import com.iwellness.admin_users_api.Entidades.Proveedor;
import com.iwellness.admin_users_api.Entidades.Rol;
import com.iwellness.admin_users_api.Entidades.Turista;
import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Repositorios.ProveedorRepositorio;
import com.iwellness.admin_users_api.Repositorios.RolRepositorio;
import com.iwellness.admin_users_api.Repositorios.TuristaRepositorio;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;

@Service
public class AdminServicio {

    @Autowired
    private UsuariosRepositorio usuariosRepositorio;
    
    @Autowired
    private RolRepositorio rolRepositorio;
    
    @Autowired
    private TuristaRepositorio turistaRepositorio;
    
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Crear un nuevo turista
     */
    @Transactional
    public Map<String, Object> crearTurista(RegistroTuristaDTO dto) {
        // Verificar si el correo ya está registrado
        if (usuariosRepositorio.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        
        // Obtener el rol de turista
        Rol rolTurista = rolRepositorio.findByNombre("Turista")
                .orElseThrow(() -> new RuntimeException("Rol 'Turista' no encontrado"));
        
        // Crear el usuario
        Usuarios usuario = new Usuarios();
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());
        usuario.setContraseña(passwordEncoder.encode(dto.getContraseña()));
        usuario.setFoto(dto.getFoto());
        usuario.setRol(rolTurista);
        
        // Guardar el usuario
        usuario = usuariosRepositorio.save(usuario);
        
        // Crear el turista
        Turista turista = new Turista();
        turista.setUsuarios(usuario);
        turista.setTelefono(dto.getTelefono());
        turista.setCiudad(dto.getCiudad());
        turista.setPais(dto.getPais());
        turista.setActividadesInteres(dto.getActividadesInteres());
        
        // Guardar el turista
        turista = turistaRepositorio.save(turista);
        
        // Preparar respuesta
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("id", usuario.getId());
        respuesta.put("nombre", usuario.getNombre());
        respuesta.put("correo", usuario.getCorreo());
        respuesta.put("mensaje", "Turista creado exitosamente");
        
        return respuesta;
    }
    
    /**
     * Crear un nuevo proveedor
     */
    @Transactional
    public Map<String, Object> crearProveedor(RegistroProveedorDTO dto) {
        // Verificar si el correo ya está registrado
        if (usuariosRepositorio.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        
        // Obtener el rol de proveedor
        Rol rolProveedor = rolRepositorio.findByNombre("Proveedor")
                .orElseThrow(() -> new RuntimeException("Rol 'Proveedor' no encontrado"));
        
        // Crear el usuario
        Usuarios usuario = new Usuarios();
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());
        usuario.setContraseña(passwordEncoder.encode(dto.getContraseña()));
        usuario.setFoto(dto.getFoto());
        usuario.setRol(rolProveedor);
        
        // Guardar el usuario
        usuario = usuariosRepositorio.save(usuario);
        
        // Crear el proveedor
        Proveedor proveedor = new Proveedor();
        proveedor.setUsuarios(usuario);
        proveedor.setNombre_empresa(dto.getNombre_empresa());
        proveedor.setCoordenadaX(dto.getCoordenadaX());
        proveedor.setCoordenadaY(dto.getCoordenadaY());
        proveedor.setCargoContacto(dto.getCargoContacto());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setTelefonoEmpresa(dto.getTelefonoEmpresa());
        
        // Guardar el proveedor
        proveedor = proveedorRepositorio.save(proveedor);
        
        // Preparar respuesta
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("id", usuario.getId());
        respuesta.put("nombre", usuario.getNombre());
        respuesta.put("correo", usuario.getCorreo());
        respuesta.put("mensaje", "Proveedor creado exitosamente");
        
        return respuesta;
    }
    
    /**
     * Actualizar un proveedor existente
     */
    @Transactional
    public Map<String, Object> actualizarProveedor(Long id, Map<String, Object> datosProveedor) {
        // Buscar el usuario
        Usuarios usuario = usuariosRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Verificar que el usuario sea un proveedor
        if (!"Proveedor".equals(usuario.getRol().getNombre())) {
            throw new RuntimeException("El usuario no es un proveedor");
        }
        
        // Actualizar datos del usuario
        if (datosProveedor.containsKey("nombre")) {
            usuario.setNombre((String) datosProveedor.get("nombre"));
        }
        
        if (datosProveedor.containsKey("foto")) {
            usuario.setFoto((String) datosProveedor.get("foto"));
        }
        
        // Guardar usuario
        usuariosRepositorio.save(usuario);
        
        // Actualizar datos del proveedor
        Proveedor proveedor = proveedorRepositorio.findByUsuariosId(id);
        if (proveedor == null) {
            throw new RuntimeException("Información de proveedor no encontrada");
        }
        
        if (datosProveedor.containsKey("nombre_empresa")) {
            proveedor.setNombre_empresa((String) datosProveedor.get("nombre_empresa"));
        }
        
        if (datosProveedor.containsKey("coordenadaX")) {
            proveedor.setCoordenadaX((String) datosProveedor.get("coordenadaX"));
        }
        
        if (datosProveedor.containsKey("coordenadaY")) {
            proveedor.setCoordenadaY((String) datosProveedor.get("coordenadaY"));
        }
        
        if (datosProveedor.containsKey("cargoContacto")) {
            proveedor.setCargoContacto((String) datosProveedor.get("cargoContacto"));
        }
        
        if (datosProveedor.containsKey("telefono")) {
            proveedor.setTelefono((String) datosProveedor.get("telefono"));
        }
        
        if (datosProveedor.containsKey("telefonoEmpresa")) {
            proveedor.setTelefonoEmpresa((String) datosProveedor.get("telefonoEmpresa"));
        }
        
        // Guardar proveedor
        proveedorRepositorio.save(proveedor);
        
        // Preparar respuesta con datos actualizados
        return obtenerProveedorActualizado(id);
    }
    
    /**
     * Obtener datos actualizados del proveedor
     */
    private Map<String, Object> obtenerProveedorActualizado(Long id) {
        Usuarios usuario = usuariosRepositorio.findById(id).orElse(null);
        Proveedor proveedor = proveedorRepositorio.findByUsuariosId(id);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("id", usuario.getId());
        respuesta.put("nombre", usuario.getNombre());
        respuesta.put("correo", usuario.getCorreo());
        respuesta.put("foto", usuario.getFoto());
        
        Map<String, Object> proveedorInfo = new HashMap<>();
        proveedorInfo.put("id", proveedor.getId());
        proveedorInfo.put("nombre_empresa", proveedor.getNombre_empresa());
        proveedorInfo.put("coordenadaX", proveedor.getCoordenadaX());
        proveedorInfo.put("coordenadaY", proveedor.getCoordenadaY());
        proveedorInfo.put("cargoContacto", proveedor.getCargoContacto());
        proveedorInfo.put("telefono", proveedor.getTelefono());
        proveedorInfo.put("telefonoEmpresa", proveedor.getTelefonoEmpresa());
        
        respuesta.put("proveedorInfo", proveedorInfo);
        
        return respuesta;
    }
    
    /**
     * Obtener estadísticas para el dashboard del administrador
     */
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        // Cantidad total de usuarios
        long totalUsuarios = usuariosRepositorio.count();
        estadisticas.put("totalUsuarios", totalUsuarios);
        
        // Cantidad de turistas
        int totalTuristas = usuariosRepositorio.countByRolName("Turista");
        estadisticas.put("totalTuristas", totalTuristas);
        
        // Cantidad de proveedores
        int totalProveedores = usuariosRepositorio.countByRolName("Proveedor");
        estadisticas.put("totalProveedores", totalProveedores);
        
        // Cantidad de administradores
        int totalAdmins = usuariosRepositorio.countByRolName("Admin");
        estadisticas.put("totalAdministradores", totalAdmins);
        
        return estadisticas;
    }
}
