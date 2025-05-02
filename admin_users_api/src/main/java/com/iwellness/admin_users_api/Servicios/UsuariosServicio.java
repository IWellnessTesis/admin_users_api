package com.iwellness.admin_users_api.Servicios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iwellness.admin_users_api.Clientes.PreferenciaFeignClient;
import com.iwellness.admin_users_api.Clientes.ServicioFeignClient;
import com.iwellness.admin_users_api.DTO.EditarProveedorDTO;
import com.iwellness.admin_users_api.DTO.EditarTuristaDTO;
import com.iwellness.admin_users_api.Entidades.Proveedor;
import com.iwellness.admin_users_api.Entidades.Turista;
import com.iwellness.admin_users_api.Entidades.Usuarios;
import com.iwellness.admin_users_api.Repositorios.ProveedorRepositorio;
import com.iwellness.admin_users_api.Repositorios.TuristaRepositorio;
import com.iwellness.admin_users_api.Repositorios.UsuariosRepositorio;

@Service
public class UsuariosServicio implements CrudService<Usuarios, Long> {

    @Autowired
    private UsuariosRepositorio usuarioRepositorio;
    
    @Autowired
    private TuristaRepositorio turistaRepositorio;
    
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    @Autowired
    private ServicioFeignClient servicioFeignClient;

    @Autowired
    private PreferenciaFeignClient preferenciaFeignClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Métodos existentes...
    
    @Override
    public Usuarios save(Usuarios userEntity) {
        String password = userEntity.getContraseña();
    
        // Codifica solo si la contraseña no está en formato codificado
        if (!isEncoded(password)) {
            String encodedPassword = passwordEncoder.encode(password);
            userEntity.setContraseña(encodedPassword);
        }
    
        return usuarioRepositorio.saveAndFlush(userEntity);
    }

    private boolean isEncoded(String password) {
        // Verifica si la contraseña ya está codificada en Base64 (ejemplo)
        return password.matches("^[A-Za-z0-9+/=]+$") && password.length() >= 44;
    }
    
    public Usuarios actualizarUsuarioTurista(Long id, EditarTuristaDTO dto) {
        Optional<Usuarios> opUsuario = usuarioRepositorio.findById(id);
        if (!opUsuario.isPresent()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        Usuarios usuario = opUsuario.get();

        // Actualiza el nombre del usuario
        usuario.setNombre(dto.getNombre());
        usuario.setFoto(dto.getFoto());

        // Actualiza la información del turista
        Turista turista = usuario.getTurista();
        if (turista == null) {
            throw new RuntimeException("Información del turista no encontrada");
        }
        turista.setTelefono(dto.getTelefono());
        turista.setCiudad(dto.getCiudad());
        turista.setPais(dto.getPais());

        // Se persisten las actualizaciones
        // Debido al cascade en la relación uno a uno, se puede guardar primero el turista
        turistaRepositorio.save(turista);
        usuarioRepositorio.save(usuario);

        return usuario;
    }

    public Usuarios actualizarUsuarioProveedor(Long id, EditarProveedorDTO dto) {
    Optional<Usuarios> opUsuario = usuarioRepositorio.findById(id);
    if (!opUsuario.isPresent()) {
        throw new RuntimeException("Usuario no encontrado");
    }

    Usuarios usuario = opUsuario.get();

    // Actualiza el nombre del usuario
    usuario.setNombre(dto.getNombre());

    usuario.setFoto(dto.getFoto());

    // Obtener y actualizar la información del proveedor
    Proveedor proveedor = usuario.getProveedor();
    if (proveedor == null) {
        throw new RuntimeException("Información del proveedor no encontrada");
    }

    proveedor.setNombre_empresa(dto.getNombre_empresa());
    proveedor.setCoordenadaX(dto.getCoordenadaX());
    proveedor.setCoordenadaY(dto.getCoordenadaY());
    proveedor.setCargoContacto(dto.getCargoContacto());
    proveedor.setTelefono(dto.getTelefono());
    proveedor.setTelefonoEmpresa(dto.getTelefonoEmpresa());

    // Persistir cambios
    proveedorRepositorio.save(proveedor);
    usuarioRepositorio.save(usuario);

    return usuario;
}
    
    public List<Usuarios> findAll() {
        return usuarioRepositorio.findAll();
    }
    
    @Override
    public Usuarios findById(Long id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        // Verificar si el proveedor existe
        Usuarios usuario = usuarioRepositorio.findById(id).orElse(null);

        if(usuario.getRol().getId() == 2){
            // Llamar al micro de Servicios para eliminar los servicios del proveedor
            servicioFeignClient.eliminarServiciosPorProveedor(id);
            usuarioRepositorio.deleteById(id);
        }else if(usuario.getRol().getId() == 1){
            // Llamar al micro de Preferencias para eliminar las preferencias del turista
            preferenciaFeignClient.eliminarPreferenciasPorTurista(id);
            usuarioRepositorio.deleteById(id);
        }

    }

    public boolean existsByNombre(String nombre) {
        return usuarioRepositorio.existsByNombre(nombre);
    }

    public boolean existsByCorreo(String correo) {
        return usuarioRepositorio.existsByCorreo(correo);
    }

    public Optional<Usuarios> findByCorreo(String correo) {
        return usuarioRepositorio.findByCorreo(correo);
    }

    public int countByRoleName(String roleName) {
        return usuarioRepositorio.countByRolName(roleName);
    }
    
    // Nuevo método para obtener todos los usuarios con detalles
    @Transactional(readOnly = true)
    public List<Map<String, Object>> findAllWithDetails() {
        List<Usuarios> usuarios = usuarioRepositorio.findAll();
        List<Map<String, Object>> resultado = new ArrayList<>();
        
        for (Usuarios usuario : usuarios) {
            Map<String, Object> usuarioMap = new HashMap<>();
            usuarioMap.put("id", usuario.getId());
            usuarioMap.put("nombre", usuario.getNombre());
            usuarioMap.put("correo", usuario.getCorreo());
            usuarioMap.put("foto", usuario.getFoto());
            
            if (usuario.getRol() != null) {
                Map<String, Object> rolMap = new HashMap<>();
                rolMap.put("id", usuario.getRol().getId());
                rolMap.put("nombre", usuario.getRol().getNombre());
                usuarioMap.put("rol", rolMap);
                
                // Cargar información específica según el rol
                if ("Turista".equals(usuario.getRol().getNombre())) {
                    Turista turista = turistaRepositorio.findByUsuariosId(usuario.getId());
                    if (turista != null) {
                        Map<String, Object> turistaMap = new HashMap<>();
                        turistaMap.put("id", turista.getId());
                        turistaMap.put("telefono", turista.getTelefono());
                        turistaMap.put("ciudad", turista.getCiudad());
                        turistaMap.put("pais", turista.getPais());
                        turistaMap.put("actividadesInteres", turista.getActividadesInteres());
                        usuarioMap.put("turistaInfo", turistaMap);
                    } else {
                        usuarioMap.put("turistaInfo", null);
                    }
                    usuarioMap.put("proveedorInfo", null);
                } else if ("Proveedor".equals(usuario.getRol().getNombre())) {
                    Proveedor proveedor = proveedorRepositorio.findByUsuariosId(usuario.getId());
                    if (proveedor != null) {
                        Map<String, Object> proveedorMap = new HashMap<>();
                        proveedorMap.put("id", proveedor.getId());
                        proveedorMap.put("nombre_empresa", proveedor.getNombre_empresa());
                        proveedorMap.put("coordenadaX", proveedor.getCoordenadaX());
                        proveedorMap.put("coordenadaY", proveedor.getCoordenadaY());
                        proveedorMap.put("cargoContacto", proveedor.getCargoContacto());
                        proveedorMap.put("telefono", proveedor.getTelefono());
                        proveedorMap.put("telefonoEmpresa", proveedor.getTelefonoEmpresa());
                        usuarioMap.put("proveedorInfo", proveedorMap);
                    } else {
                        usuarioMap.put("proveedorInfo", null);
                    }
                } else {
                    usuarioMap.put("turistaInfo", null);
                    usuarioMap.put("proveedorInfo", null);
                }
            } else {
                usuarioMap.put("rol", null);
            }
            
            resultado.add(usuarioMap);
        }
        
        return resultado;
    }
    
    // Nuevo método para obtener un usuario por ID con todos sus detalles
    @Transactional(readOnly = true)
    public Map<String, Object> findByIdWithDetails(Long id) {
        Usuarios usuario = findById(id);
        if (usuario == null) {
            return null;
        }
        
        Map<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("id", usuario.getId());
        usuarioMap.put("nombre", usuario.getNombre());
        usuarioMap.put("correo", usuario.getCorreo());
        usuarioMap.put("foto", usuario.getFoto());
        
        if (usuario.getRol() != null) {
            Map<String, Object> rolMap = new HashMap<>();
            rolMap.put("id", usuario.getRol().getId());
            rolMap.put("nombre", usuario.getRol().getNombre());
            usuarioMap.put("rol", rolMap);
            
            // Cargar información específica según el rol
            if ("Turista".equals(usuario.getRol().getNombre())) {
                Turista turista = turistaRepositorio.findByUsuariosId(usuario.getId());
                if (turista != null) {
                    Map<String, Object> turistaMap = new HashMap<>();
                    turistaMap.put("id", turista.getId());
                    turistaMap.put("telefono", turista.getTelefono());
                    turistaMap.put("ciudad", turista.getCiudad());
                    turistaMap.put("pais", turista.getPais());
                    turistaMap.put("actividadesInteres", turista.getActividadesInteres());
                    usuarioMap.put("turistaInfo", turistaMap);
                } else {
                    usuarioMap.put("turistaInfo", null);
                }
            } else if ("Proveedor".equals(usuario.getRol().getNombre())) {
                Proveedor proveedor = proveedorRepositorio.findByUsuariosId(usuario.getId());
                if (proveedor != null) {
                    Map<String, Object> proveedorMap = new HashMap<>();
                    proveedorMap.put("id", proveedor.getId());
                    proveedorMap.put("nombre_empresa", proveedor.getNombre_empresa());
                    proveedorMap.put("coordenadaX", proveedor.getCoordenadaX());
                    proveedorMap.put("coordenadaY", proveedor.getCoordenadaY());
                    proveedorMap.put("cargoContacto", proveedor.getCargoContacto());
                    proveedorMap.put("telefono", proveedor.getTelefono());
                    proveedorMap.put("telefonoEmpresa", proveedor.getTelefonoEmpresa());
                    usuarioMap.put("proveedorInfo", proveedorMap);
                } else {
                    usuarioMap.put("proveedorInfo", null);
                }
            } else {
                usuarioMap.put("turistaInfo", null);
                usuarioMap.put("proveedorInfo", null);
            }
        } else {
            usuarioMap.put("rol", null);
        }
        
        return usuarioMap;
    }

    public List<Usuarios> obtenerProveedores() {
        return usuarioRepositorio.getAllProveedores();
    }

    public List<Usuarios> obtenerTuristas() {
        return usuarioRepositorio.getAllTuristas();
    }

}