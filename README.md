# CineMatch Backend Core

Módulo de dominio extraído del proyecto Swing de CineMatch. No contiene controladores HTTP, DTOs ni base de datos: una futura API REST lo puede consumir como dependencia.

Los datos se almacenan en memoria usando repositorios thread-safe y se pierden al apagar la aplicación. Los identificadores son UUID (`String`).

## Alcance migrado

- Catálogo de películas.
- Cines, salas, asientos y funciones.
- Usuarios, registro, inicio de sesión y edición de perfil.
- Reservas, selección/cancelación de asientos y pagos.

Las reglas de validación y los estados provienen de la aplicación Swing original. Este módulo evita los `System.out` y controladores de Swing; los consumidores reciben excepciones de dominio para decidir cómo presentarlas.
