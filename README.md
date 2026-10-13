# CineMatch Backend

### Backend principal de la plataforma CineMatch

CineMatch es una aplicación web que permite consultar películas, explorar cines y funciones disponibles, comparar precios y horarios, y gestionar reservas y pagos.

Este repositorio contiene el **backend principal desarrollado en Java con Spring Boot**, responsable de exponer la API REST, gestionar las reglas de negocio y coordinar la comunicación con el microservicio de pagos desarrollado en Django.

## Arquitectura del sistema

El sistema está dividido en tres componentes independientes:

| Componente             | Tecnología         | Puerto | Responsabilidad                                                         |
| ---------------------- | ------------------ | ------ | ----------------------------------------------------------------------- |
| Frontend               | React + Vite       | `5173` | Interfaz de usuario y panel administrativo.                             |
| Backend principal      | Java + Spring Boot | `8081` | API REST, autenticación, películas, cines, funciones, reservas y pagos. |
| Microservicio de pagos | Python + Django    | `8000` | Procesamiento de pagos y notificación de resultados mediante webhook.   |

## Características principales

* **Gestión de películas:** creación, consulta, actualización y eliminación de películas.
* **Gestión de cines:** administración de cines y sus salas.
* **Gestión de funciones:** programación y administración de funciones de cine.
* **Comparación de funciones:** consulta de horarios y precios para una misma película.
* **Gestión de reservas:** creación y cancelación de reservas, consulta de asientos y liberación de asientos al cancelar.
* **Autenticación:** registro e inicio de sesión para clientes y acceso administrativo.
* **Gestión de perfiles:** actualización de la información de clientes y administradores.
* **Integración de pagos:** comunicación HTTP con el microservicio Django y recepción de confirmaciones mediante webhook.
* **Validaciones de negocio:** control de duplicados, disponibilidad de asientos, conflictos de horarios y montos de pago.

## Tecnologías utilizadas

* Java
* Spring Boot
* Maven
* API REST
* HTTP y JSON
* React y Vite para el frontend integrado
* Django para el microservicio externo de pagos

## Arquitectura interna

El backend sigue una arquitectura en capas para separar las responsabilidades de la aplicación.

* **Controladores REST:** reciben solicitudes HTTP y exponen los endpoints.
* **Servicios:** implementan las reglas de negocio y validaciones.
* **Repositorios:** administran el acceso a los datos.
* **DTO y Mapper:** permiten transferir y adaptar información entre las capas de la aplicación.
* **Manejo centralizado de errores:** transforma excepciones en respuestas HTTP consistentes.

### Patrones de diseño

* **Repository Pattern:** desacopla la lógica de negocio del almacenamiento. La implementación actual utiliza repositorios en memoria.
* **DTO y Mapper:** separan los modelos internos de los datos expuestos por la API.
* **Arquitectura de microservicios:** permite que Spring Boot y Django funcionen como aplicaciones independientes comunicadas mediante HTTP.

## Endpoints de la API REST

La API principal está disponible localmente en `http://localhost:8081`.

### Películas

| Método   | Endpoint                         | Descripción                                  |
| -------- | -------------------------------- | -------------------------------------------- |
| `GET`    | `/api/peliculas`                 | Lista las películas.                         |
| `GET`    | `/api/peliculas/{id}`            | Consulta una película por su identificador.  |
| `POST`   | `/api/peliculas`                 | Crea una película.                           |
| `PUT`    | `/api/peliculas/{id}`            | Actualiza una película.                      |
| `DELETE` | `/api/peliculas/{id}`            | Elimina una película.                        |
| `GET`    | `/api/peliculas/{id}/funciones`  | Lista las funciones de una película.         |
| `GET`    | `/api/peliculas/{id}/comparador` | Compara precios y horarios de sus funciones. |

### Cines y salas

| Método   | Endpoint          | Descripción                                       |
| -------- | ----------------- | ------------------------------------------------- |
| `GET`    | `/api/cines`      | Lista los cines y sus salas.                      |
| `GET`    | `/api/cines/{id}` | Consulta un cine.                                 |
| `POST`   | `/api/cines`      | Crea un cine con sus salas.                       |
| `PUT`    | `/api/cines/{id}` | Actualiza un cine y sus salas.                    |
| `DELETE` | `/api/cines/{id}` | Elimina un cine, sus salas y funciones asociadas. |

### Funciones

| Método   | Endpoint              | Descripción                      |
| -------- | --------------------- | -------------------------------- |
| `GET`    | `/api/funciones`      | Lista las funciones disponibles. |
| `GET`    | `/api/funciones/{id}` | Consulta una función.            |
| `POST`   | `/api/funciones`      | Crea una función.                |
| `PUT`    | `/api/funciones/{id}` | Actualiza una función.           |
| `DELETE` | `/api/funciones/{id}` | Cancela una función.             |

### Reservas y asientos

| Método   | Endpoint                          | Descripción                                |
| -------- | --------------------------------- | ------------------------------------------ |
| `GET`    | `/api/funciones/{id}/asientos`    | Consulta los asientos de una función.      |
| `POST`   | `/api/reservas`                   | Crea una reserva pendiente.                |
| `GET`    | `/api/clientes/{correo}/reservas` | Lista las reservas de un cliente.          |
| `DELETE` | `/api/reservas/{id}`              | Cancela una reserva y libera sus asientos. |

### Autenticación y perfiles

| Método | Endpoint                 | Descripción                         |
| ------ | ------------------------ | ----------------------------------- |
| `POST` | `/api/clientes/registro` | Registra un cliente.                |
| `POST` | `/api/auth/login`        | Inicia sesión administrativa.       |
| `POST` | `/api/clientes/login`    | Inicia sesión de cliente.           |
| `PUT`  | `/api/auth/perfil`       | Actualiza el perfil administrativo. |
| `PUT`  | `/api/clientes/perfil`   | Actualiza el perfil del cliente.    |

### Pagos

| Método | Endpoint             | Descripción                                                   |
| ------ | -------------------- | ------------------------------------------------------------- |
| `GET`  | `/api/pagos`         | Lista los pagos confirmados en Spring Boot.                   |
| `GET`  | `/api/pagos/{id}`    | Consulta un pago por su identificador.                        |
| `POST` | `/api/pagos`         | Solicita el procesamiento de un pago al microservicio Django. |
| `POST` | `/api/pagos/webhook` | Recibe la confirmación del resultado enviada por Django.      |

**Nota:** estos endpoints corresponden al contrato documentado del proyecto. Los formatos de solicitud, las respuestas y los códigos HTTP deben consultarse en la implementación de cada controlador.

## Reglas de negocio y validaciones

El backend aplica validaciones para mantener la consistencia de las operaciones:

* Los correos de los usuarios deben ser únicos, sin distinguir mayúsculas y minúsculas.
* No se permiten películas con títulos duplicados, ignorando mayúsculas y espacios externos.
* No se permiten funciones que se superpongan en la misma sala y fecha.
* Una función puede comenzar exactamente cuando termina otra.
* No se permiten cines con el mismo nombre en una misma ciudad.
* No se permiten salas duplicadas dentro de un mismo cine.
* No se puede reservar un asiento que ya esté ocupado.
* El monto de un pago debe coincidir con el total calculado para la reserva.
* Los clientes no deben acceder a las funcionalidades administrativas mediante las rutas destinadas a administradores.

## Flujo de procesamiento de pagos

El backend principal coordina el proceso de pago con el microservicio Django.

1. El cliente inicia el proceso de pago desde el frontend.
2. Spring Boot consulta el total correspondiente a la reserva.
3. Spring Boot envía una solicitud HTTP al microservicio Django.
4. Django procesa y registra la transacción.
5. Django notifica el resultado a Spring Boot mediante el endpoint `/api/pagos/webhook`.
6. Spring Boot actualiza el estado de la reserva a `PAGADA` únicamente cuando recibe la confirmación correspondiente.

Esta separación permite mantener la lógica principal de CineMatch independiente del procesamiento de pagos.

## Requisitos previos

Antes de ejecutar el backend, instala:

* Java Development Kit (JDK) compatible con la versión configurada en el proyecto.
* Git.
* Maven, si no se utiliza el Maven Wrapper incluido en el repositorio.

Para comprobar la instalación de Java:

```bash
java -version
```

Para comprobar Git:

```bash
git --version
```

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/Fremithor/cinematch-backend.git
cd cinematch-backend
```

### 2. Ejecutar el backend

En Windows, desde PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Si el repositorio no incluye Maven Wrapper, utiliza Maven instalado localmente:

```bash
mvn spring-boot:run
```

### 3. Verificar el servicio

Una vez iniciado, el backend estará disponible en:

`http://localhost:8081`

Puedes probar los endpoints con Postman, Insomnia o cualquier cliente HTTP.

### 4. Ejecutar el sistema completo

Para probar todas las funcionalidades, inicia los componentes en este orden:

**Backend Spring Boot**

```powershell
.\mvnw.cmd spring-boot:run
```

## Almacenamiento de datos

En la versión actual, los repositorios utilizan almacenamiento en memoria. Esto permite ejecutar y demostrar las funcionalidades sin configurar una base de datos externa.

Por este motivo, los datos almacenados pueden perderse cuando se reinicia la aplicación. El patrón Repository facilita una futura migración a una solución de persistencia permanente.

## Manejo de errores y disponibilidad

Spring Boot centraliza el tratamiento de excepciones para proporcionar respuestas HTTP consistentes.

Si el microservicio Django no está disponible, las operaciones de pago pueden fallar y el sistema debe informar que el servicio de pagos no está disponible. Las demás funcionalidades deben continuar operando de forma independiente cuando no dependan del procesamiento de pagos.

## Repositorios relacionados

* **Backend principal:** [cinematch-backend](https://github.com/Fremithor/cinematch-backend)
* **Microservicio de pagos:** [Cines2](https://github.com/mrkzukaritas/Cines2)

## Estado del proyecto

Proyecto académico de desarrollo web orientado a la comparación de funciones de cine, la gestión de reservas y la integración de un microservicio independiente de pagos.
