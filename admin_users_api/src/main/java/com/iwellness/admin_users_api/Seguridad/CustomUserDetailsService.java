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
import org.springframework.stereotype.Service;

import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Entidades.Rol;

import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuariosRepositorio userRepository;

    @Autowired
    private JWTProveedor jwtGenerator;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuarios user = userRepository.findByCorreo(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));
        return new User(user.getCorreo(), user.getContraseña(), mapRolesToAuthorities(List.of(user.getRol())));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Rol> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
    }

    public String getUserRoleFromToken(String token) {
        if (jwtGenerator.validateToken(token)) {
            // Extraer el nombre de usuario del token
            String username = jwtGenerator.getUserFromJwt(token);
            
            // Cargar los detalles del usuario desde la base de datos
            UserDetails userDetails = loadUserByUsername(username);

            // Obtener el rol del usuario
            return userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority())
                    .orElse("ROLE_USER"); // Retornar un valor predeterminado si no se encuentra ningún rol
        }
        throw new IllegalArgumentException("Invalid JWT token");
    }

    public String getUserFromToken(String token) {
        if (jwtGenerator.validateToken(token)) {
            // Extraer el nombre de usuario del token
            String username = jwtGenerator.getUserFromJwt(token);
            
            return username;
        }
        throw new IllegalArgumentException("Invalid JWT token");
    }
}