package com.iwellness.admin_users_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@EnableFeignClients
@SpringBootApplication
public class AdminUsersApiApplication implements ApplicationListener<ContextRefreshedEvent> {

    public static void main(String[] args) {
        SpringApplication.run(AdminUsersApiApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext() instanceof ServletWebServerApplicationContext) {
            ServletWebServerApplicationContext context = (ServletWebServerApplicationContext) event.getApplicationContext();
            int port = context.getWebServer().getPort();
            System.out.println("==================================================");
            System.out.println("La aplicación está ejecutándose en el puerto: " + port);
            System.out.println("==================================================");
        }
    }
}