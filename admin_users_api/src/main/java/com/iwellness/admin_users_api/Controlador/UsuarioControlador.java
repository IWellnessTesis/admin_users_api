package com.iwellness.admin_users_api.Controlador;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.iwellness.admin_users_api.DTO.EditarProveedorDTO;
import com.iwellness.admin_users_api.DTO.EditarTuristaDTO;
import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Servicios.UsuariosServicio;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioControlador {

    @Autowired
    private UsuariosServicio usuariosServicio;

    // Método helper para obtener el usuario actual
    private Usuarios getUsuarioActual() {
        String emailUsuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuariosServicio.findByCorreo(emailUsuarioActual).orElse(null);
    }

    // Método helper para verificar si es admin
    private boolean isAdmin(Usuarios usuario) {
        return usuario != null && "Admin".equals(usuario.getRol().getNombre());
    }

    // Método helper para verificar si es el propietario
    private boolean isOwner(Usuarios usuario, Long id) {
        return usuario != null && usuario.getId().equals(id);
    }

    @GetMapping("/all")
    public ResponseEntity<?> obtenerTodosLosUsuarios() {
        try {
            // Usar el nuevo método que devuelve Map<String, Object>
            return ResponseEntity.ok(usuariosServicio.findAllWithDetails());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error al obtener los usuarios: " + e.getMessage());
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable Long id) {
        try {
            Usuarios usuarioActual = getUsuarioActual();
            
            if (usuarioActual == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                       .body("Usuario no autenticado");
            }
            
            // Verificar permisos: solo admins o el propio usuario pueden acceder
            if (!isAdmin(usuarioActual) && !isOwner(usuarioActual, id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                       .body("No tiene permisos para acceder a este perfil");
            }
            
            Object usuario = usuariosServicio.findByIdWithDetails(id);
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
    
    @PutMapping("/editarTurista/{id}")
    public ResponseEntity<?> editarUsuarioTurista(@PathVariable Long id, @RequestBody EditarTuristaDTO dto) {
        try {
            Usuarios usuarioActual = getUsuarioActual();
            
            if (usuarioActual == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                       .body("Usuario no autenticado");
            }
            
            // Solo admins o el propio usuario pueden editar
            if (!isAdmin(usuarioActual) && !isOwner(usuarioActual, id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                       .body("No tiene permisos para editar este usuario");
            }
            
            Usuarios usuarioActualizado = usuariosServicio.actualizarUsuarioTurista(id, dto);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/editarProveedor/{id}")
    public ResponseEntity<?> editarUsuarioProveedor(@PathVariable Long id, @RequestBody EditarProveedorDTO dto) {
        try {
            Usuarios usuarioActual = getUsuarioActual();
            
            if (usuarioActual == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                       .body("Usuario no autenticado");
            }
            
            // Solo admins o el propio usuario pueden editar
            if (!isAdmin(usuarioActual) && !isOwner(usuarioActual, id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                       .body("No tiene permisos para editar este usuario");
            }
            
            Usuarios usuarioActualizado = usuariosServicio.actualizarUsuarioProveedor(id, dto);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            Usuarios usuarioActual = getUsuarioActual();
            
            if (usuarioActual == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                       .body("Usuario no autenticado");
            }
            
            // Solo administradores pueden eliminar usuarios
            if (!isAdmin(usuarioActual)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                       .body("Solo los administradores pueden eliminar usuarios");
            }
            
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

    @GetMapping("/proveedores")
    public ResponseEntity<?> obtenerProveedores() {
        try {
            Usuarios usuarioActual = getUsuarioActual();
            
            // Solo administradores pueden ver todos los proveedores
            if (!isAdmin(usuarioActual)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                       .body("No tiene permisos para ver todos los proveedores");
            }
            
            return ResponseEntity.ok(usuariosServicio.obtenerProveedores());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error al obtener los proveedores: " + e.getMessage());
        }
    }

    @GetMapping("/turistas")
    public ResponseEntity<?> obtenerTuristas() {
        try {
            Usuarios usuarioActual = getUsuarioActual();
            
            // Solo administradores pueden ver todos los turistas
            if (!isAdmin(usuarioActual)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                       .body("No tiene permisos para ver todos los turistas");
            }
            
            return ResponseEntity.ok(usuariosServicio.obtenerTuristas());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error al obtener los turistas: " + e.getMessage());
        }
    }
}