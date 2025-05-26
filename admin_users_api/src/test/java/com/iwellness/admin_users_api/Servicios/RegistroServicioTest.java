package com.iwellness.admin_users_api.Servicios;

import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Entidades.Rol;
import com.iwellness.admin_users_api.Entidades.Turista;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;
import com.iwellness.admin_users_api.Repositorios.RolRepositorio;
import com.iwellness.admin_users_api.Repositorios.TuristaRepositorio;
import com.iwellness.admin_users_api.Repositorios.ProveedorRepositorio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RegistroServicioTest {

    @Mock
    private UsuariosRepositorio usuariosRepositorio;

    @Mock
    private RolRepositorio rolRepositorio;

    @Mock
    private TuristaRepositorio turistaRepositorio;

    @Mock
    private ProveedorRepositorio proveedorRepositorio;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistroServicio registroServicio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVerificarCorreo_CorreoExistente() {
        String correo = "test@example.com";
        when(usuariosRepositorio.findByCorreo(correo)).thenReturn(Optional.of(new Usuarios()));

        boolean resultado = registroServicio.verificarCorreo(correo);

        assertTrue(resultado, "El método debe devolver true si el correo ya está registrado.");
        verify(usuariosRepositorio).findByCorreo(correo);
    }

    @Test
    void testVerificarCorreo_CorreoNoExistente() {
        String correo = "test@example.com";
        when(usuariosRepositorio.findByCorreo(correo)).thenReturn(Optional.empty());

        boolean resultado = registroServicio.verificarCorreo(correo);

        assertFalse(resultado, "El método debe devolver false si el correo no está registrado.");
        verify(usuariosRepositorio).findByCorreo(correo);
    }
    @Test
    void testRegistrarUsuario_CorreoExistente() {
        // Crear un mapa con TODOS los datos obligatorios
        Map<String, Object> datos = new HashMap<>();
        datos.put("correo", "existente@example.com");
        datos.put("contraseña", "password123");
        datos.put("nombre", "Usuario Test");  // Añadir nombre que es obligatorio
        datos.put("foto", "ruta/foto.jpg");   // Añadir foto si es obligatorio
        
        // Configurar el mock para que devuelva true al verificar si el correo existe
        when(usuariosRepositorio.existsByCorreo("existente@example.com")).thenReturn(true);
        
        // Ejecutar el método a probar
        String resultado = registroServicio.registrarUsuario(datos, "Turista");
        
        // Verificar que el resultado sea el esperado
        assertEquals("Error: El correo ya está registrado", resultado);
        
        // Verificar que nunca se llamó al método save
        verify(usuariosRepositorio, never()).save(any(Usuarios.class));
    }

    @Test
    void testRegistrarUsuario_RolNoEncontrado() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("correo", "nuevo@example.com");
        datos.put("contraseña", "password123");
        datos.put("nombre", "Usuario Test");  // Añadir el nombre
        datos.put("foto", "ruta/foto.jpg");   // Si foto es obligatorio
    
        when(usuariosRepositorio.existsByCorreo("nuevo@example.com")).thenReturn(false);
        when(rolRepositorio.findByNombre("Turista")).thenReturn(Optional.empty());
    
        String resultado = registroServicio.registrarUsuario(datos, "Turista");
    
        assertEquals("Error: No se encontró el rol adecuado", resultado);
        verify(usuariosRepositorio, never()).save(any(Usuarios.class));
    }
}
