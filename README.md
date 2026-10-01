# Punto Encuentro

Punto Encuentro es una plataforma web para reservar y administrar canchas deportivas. Los jugadores pueden consultar canchas, ver sus precios, elegir un horario y crear reservas de una hora. Los dueños pueden registrar sus canchas, definir la tarifa por hora y administrar las solicitudes de reserva.

## Funcionalidades

- Registro e inicio de sesión de jugadores.
- Registro e inicio de sesión de dueños.
- Cierre de sesión del jugador.
- Registro de canchas con precio personalizado por hora.
- Consulta de canchas y precios disponibles.
- Reservas con duración fija de una hora.
- Horarios de inicio exactos: `HH:00`.
- Prevención de reservas superpuestas para la misma cancha.
- Estados de reserva: pendiente, confirmada y cancelada.
- Panel del dueño para aceptar o cancelar reservas.
- Cálculo del precio mediante estrategias de precios.
- Manejo global de errores de la API.

## Tecnologías

### Backend

- Java 21.
- Spring Boot.
- Spring Web MVC.
- Maven.
- Almacenamiento temporal en memoria.

### Frontend

- React 18.
- Vite.
- JavaScript.
- CSS responsive.

## Arquitectura

La aplicación utiliza una arquitectura en capas:

```text
Frontend React
      ↓
Controladores REST
      ↓
DTOs de solicitud y respuesta
      ↓
FachadaReservas
      ↓
ServicioReservas
      ↓
Dominio y patrones de diseño
```

Los controladores reciben los DTOs enviados por el frontend y devuelven DTOs de respuesta. La fachada es el punto de entrada de los casos de uso y coordina el acceso a los servicios. Los servicios ejecutan la lógica de aplicación y utilizan las clases del dominio.

## Patrones de diseño

### Fachada

`FachadaReservas` centraliza el acceso a las operaciones del sistema. Los controladores pasan por la fachada en lugar de comunicarse directamente con todas las clases internas.

### Experto

Cada clase contiene las responsabilidades relacionadas con la información que conoce. Por ejemplo, `Cancha` verifica su disponibilidad, `Reserva` administra sus cambios de estado y `ServicioReservas` coordina el proceso de creación.

### Strategy

Las estrategias de precio permiten calcular el costo de una reserva de distintas maneras, por ejemplo mediante precio estándar o precio de horario pico. La tarifa base pertenece a cada cancha y la define su dueño.

### Observer

Las reservas notifican a sus observadores cada vez que cambia su estado. Esto permite reaccionar a eventos como confirmaciones o cancelaciones sin acoplar la reserva a una implementación concreta.

### State

La reserva delega su comportamiento al estado actual. Los estados principales son `PENDIENTE`, `CONFIRMADA` y `CANCELADA`, y cada uno define las transiciones permitidas.

## Estructura principal

```text
src/main/java/app/prueba/puntoencuentro/reserva/
├── controlador/
│   ├── ControladorDuenos.java
│   ├── ControladorJugadores.java
│   ├── ControladorReservas.java
│   ├── *DTO.java
│   └── ManejadorExcepcionesApi.java
├── dominio/
│   ├── Cancha.java
│   ├── Reserva.java
│   └── estados de reserva
├── observador/
├── precios/
└── servicio/
    ├── FachadaReservas.java
    └── ServicioReservas.java

frontend/
└── src/
    ├── componentes/
    ├── servicios/
    └── estilos.css
```

## Requisitos

- JDK 21 o superior.
- Maven Wrapper incluido en el proyecto.
- Node.js y npm.

## Almacenamiento

Esta versión utiliza almacenamiento en memoria mediante mapas concurrentes. Los datos de jugadores, dueños, canchas y reservas se pierden cuando se reinicia el backend.

Como siguiente evolución, el almacenamiento puede reemplazarse por PostgreSQL o MySQL y la autenticación puede migrarse a sesiones o tokens JWT.
