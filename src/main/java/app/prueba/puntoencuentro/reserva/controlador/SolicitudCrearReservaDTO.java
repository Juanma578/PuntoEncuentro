package app.prueba.puntoencuentro.reserva.controlador;

import java.time.LocalDateTime;
import java.util.UUID;

public record SolicitudCrearReservaDTO(long idCancha, LocalDateTime inicio, UUID idJugador) {
}
