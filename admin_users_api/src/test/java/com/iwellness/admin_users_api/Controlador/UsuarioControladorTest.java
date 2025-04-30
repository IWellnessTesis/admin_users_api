package com.iwellness.admin_users_api.Controlador;

import com.iwellness.admin_users_api.DTO.EditarTuristaDTO;
import com.iwellness.admin_users_api.Entidades.Turista;
import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Servicios.UsuariosServicio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControladorTest {

    @Mock
    private UsuariosServicio usuariosServicio;

    @InjectMocks
    private UsuarioControlador usuarioControlador;

    @Test
    void obtenerUsuarios_DeberiaRetornarListaUsuarios() {
        // Crear una lista de mapas como devuelve el nuevo método findAllWithDetails
        List<Map<String, Object>> usuarios = new ArrayList<>();
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("id", 1L);
        usuario.put("nombre", "Test User");
        usuario.put("correo", "test@example.com");
        usuarios.add(usuario);
        
        when(usuariosServicio.findAllWithDetails()).thenReturn(usuarios);

        ResponseEntity<?> response = usuarioControlador.obtenerTodosLosUsuarios();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarios, response.getBody());
    }

    @Test
    void obtenerUsuarios_DeberiaRetornarError() {
        when(usuariosServicio.findAllWithDetails()).thenThrow(new RuntimeException("Error en DB"));

        ResponseEntity<?> response = usuarioControlador.obtenerTodosLosUsuarios();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error al obtener los usuarios"));
    }

    @Test
    void obtenerUsuarioPorId_UsuarioExistente() {
        // Crear un mapa como devuelve el nuevo método findByIdWithDetails
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("id", 1L);
        usuario.put("nombre", "Test User");
        usuario.put("correo", "test@example.com");
        
        when(usuariosServicio.findByIdWithDetails(1L)).thenReturn(usuario);

        ResponseEntity<?> response = usuarioControlador.obtenerUsuarioPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
    }

    @Test
    void obtenerUsuarioPorId_UsuarioNoEncontrado() {
        when(usuariosServicio.findByIdWithDetails(1L)).thenReturn(null);
    
        ResponseEntity<?> response = usuarioControlador.obtenerUsuarioPorId(1L);
    
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("No se encontró el usuario con ID: 1"));
    }

     @Test
     void actualizarUsuario_DeberiaActualizarUsuario() {
        // Arrange
        // Creamos el DTO con los datos de actualización (nombre, teléfono, ciudad, país)
        EditarTuristaDTO dto = new EditarTuristaDTO();
        dto.setNombre("Test User");
        dto.setTelefono("123456789");
        dto.setCiudad("Test City");
        dto.setPais("Test Country");

        // Creamos el usuario inicial y su relación con Turista
        Usuarios usuarioInicial = new Usuarios();
        usuarioInicial.setId(1L);
        usuarioInicial.setNombre("Original Name");
        Turista turistaInicial = new Turista();
        turistaInicial.setId(1L);
        turistaInicial.setTelefono("987654321");
        turistaInicial.setCiudad("Old City");
        turistaInicial.setPais("Old Country");
        turistaInicial.setUsuarios(usuarioInicial);
        usuarioInicial.setTurista(turistaInicial);

        // Creamos el objeto Usuario tal como se espera después de actualizar
        Usuarios usuarioActualizado = new Usuarios();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setNombre(dto.getNombre());
        Turista turistaActualizada = new Turista();
        turistaActualizada.setId(1L);
        turistaActualizada.setTelefono(dto.getTelefono());
        turistaActualizada.setCiudad(dto.getCiudad());
        turistaActualizada.setPais(dto.getPais());
        turistaActualizada.setUsuarios(usuarioActualizado);
        usuarioActualizado.setTurista(turistaActualizada);

        // Simulamos la llamada al método actualizarUsuarioTurista del servicio
        when(usuariosServicio.actualizarUsuarioTurista(1L, dto)).thenReturn(usuarioActualizado);

        // Act
        ResponseEntity<?> response = usuarioControlador.editarUsuarioTurista(1L, dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioActualizado, response.getBody());
    }

    @Test
    void eliminarUsuario_DeberiaEliminarUsuario() {
        // Mock para el método findById
        Usuarios usuario = new Usuarios();
        usuario.setId(1L);
        when(usuariosServicio.findById(1L)).thenReturn(usuario);
        
        doNothing().when(usuariosServicio).deleteById(1L);

        ResponseEntity<?> response = usuarioControlador.eliminarUsuario(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario eliminado con éxito", response.getBody());
    }
}