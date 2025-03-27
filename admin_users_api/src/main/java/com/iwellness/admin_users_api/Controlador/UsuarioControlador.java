package com.iwellness.admin_users_api.Controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Servicios.UsuariosServicio;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioControlador {

    @Autowired
    private UsuariosServicio usuariosServicio;

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
            // Usar el nuevo método que devuelve Map<String, Object>
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
    
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuarios usuario) {
        try {
            Usuarios existingUsuario = usuariosServicio.findById(id);
            if (existingUsuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                       .body("No se encontró el usuario con ID: " + id);
            }
            
            // Asegura que el ID sea el correcto
            usuario.setId(id);
            
            // Actualiza el usuario
            usuariosServicio.update(usuario);
            
            // Retorna los detalles actualizados
            return ResponseEntity.ok(usuariosServicio.findByIdWithDetails(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    @DeleteMapping("/eliminar/{id}")
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
    
}