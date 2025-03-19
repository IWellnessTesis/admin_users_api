package com.iwellness.admin_users_api.Controlador;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.iwellness.admin_users_api.DTO.UsuariosDTO;
import com.iwellness.admin_users_api.Entidades.Proveedor;
import com.iwellness.admin_users_api.Entidades.Rol;
import com.iwellness.admin_users_api.Entidades.Turista;
import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Repositorios.RolRepositorio;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;
import com.iwellness.admin_users_api.Repositorios.TuristaRepositorio;
import com.iwellness.admin_users_api.Repositorios.ProveedorRepositorio;
import com.iwellness.admin_users_api.Seguridad.JWTProveedor;
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
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UsuarioControladorTest {

    @InjectMocks
    private UsuarioControlador usuarioController;

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
        userData.put("telefono", 88887777);
        userData.put("direccion", "Calle 123");
        userData.put("ciudad", "San José");
        userData.put("pais", "Costa Rica");
        userData.put("actividadesInteres", "Senderismo");
        
        // Mock email check
        when(usuariosRepositorio.existsByCorreo("test@example.com")).thenReturn(false);
        
        // Mock role lookup - note controller uses findByNombre, not findById
        Rol turistaRol = new Rol();
        turistaRol.setNombre("Turista");
        when(rolRepositorio.findByNombre("Turista")).thenReturn(Optional.of(turistaRol));
        
        // Mock password encoding
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        
        // Mock user creation
        Usuarios nuevoUsuario = new Usuarios();
        nuevoUsuario.setId(1L);
        when(usuariosRepositorio.save(any(Usuarios.class))).thenReturn(nuevoUsuario);
        
        // Mock tourist creation
        Turista nuevoTurista = new Turista();
        when(turistaRepositorio.save(any(Turista.class))).thenReturn(nuevoTurista);
        
        // Call the controller method
        ResponseEntity<String> response = usuarioController.registrarTurista(userData);
        
        // Verify results
        assertEquals(201, response.getStatusCodeValue(), "Should return 201 Created status");
        assertEquals("Registro exitoso", response.getBody(), "Response body should confirm success");
    }

    @Test
    void testRegistrarProveedor_Success() {
        // Setup test data
        Map<String, Object> userData = new HashMap<>();
        userData.put("nombre", "Empresa XYZ");
        userData.put("correo", "empresa@example.com");
        userData.put("contraseña", "password");
        userData.put("foto", "test.jpg");
        userData.put("nombreEmpresa", "EcoTours");  
        userData.put("direccion", "Avenida Central");
        userData.put("cargoContacto", "Gerente");
        userData.put("telefono", "88887777");
        userData.put("identificacionFiscal", "J-12345678");
        userData.put("telefonoEmpresa", "22223333");
        userData.put("licenciasPermisos", "Licencia.pdf");
        userData.put("certificadosCalidad", "Certificado.pdf");
        
        // Mock email check
        when(usuariosRepositorio.existsByCorreo("empresa@example.com")).thenReturn(false);
        
        // Mock role lookup - use findByNombre instead of findById
        Rol proveedorRol = new Rol();
        proveedorRol.setNombre("Proveedor");
        when(rolRepositorio.findByNombre("Proveedor")).thenReturn(Optional.of(proveedorRol));
        
        // Mock password encoding
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        
        // Mock user creation
        Usuarios nuevoUsuario = new Usuarios();
        nuevoUsuario.setId(1L);
        when(usuariosRepositorio.save(any(Usuarios.class))).thenReturn(nuevoUsuario);
        
        // Mock provider creation
        Proveedor nuevoProveedor = new Proveedor();
        when(proveedorRepositorio.save(any(Proveedor.class))).thenReturn(nuevoProveedor);
        
        // Call the controller method
        ResponseEntity<String> response = usuarioController.registrarProveedor(userData);
        
        // Verify results
        assertEquals(201, response.getStatusCodeValue(), "Should return 201 Created status");
        assertEquals("Registro exitoso", response.getBody(), "Response body should confirm success");
    }
}