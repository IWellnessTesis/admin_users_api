package com.iwellness.admin_users_api.Servicios;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UsuariosServicioTest {

    @InjectMocks
    private UsuariosServicio ususariosServicio;

    @Mock
    private UsuariosRepositorio usuarioRepositorio;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    void testSave_NewPassword_ShouldEncode() {
        Usuarios usuario = new Usuarios();
        usuario.setContraseña("plaintext");
        
        when(passwordEncoder.encode("plaintext")).thenReturn("encodedPassword");
        when(usuarioRepositorio.saveAndFlush(usuario)).thenReturn(usuario);

        Usuarios result = ususariosServicio.save(usuario);

        assertEquals("encodedPassword", result.getContraseña());
        verify(passwordEncoder).encode("plaintext");
        verify(usuarioRepositorio).saveAndFlush(usuario);
    }
    
    
    @Test
    void testFindById_UserExists() {
        Usuarios usuario = new Usuarios();
        when(usuarioRepositorio.findById(1L)).thenReturn(Optional.of(usuario));

        Usuarios result = ususariosServicio.findById(1L);

        assertNotNull(result);
    }

    @Test
    void testFindById_UserDoesNotExist() {
        when(usuarioRepositorio.findById(1L)).thenReturn(Optional.empty());

        Usuarios result = ususariosServicio.findById(1L);

        assertNull(result);
    }

    @Test
    void testExistsByCorreo() {
        when(usuarioRepositorio.existsByCorreo("test@example.com")).thenReturn(true);

        boolean exists = ususariosServicio.existsByCorreo("test@example.com");

        assertTrue(exists);
    }
}