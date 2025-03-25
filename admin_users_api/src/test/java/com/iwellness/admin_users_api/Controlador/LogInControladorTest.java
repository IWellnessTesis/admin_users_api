package com.iwellness.admin_users_api.Controlador;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.iwellness.admin_users_api.DTO.UsuariosDTO;
import com.iwellness.admin_users_api.Repositorios.RolRepositorio;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;
import com.iwellness.admin_users_api.Repositorios.TuristaRepositorio;
import com.iwellness.admin_users_api.Repositorios.ProveedorRepositorio;
import com.iwellness.admin_users_api.Seguridad.JWTProveedor;
import com.iwellness.admin_users_api.Servicios.RegistroServicio;

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
public class LogInControladorTest {

    @InjectMocks
    private LogInControlador usuarioController;

    @Mock
    private RegistroServicio registroServicio;

    @Mock
    private UsuariosRepositorio usuariosRepositorio;

    @Mock
    private RolRepositorio rolRepositorio;

    @Mock
    private TuristaRepositorio turistaRepositorio;

    @Mock
    private ProveedorRepositorio proveedorRepositorio;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTProveedor jwtProveedor;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testLogin_Success() {
       UsuariosDTO userDTO = new UsuariosDTO();
       userDTO.setCorreo("test@example.com");
       userDTO.setContraseña("password");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtProveedor.TokenGenerado(authentication)).thenReturn("mockedToken");
        ResponseEntity<?> response = usuarioController.login(userDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("mockedToken", response.getBody());
    }

    @Test
    void testLogin_Failed() {
        UsuariosDTO userDTO = new UsuariosDTO();
        userDTO.setCorreo("test@example.com");
        userDTO.setContraseña("wrongpassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Authentication failed"));
        ResponseEntity<?> response = usuarioController.login(userDTO);
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Autenticación fallida", response.getBody());
    }

     @Test
    void testRegistrarTurista_Success() {
        // Setup test data
        Map<String, Object> userData = new HashMap<>();
        userData.put("nombre", "Test User");
        userData.put("correo", "test@example.com");
        userData.put("contraseña", "password");
        userData.put("foto", "test.jpg");
        // Otros datos...
        
        // Mock el método registrarUsuario para que retorne "Registro exitoso"
        when(registroServicio.registrarUsuario(any(Map.class), eq("Turista"))).thenReturn("Registro exitoso");
        
        // Call the controller method
        ResponseEntity<String> response = usuarioController.registrarTurista(userData);
        
        // Verify results
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Registro exitoso", response.getBody());
        verify(registroServicio).registrarUsuario(any(Map.class), eq("Turista"));
    }

    @Test
    void testRegistrarProveedor_Success() {
        // Setup test data
        Map<String, Object> userData = new HashMap<>();
        userData.put("nombre", "Empresa XYZ");
        userData.put("correo", "empresa@example.com");
        userData.put("contraseña", "password");
        userData.put("foto", "test.jpg");
        // Otros datos...
        
        // Mock el método registrarUsuario para que retorne "Registro exitoso"
        when(registroServicio.registrarUsuario(any(Map.class), eq("Proveedor"))).thenReturn("Registro exitoso");
        
        // Call the controller method
        ResponseEntity<String> response = usuarioController.registrarProveedor(userData);
        
        // Verify results
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Registro exitoso", response.getBody());
        verify(registroServicio).registrarUsuario(any(Map.class), eq("Proveedor"));
    }

    
}