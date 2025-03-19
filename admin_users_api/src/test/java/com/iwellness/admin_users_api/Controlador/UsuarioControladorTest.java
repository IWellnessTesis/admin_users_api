package com.iwellness.admin_users_api.Controlador;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.iwellness.admin_users_api.DTO.UsuariosDTO;
import com.iwellness.admin_users_api.Repositorios.RolRepositorio;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;
import com.iwellness.admin_users_api.Seguridad.JWTProveedor;
import com.iwellness.admin_users_api.Servicios.RegistroServicio;
import com.iwellness.admin_users_api.Servicios.UsusariosServicio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class UsuarioControladorTest {

    @InjectMocks
    private UsuarioControlador usuarioController;

    @Mock
    private RegistroServicio registroServicio;

    @Mock
    private RolRepositorio rolRepositorio;

    @Mock
    private UsuariosRepositorio usuarioRepositorio;

    @Mock
    private UsusariosServicio ususariosServicio;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTProveedor jwtProveedor;
    
    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    void testLogin_Success() {
        // Arrange
        UsuariosDTO userDTO = new UsuariosDTO();
        userDTO.setCorreo("test@example.com");
        userDTO.setContraseña("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtProveedor.TokenGenerado(authentication)).thenReturn("mockedToken");

        // Act
        ResponseEntity<?> response = usuarioController.login(userDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("mockedToken", response.getBody());
    }

    @Test
    void testLogin_Failed() {
        // Arrange
        UsuariosDTO userDTO = new UsuariosDTO();
        userDTO.setCorreo("test@example.com");
        userDTO.setContraseña("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Authentication failed"));

        // Act
        ResponseEntity<?> response = usuarioController.login(userDTO);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Autenticación fallida: credenciales inválidas", response.getBody());
    }

    @Test
    void testRegistrarUsuario_Success() {
        // Arrange
        Map<String, Object> userData = new HashMap<>();
        userData.put("nombre", "Test User");
        userData.put("correo", "test@example.com");
        userData.put("contraseña", "password");
        userData.put("foto", "test.jpg");
        userData.put("rolId", 1L);

        when(registroServicio.verificarCorreo("test@example.com")).thenReturn(false);
        when(rolRepositorio.findById(1L)).thenReturn(java.util.Optional.of(mock(com.iwellness.admin_users_api.Entidades.Rol.class)));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        doNothing().when(registroServicio).registrarUsuario(any());

        // Act
        ResponseEntity<String> response = usuarioController.registrarUsuario(userData);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Usuario registrado con éxito", response.getBody());
    }
}
