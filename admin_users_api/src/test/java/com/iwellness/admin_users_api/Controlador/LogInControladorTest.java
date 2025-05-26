package com.iwellness.admin_users_api.Controlador;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.iwellness.admin_users_api.DTO.UsuariosDTO;
import com.iwellness.admin_users_api.Entidades.Proveedor;
import com.iwellness.admin_users_api.Entidades.Turista;
import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Repositorios.RolRepositorio;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;
import com.iwellness.admin_users_api.Repositorios.TuristaRepositorio;
import com.iwellness.admin_users_api.Repositorios.ProveedorRepositorio;
import com.iwellness.admin_users_api.Seguridad.JWTProveedor;
import com.iwellness.admin_users_api.Servicios.RegistroServicio;
import com.iwellness.admin_users_api.Servicios.UsuariosServicio;
import com.iwellness.admin_users_api.Servicios.Rabbit.MensajeServiceUsers;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LogInControladorTest {

    @InjectMocks
    private LogInControlador usuarioController;

    @Mock
    private RegistroServicio registroServicio;

    @Mock
    private UsuariosServicio usuariosServicio;

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
    private MensajeServiceUsers mensajeServiceUsers;

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
    void testRegistrarTurista_Success() throws Exception {
        // Datos de entrada
        Map<String, Object> userData = new HashMap<>();
        userData.put("nombre", "Test User");
        userData.put("correo", "test@example.com");
        userData.put("contraseña", "password");
        userData.put("foto", "test.jpg");

        // Mock registrarUsuario para que retorne éxito
        when(registroServicio.registrarUsuario(any(Map.class), eq("Turista"))).thenReturn("Registro exitoso");

        // Mock usuariosServicio.findByCorreo para devolver un usuario válido
        Usuarios nuevoTurista = new Usuarios();
        nuevoTurista.setNombre("Test User");
        // Setear más campos si necesario
        when(usuariosServicio.findByCorreo("test@example.com")).thenReturn(Optional.of(nuevoTurista));

        // Mock turistaRepositorio.findByUsuarios para devolver un turista válido
        Turista turista = new Turista();
        turista.setId(123L);
        turista.setTelefono("123456789");
        turista.setCiudad("Ciudad");
        turista.setPais("Pais");
        turista.setGenero("M");
        turista.setEstadoCivil("Soltero");
        when(turistaRepositorio.findByUsuarios(nuevoTurista)).thenReturn(Optional.of(turista));

        // Mock mensajeServiceUsers.enviarMensajeTurista para que no haga nada (void)
        doNothing().when(mensajeServiceUsers).enviarMensajeTurista(anyString());

        // Llamar al controlador
        ResponseEntity<String> response = usuarioController.registrarTurista(userData);

        // Verificaciones
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Registro exitoso", response.getBody());

        verify(registroServicio).registrarUsuario(any(Map.class), eq("Turista"));
        verify(usuariosServicio).findByCorreo("test@example.com");
        verify(turistaRepositorio).findByUsuarios(nuevoTurista);
        verify(mensajeServiceUsers).enviarMensajeTurista(anyString());
    }


    @Test
    void testRegistrarProveedor_Success() throws Exception {
        Map<String, Object> userData = new HashMap<>();
        userData.put("nombre", "Empresa XYZ");
        userData.put("correo", "empresa@example.com");
        userData.put("contraseña", "password");
        userData.put("foto", "test.jpg");

        // Mock registrarUsuario
        when(registroServicio.registrarUsuario(any(Map.class), eq("Proveedor"))).thenReturn("Registro exitoso");

        // Mock authenticationManager.authenticate
        Authentication authMock = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authMock);

        // Mock jwtProveedor.TokenGenerado
        when(jwtProveedor.TokenGenerado(authMock)).thenReturn("token123");

        // Mock usuario con proveedor
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre_empresa("Empresa XYZ");
        proveedor.setCargoContacto("Contacto");
        proveedor.setTelefono("123456789");
        proveedor.setTelefonoEmpresa("987654321");
        proveedor.setCoordenadaX("10.0");
        proveedor.setCoordenadaY("20.0");

        Usuarios nuevoProveedor = new Usuarios();
        nuevoProveedor.setId(1L);
        nuevoProveedor.setNombre("Empresa XYZ");
        nuevoProveedor.setProveedor(proveedor);

        when(usuariosServicio.findByCorreo("empresa@example.com")).thenReturn(Optional.of(nuevoProveedor));

        // Mock mensajeServiceUsers.enviarMensajeProveedor (void)
        doNothing().when(mensajeServiceUsers).enviarMensajeProveedor(anyString());

        // Llamar controlador
        ResponseEntity<?> response = usuarioController.registrarProveedor(userData);

        // Verificaciones
        assertEquals(201, response.getStatusCodeValue());

        // El cuerpo es un Map<String, String>, así que podemos verificar los valores
        @SuppressWarnings("unchecked")
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertNotNull(body);
        assertEquals("Registro exitoso", body.get("message"));
        assertEquals("token123", body.get("token"));

        verify(registroServicio).registrarUsuario(any(Map.class), eq("Proveedor"));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtProveedor).TokenGenerado(authMock);
        verify(usuariosServicio).findByCorreo("empresa@example.com");
        verify(mensajeServiceUsers).enviarMensajeProveedor(anyString());
    }

    
}