package app.prueba.puntoencuentro.reserva.precios;

import app.prueba.puntoencuentro.reserva.dominio.Cancha;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface EstrategiaPrecio {
    boolean esCompatible(LocalDateTime inicio);
    BigDecimal calcular(Cancha cancha, LocalDateTime inicio, LocalDateTime fin);
}


