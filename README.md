# admin_users_api

Microservicio para la gestión de usuarios, contraseñas y roles.

## 📌 Descripción

`admin_users_api` es un microservicio encargado de manejar el ciclo de vida de los usuarios dentro del ecosistema, incluyendo:

- Registro de nuevos usuarios (turistas, proveedores, administradores).
- Autenticación y validación de credenciales.
- Asignación y gestión de roles.
- Recuperación y actualización de contraseñas.
- Consulta de información básica del usuario.

Forma parte de una arquitectura basada en microservicios orientada al ecosistema de turismo de bienestar.

## 🚀 Tecnologías

- Java 17
- Spring Boot
- Spring Security
- Firebase Authentication / JWT
- Maven
- SQLite
- RabbitMQ

## 📡 Endpoints principales

### 📘 Autenticación y Registro

- `POST /auth/registro` → Registro de nuevo usuario  
- `POST /auth/login` → Autenticación de usuario  
- `POST /auth/recuperar` → Recuperación de contraseña  
- `POST /auth/cambiar-password` → Cambio de contraseña  

### 📘 Gestión de Usuarios

- `GET /user/{id}` → Obtener información del usuario  
- `GET /user/` → Listar todos los usuarios  
- `PUT /user/{id}` → Actualizar usuario  
- `DELETE /user/{id}` → Eliminar usuario  

### 📘 Roles

- `GET /roles` → Obtener lista de roles disponibles  
- `POST /roles/asignar` → Asignar rol a usuario  

## 🛡️ Seguridad

Este microservicio utiliza autenticación basada en tokens JWT (o Firebase Auth) para proteger sus endpoints. Los permisos se gestionan a través de roles como `ROLE_ADMIN`, `ROLE_TURISTA`, y `ROLE_PROVEEDOR`.

## ⚙️ Para su funcionamiento
Este microservicio debe ejecutarse junto con los siguientes componentes del sistema:+
- Frontend [`FrontEnd_IWellness`](https://github.com/IWellnessTesis/FrontEnd_IWellness).
- Microservicio de gestión de servicios [`providers_api`](https://github.com/IWellnessTesis/providers_api).
- Microservicio de gestión de preferencias [`user_preferences_api`](https://github.com/IWellnessTesis/user_preferences_api).

Además, para la publicación y procesamiento de mensajes, deben estar en funcionamiento los siguientes servicios:
- Servidor de mensajería [`IWellness_Data_Services`](https://github.com/IWellnessTesis/IWellness_data_services/tree/main).
- Microservicio de procesamiento de datos [`Queue_Rabbit`](https://github.com/IWellnessTesis/Queue-Rabbit).
- Base de datos de persistencia (MySQL) [`IWellness_DB`](https://github.com/IWellnessTesis/IWellness-DB).


