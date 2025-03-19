package com.iwellness.admin_users_api.Controlador;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.iwellness.admin_users_api.Controlador.DebugControlador;
import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DebugControladorTest {

    @InjectMocks
    private DebugControlador debugController;

    @Mock
    private UsuariosRepositorio usuarioRepositorio;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    void testCheckUser_NotFound() {
        when(usuarioRepositorio.findByCorreo("test@example.com")).thenReturn(Optional.empty());
        ResponseEntity<?> response = debugController.checkUser("test@example.com");
        assertEquals("Usuario no encontrado en la base de datos", response.getBody());
    }

    @Test
    void testCheckUser_Found() {
        Usuarios usuario = mock(Usuarios.class);
        when(usuarioRepositorio.findByCorreo("test@example.com")).thenReturn(Optional.of(usuario));
        when(usuario.getNombre()).thenReturn("Test User");
        when(usuario.getCorreo()).thenReturn("test@example.com");
        when(usuario.getRol()).thenReturn(mock(com.iwellness.admin_users_api.Entidades.Rol.class));
        when(usuario.getRol().getId()).thenReturn(1L);
        
        ResponseEntity<?> response = debugController.checkUser("test@example.com");
        assertTrue(response.getBody().toString().contains("Usuario encontrado"));
    }

    @Test
    void testCheckPassword_Success() {
        Usuarios usuario = mock(Usuarios.class);
        when(usuarioRepositorio.findByCorreo("test@example.com")).thenReturn(Optional.of(usuario));
        when(usuario.getContraseña()).thenReturn("hashedPassword");
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);

        ResponseEntity<?> response = debugController.checkPassword("test@example.com", "password");
        assertTrue(response.getBody().toString().contains("Resultado de verificación: true"));
    }

    @Test
    void testCheckPassword_Failure() {
        Usuarios usuario = mock(Usuarios.class);
        when(usuarioRepositorio.findByCorreo("test@example.com")).thenReturn(Optional.of(usuario));
        when(usuario.getContraseña()).thenReturn("hashedPassword");
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        ResponseEntity<?> response = debugController.checkPassword("test@example.com", "wrongpassword");
        assertTrue(response.getBody().toString().contains("Resultado de verificación: false"));
    }

    @Test
    void testResetPassword_Success() {
        Usuarios usuario = mock(Usuarios.class);
        when(usuarioRepositorio.findByCorreo("test@example.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("newpassword")).thenReturn("newHashedPassword");
        
        ResponseEntity<?> response = debugController.resetPassword("test@example.com", "newpassword");
        assertTrue(response.getBody().toString().contains("Contraseña actualizada con éxito"));
    }
}