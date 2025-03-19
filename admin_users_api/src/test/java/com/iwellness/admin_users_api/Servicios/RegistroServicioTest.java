package com.iwellness.admin_users_api.Servicios;


import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistroServicioTest {

    @Mock
    private UsuariosRepositorio usuariosRepositorio;

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
    void testRegistrarUsuario() {
        Usuarios usuario = new Usuarios();
        usuario.setCorreo("nuevo@example.com");

        registroServicio.registrarUsuario(usuario);

        verify(usuariosRepositorio).save(usuario);
    }
}
