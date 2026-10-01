package app.prueba.puntoencuentro.reserva.servicio;

import app.prueba.puntoencuentro.reserva.dominio.Reserva;
import java.math.BigDecimal;

public record ResultadoReserva(Reserva reserva, BigDecimal precioTotal) {
}
