# CineMatch Backend (Spring Boot)

Microservicio principal de CineMatch. Expone la lógica del dominio y la API REST en el puerto `8081`; el frontend React consume esta API directamente.

## Ejecutar

En Windows: `./mvnw.cmd spring-boot:run`.

Puedes comprobarlo con `GET http://127.0.0.1:8081/api/peliculas`.

## Comunicación con pagos

`POST /api/pagos` solicita una transacción al microservicio Django (puerto `8000`). Django procesa la transacción y llama a `POST /api/pagos/webhook`; solo entonces Spring marca la reserva como pagada. Esta comunicación HTTP separa las responsabilidades de reservas y pagos.

Los datos se almacenan en memoria usando repositorios thread-safe y se reinician al apagar la aplicación. Los identificadores son UUID (`String`).

## Alcance migrado

- Catálogo de películas.
- Cines, salas, asientos y funciones.
- Usuarios, registro, inicio de sesión y edición de perfil.
- Reservas, selección/cancelación de asientos y pagos.

Las reglas de validación y los estados provienen de la aplicación Swing original. Este módulo evita los `System.out` y controladores de Swing; los consumidores reciben excepciones de dominio para decidir cómo presentarlas.
