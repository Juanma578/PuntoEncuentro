package app.prueba.puntoencuentro.reserva.controlador;

import app.prueba.puntoencuentro.reserva.dominio.Reserva;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record RespuestaReservaDTO(UUID identificador, long idCancha, String nombreCliente,
                                  LocalDateTime inicio, LocalDateTime fin,
                                  String estado, BigDecimal precioTotal) {
    public static RespuestaReservaDTO desde(Reserva reserva, BigDecimal total) {
        return new RespuestaReservaDTO(reserva.identificador(), reserva.idCancha(),
                reserva.nombreCliente(), reserva.inicio(), reserva.fin(),
                reserva.estado().nombre(), total);
    }
}

