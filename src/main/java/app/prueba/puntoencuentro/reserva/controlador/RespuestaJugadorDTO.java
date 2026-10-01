package app.prueba.puntoencuentro.reserva.controlador;

import java.util.UUID;

public record RespuestaJugadorDTO(UUID identificador, String nombre, String correo) {
}

