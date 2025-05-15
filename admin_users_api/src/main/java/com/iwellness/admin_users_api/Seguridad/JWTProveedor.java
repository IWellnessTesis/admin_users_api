package com.iwellness.admin_users_api.Seguridad;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwellness.admin_users_api.Entidades.Proveedor;
import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Repositorios.ProveedorRepositorio;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;

@Component
public class JWTProveedor {

    @Autowired
private UsuariosRepositorio usuariosRepositorio;

@Autowired
private ProveedorRepositorio proveedorRepositorio;
    
    private static final Logger logger = LoggerFactory.getLogger(JWTProveedor.class);
    private static final String SECRET_KEY = "estaEsUnaClaveSecretaMuySeguraParaFirmarLosTokensJWT";
    public static final Long EXPIRATION_TIME = 7000000L;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String TokenGenerado(Authentication authentication) {
        try {
            String username = authentication.getName();
            logger.info("Generando token para usuario: {}", username);

                // Buscar el usuario por correo (o por nombre, según tu login)
            Optional<Usuarios> usuarioOpt = usuariosRepositorio.findByCorreo(username);
            if (!usuarioOpt.isPresent()) {
                throw new RuntimeException("Usuario no encontrado: " + username);
            }
            Usuarios usuario = usuarioOpt.get();

            // Obtener el proveedor relacionado (si existe)
            Proveedor proveedor = usuario.getProveedor(); // Si tienes la relación directa
            // O, si necesitas buscarlo por el id del usuario:
            // Proveedor proveedor = proveedorRepositorio.findByUsuariosId(usuario.getId());

        Long idProveedor = proveedor != null ? proveedor.getId() : null;
            
            // Generar fechas para el token
            Date currentDate = new Date();
            Date expireDate = new Date(currentDate.getTime() + EXPIRATION_TIME);
            
            // Obtener el rol del usuario
            String rol = authentication.getAuthorities().stream()
                  .findFirst()
                  .map(authority -> authority.getAuthority())
                  .orElse("ROLE_USER");
                  
            logger.info("Rol asignado al token: {}", rol);
            
            // Crear el payload del JWT
            String payload = objectMapper.writeValueAsString(new JwtPayload(
                username,
                rol,
                currentDate.getTime() / 1000,
                expireDate.getTime() / 1000,
                idProveedor
            ));
            
            // Crear el header
            String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            
            // Codificar header y payload en Base64
            String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes(StandardCharsets.UTF_8));
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
            
            // Crear la firma
            String dataToSign = encodedHeader + "." + encodedPayload;
            String signature = hmacSha256(dataToSign, SECRET_KEY);
            
            // Combinar todo para formar el JWT
            return dataToSign + "." + signature;
            
        } catch (Exception e) {
            logger.error("Error al generar el token JWT", e);
            throw new RuntimeException("Error al generar el token", e);
        }
    }
    
    private String hmacSha256(String data, String secret) throws Exception {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKey);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }
    
    public String getUserFromJwt(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }
            
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            JwtPayload jwtPayload = objectMapper.readValue(payload, JwtPayload.class);
            return jwtPayload.getSub();
        } catch (Exception e) {
            return null;
        }
    }
    
    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }
            
            // Verificar firma
            String dataToSign = parts[0] + "." + parts[1];
            String expectedSignature = hmacSha256(dataToSign, SECRET_KEY);
            
            if (!expectedSignature.equals(parts[2])) {
                return false;
            }
            
            // Verificar expiración
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            JwtPayload jwtPayload = objectMapper.readValue(payload, JwtPayload.class);
            
            return jwtPayload.getExp() > (System.currentTimeMillis() / 1000);
        } catch (Exception e) {
            return false;
        }
    }
    
    // Clase interna para manejar el payload del JWT
    private static class JwtPayload {
        private String sub;
        private String rol;
        private long iat;
        private long exp;
        private Long idProveedor; 
        
        public JwtPayload() {}
        
        public JwtPayload(String sub, String rol, long iat, long exp, Long idProveedor) {
            this.sub = sub;
            this.rol = rol;
            this.iat = iat;
            this.exp = exp;
            this.idProveedor = idProveedor;
        }
        
        public String getSub() { return sub; }
        public void setSub(String sub) { this.sub = sub; }
        
        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
        
        public long getIat() { return iat; }
        public void setIat(long iat) { this.iat = iat; }
        
        public long getExp() { return exp; }
        public void setExp(long exp) { this.exp = exp; }

        public Long getIdProveedor() { return idProveedor; }
        public void setIdProveedor(Long idProveedor) { this.idProveedor = idProveedor; }
    }
}