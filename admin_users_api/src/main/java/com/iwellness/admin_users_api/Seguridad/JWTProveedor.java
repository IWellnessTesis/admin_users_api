package com.iwellness.admin_users_api.Seguridad;

import com.iwellness.admin_users_api.Entidades.Usuarios;
import java.security.Key;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTProveedor {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public static final Long EXPIRATION_TIME = 7000000L;

    public String TokenGenerado(Authentication authentication) {

        String username = ((Usuarios) authentication.getPrincipal()).getCorreo();  // Usamos el correo como nombre de usuario
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .claim("rol", ((Usuarios) authentication.getPrincipal()).getRol().getNombre())  // Asignamos el rol del usuario
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUserFromJwt(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
