package com.iwellness.admin_users_api.Seguridad;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Entidades.Proveedor;
import com.iwellness.admin_users_api.Entidades.Rol;
import com.iwellness.admin_users_api.Entidades.Turista;
import com.iwellness.admin_users_api.Repositorios.RolRepositorio;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuariosRepositorio usuariosRepositorio;

    @Autowired
    private RolRepositorio rolRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método para cargar un usuario por su nombre de usuario (en este caso, el correo)
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuarios usuario = usuariosRepositorio.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + correo));

        // Mapear el usuario de la base de datos a UserDetails de Spring Security
        return new User(
                usuario.getCorreo(),
                usuario.getContraseña(),
                mapRolesToAuthorities(Collections.singletonList(usuario.getRol()))
        );
    }

    // Método para convertir roles de la base de datos a GrantedAuthority
    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Rol> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getNombre()))
                .collect(Collectors.toList());
    }

    // Método para convertir un Turista a un Usuario
    public Usuarios turistaToUsuario(Turista turista) {
        Usuarios usuario = new Usuarios();
        usuario.setCorreo(turista.getUsuarios().getCorreo());
        usuario.setContraseña(passwordEncoder.encode(turista.getUsuarios().getContraseña()));

        // Asignar el rol de TURISTA
        Rol rolTurista = rolRepositorio.findByNombre("TURISTA")
                .orElseThrow(() -> new RuntimeException("Rol TURISTA no encontrado"));
        usuario.setRol(rolTurista);

        return usuario;
    }

    // Método para convertir un Proveedor a un Usuario
    public Usuarios proveedorToUsuario(Proveedor proveedor) {
        Usuarios usuario = new Usuarios();
        usuario.setCorreo(proveedor.getUsuarios().getCorreo());
        usuario.setContraseña(passwordEncoder.encode(proveedor.getUsuarios().getContraseña()));

        // Asignar el rol de PROVEEDOR
        Rol rolProveedor = rolRepositorio.findByNombre("PROVEEDOR")
                .orElseThrow(() -> new RuntimeException("Rol PROVEEDOR no encontrado"));
        usuario.setRol(rolProveedor);

        return usuario;
    }
}