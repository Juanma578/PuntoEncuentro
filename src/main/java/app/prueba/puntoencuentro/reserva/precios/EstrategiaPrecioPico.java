package app.prueba.puntoencuentro.reserva.precios;

import app.prueba.puntoencuentro.reserva.dominio.Cancha;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class EstrategiaPrecioPico implements EstrategiaPrecio {
    @Override public boolean esCompatible(LocalDateTime inicio) {
        return !inicio.toLocalTime().isBefore(java.time.LocalTime.of(18, 0));
    }
    @Override
    public BigDecimal calcular(Cancha cancha, LocalDateTime inicio, LocalDateTime fin) {
        return new EstrategiaPrecioEstandar().calcular(cancha, inicio, fin)
                .multiply(BigDecimal.valueOf(1.25));
    }
}


