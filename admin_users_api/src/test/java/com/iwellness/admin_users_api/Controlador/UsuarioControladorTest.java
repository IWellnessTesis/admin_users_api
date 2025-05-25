package com.iwellness.admin_users_api.Controlador;

import com.iwellness.admin_users_api.DTO.EditarTuristaDTO;
import com.iwellness.admin_users_api.DTO.TuristaDTO;
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
    Usuarios usuarioAutenticado = new Usuarios();
    usuarioAutenticado.setId(1L);
    usuarioAutenticado.setNombre("Test User");
    usuarioAutenticado.setCorreo("test@example.com");

    UsuarioControlador spyControlador = spy(usuarioControlador);

    doReturn(usuarioAutenticado).when(spyControlador).getUsuarioActual();

    // Marcar estos stubbings como lenient para evitar el error
    lenient().doReturn(true).when(spyControlador).isAdmin(usuarioAutenticado);
    lenient().doReturn(true).when(spyControlador).isOwner(usuarioAutenticado, 1L);

    Map<String, Object> usuario = new HashMap<>();
    usuario.put("id", 1L);
    usuario.put("nombre", "Test User");
    usuario.put("correo", "test@example.com");

    when(usuariosServicio.findByIdWithDetails(1L)).thenReturn(usuario);

    ResponseEntity<?> response = spyControlador.obtenerUsuarioPorId(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(usuario, response.getBody());
}



@Test
void obtenerUsuarioPorId_UsuarioNoEncontrado() {
    Usuarios usuarioAutenticado = new Usuarios();
    usuarioAutenticado.setId(1L);
    usuarioAutenticado.setNombre("Usuario Admin");
    usuarioAutenticado.setCorreo("admin@example.com");

    // Crear spy para el controlador para mockear getUsuarioActual e isAdmin/isOwner
    UsuarioControlador spyControlador = spy(usuarioControlador);

    // Mockeamos usuario autenticado para pasar la validación de autenticación
    doReturn(usuarioAutenticado).when(spyControlador).getUsuarioActual();

    // Asumimos que es admin o dueño para pasar permisos
    lenient().doReturn(true).when(spyControlador).isAdmin(usuarioAutenticado);
    lenient().doReturn(true).when(spyControlador).isOwner(usuarioAutenticado, 1L);

    // Mockeamos que no encontró el usuario con detalles (retorna null)
    when(usuariosServicio.findByIdWithDetails(1L)).thenReturn(null);

    ResponseEntity<?> response = spyControlador.obtenerUsuarioPorId(1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertTrue(response.getBody().toString().contains("No se encontró el usuario con ID: 1"));
}
}