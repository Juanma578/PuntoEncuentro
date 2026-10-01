package app.prueba.puntoencuentro.reserva.precios;

import app.prueba.puntoencuentro.reserva.dominio.Cancha;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public final class EstrategiaPrecioEstandar implements EstrategiaPrecio {
    @Override public boolean esCompatible(LocalDateTime inicio) { return true; }
    @Override
    public BigDecimal calcular(Cancha cancha, LocalDateTime inicio, LocalDateTime fin) {
        long minutos = Duration.between(inicio, fin).toMinutes();
        return cancha.tarifaPorHora().multiply(BigDecimal.valueOf(minutos))
                .divide(BigDecimal.valueOf(60));
    }
}


