# CineMatch Backend (Spring Boot)

Microservicio de negocio de CineMatch. Expone la lógica del dominio mediante una API REST en el puerto `8081`; la API Gateway de Django consume estos endpoints por HTTP.

## Ejecutar

En Windows: `./mvnw.cmd spring-boot:run`.

Puedes comprobarlo directamente con `GET http://127.0.0.1:8081/api/peliculas` o a través de la API Django en `http://127.0.0.1:8000/api/peliculas`.

Los datos se almacenan en memoria usando repositorios thread-safe y se reinician al apagar la aplicación. Los identificadores son UUID (`String`).

## Alcance migrado

- Catálogo de películas.
- Cines, salas, asientos y funciones.
- Usuarios, registro, inicio de sesión y edición de perfil.
- Reservas, selección/cancelación de asientos y pagos.

Las reglas de validación y los estados provienen de la aplicación Swing original. Este módulo evita los `System.out` y controladores de Swing; los consumidores reciben excepciones de dominio para decidir cómo presentarlas.
