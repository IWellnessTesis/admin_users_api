package com.iwellness.admin_users_api.Controlador;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.iwellness.admin_users_api.DTO.EditarTuristaDTO;
import com.iwellness.admin_users_api.DTO.RegistroProveedorDTO;
import com.iwellness.admin_users_api.DTO.RegistroTuristaDTO;
import com.iwellness.admin_users_api.DTO.RegistroUsuarioDTO;
import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Servicios.AdminServicio;
import com.iwellness.admin_users_api.Servicios.UsuariosServicio;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminControlador {

    @Autowired
    private AdminServicio adminServicio;
    
    @Autowired
    private UsuariosServicio usuariosServicio;

    /**
     * Obtener todos los usuarios (turistas y proveedores)
     */
    @GetMapping("/usuarios")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> obtenerTodosLosUsuarios() {
        try {
            return ResponseEntity.ok(usuariosServicio.findAllWithDetails());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error al obtener los usuarios: " + e.getMessage());
        }
    }

    /**
     * Obtener todos los turistas
     */
    @GetMapping("/turistas")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> obtenerTuristas() {
        try {
            return ResponseEntity.ok(usuariosServicio.obtenerTuristas());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error al obtener los turistas: " + e.getMessage());
        }
    }

    /**
     * Obtener todos los proveedores
     */
    @GetMapping("/proveedores")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> obtenerProveedores() {
        try {
            return ResponseEntity.ok(usuariosServicio.obtenerProveedores());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error al obtener los proveedores: " + e.getMessage());
        }
    }

    /**
     * Obtener usuario por ID
     */
    @GetMapping("/usuarios/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable Long id) {
        try {
            Map<String, Object> usuario = usuariosServicio.findByIdWithDetails(id);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                       .body("No se encontró el usuario con ID: " + id);
            }
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error al obtener el usuario: " + e.getMessage());
        }
    }

    /**
     * Crear nuevo admin
     */
    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> crearAdmin(@RequestBody RegistroUsuarioDTO adminDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                   .body(adminServicio.crearAdmin(adminDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body("Error al crear el admin: " + e.getMessage());
        }
    }

    /**
     * Crear nuevo turista
     */
    @PostMapping("/turistas")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> crearTurista(@RequestBody RegistroTuristaDTO turistaDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                   .body(adminServicio.crearTurista(turistaDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body("Error al crear el turista: " + e.getMessage());
        }
    }

    /**
     * Crear nuevo proveedor
     */
    @PostMapping("/proveedores")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> crearProveedor(@RequestBody RegistroProveedorDTO proveedorDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                   .body(adminServicio.crearProveedor(proveedorDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body("Error al crear el proveedor: " + e.getMessage());
        }
    }

    /**
     * Actualizar turista
     */
    @PutMapping("/turistas/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> actualizarTurista(@PathVariable Long id, @RequestBody EditarTuristaDTO turistaDTO) {
        try {
            Usuarios usuarioActualizado = usuariosServicio.actualizarUsuarioTurista(id, turistaDTO);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body("Error al actualizar el turista: " + e.getMessage());
        }
    }

    /**
     * Actualizar proveedor
     */
    @PutMapping("/proveedores/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> actualizarProveedor(@PathVariable Long id, @RequestBody Map<String, Object> proveedorData) {
        try {
            Map<String, Object> usuarioActualizado = adminServicio.actualizarProveedor(id, proveedorData);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body("Error al actualizar el proveedor: " + e.getMessage());
        }
    }

        /**
     * Actualizar proveedor
     */
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> actualizarAdmin(@PathVariable Long id, @RequestBody Map<String, Object> adminData) {
        try {
            Map<String, Object> usuarioActualizado = adminServicio.actualizarAdmin(id, adminData);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body("Error al actualizar el admin: " + e.getMessage());
        }
    }

    /**
     * Eliminar usuario
     */
    @DeleteMapping("/usuarios/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            Usuarios existingUsuario = usuariosServicio.findById(id);
            if (existingUsuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                       .body("No se encontró el usuario con ID: " + id);
            }
            
            usuariosServicio.deleteById(id);
            return ResponseEntity.ok("Usuario eliminado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error al eliminar el usuario: " + e.getMessage());
        }
    }

    /**
     * Estadísticas para el dashboard del administrador
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> obtenerEstadisticas() {
        try {
            return ResponseEntity.ok(adminServicio.obtenerEstadisticas());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error al obtener estadísticas: " + e.getMessage());
        }
    }
}