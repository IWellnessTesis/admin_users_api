package com.iwellness.admin_users_api.Init;

import com.iwellness.admin_users_api.Entidades.Rol;
import com.iwellness.admin_users_api.Repositorios.RolRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DatabaseInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Bean
    CommandLineRunner initDatabase(RolRepositorio rolRepositorio) {
        return args -> {
            if (rolRepositorio.count() == 0) { // Solo inicializar si está vacío
                logger.info("Inicializando la base de datos con roles por defecto...");

                Rol turista = new Rol();
                turista.setNombre("Turista");

                Rol proveedor = new Rol();
                proveedor.setNombre("Proveedor");

                rolRepositorio.save(turista);
                rolRepositorio.save(proveedor);

                logger.info("Roles creados: Turista y Proveedor.");
            } else {
                logger.info("La base de datos ya contiene roles, omitiendo inicializacion.");
            }
        };
    }
}
