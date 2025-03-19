package com.iwellness.admin_users_api.Servicios;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.iwellness.admin_users_api.Entidades.Rol;
import com.iwellness.admin_users_api.Repositorios.RolRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RolServicioTest {

    @InjectMocks
    private RolServicio rolServicio;

    @Mock
    private RolRepositorio rolRepositorio;

    @Test
    void testFindByNombre_Found() {
        Rol rol = new Rol();
        rol.setNombre("ADMIN");
        when(rolRepositorio.findByNombre("ADMIN")).thenReturn(Optional.of(rol));

        Optional<Rol> result = rolServicio.findByNombre("ADMIN");

        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getNombre());
    }

    @Test
    void testFindByNombre_NotFound() {
        when(rolRepositorio.findByNombre("USER")).thenReturn(Optional.empty());

        Optional<Rol> result = rolServicio.findByNombre("USER");

        assertFalse(result.isPresent());
    }

    @Test
    void testExistsByName_True() {
        when(rolRepositorio.existsByNombre("ADMIN")).thenReturn(true);

        assertTrue(rolServicio.existsByName("ADMIN"));
    }

    @Test
    void testExistsByName_False() {
        when(rolRepositorio.existsByNombre("USER")).thenReturn(false);

        assertFalse(rolServicio.existsByName("USER"));
    }

    @Test
    void testExistsById_True() {
        when(rolRepositorio.existsById(1L)).thenReturn(true);

        assertTrue(rolServicio.existsById(1L));
    }

    @Test
    void testExistsById_False() {
        when(rolRepositorio.existsById(99L)).thenReturn(false);

        assertFalse(rolServicio.existsById(99L));
    }

    @Test
    void testSave() {
        Rol rol = new Rol();
        rol.setNombre("MODERATOR");
        when(rolRepositorio.save(rol)).thenReturn(rol);

        Rol savedRol = rolServicio.save(rol);

        assertNotNull(savedRol);
        assertEquals("MODERATOR", savedRol.getNombre());
    }

    @Test
    void testFindAll() {
        Rol rol1 = new Rol(); rol1.setNombre("ADMIN");
        Rol rol2 = new Rol(); rol2.setNombre("USER");
        List<Rol> roles = Arrays.asList(rol1, rol2);
        
        when(rolRepositorio.findAll()).thenReturn(roles);

        List<Rol> result = rolServicio.findAll();

        assertEquals(2, result.size());
        assertEquals("ADMIN", result.get(0).getNombre());
        assertEquals("USER", result.get(1).getNombre());
    }
}
