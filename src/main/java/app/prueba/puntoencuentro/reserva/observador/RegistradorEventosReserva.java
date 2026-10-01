package app.prueba.puntoencuentro.reserva.observador;

import app.prueba.puntoencuentro.reserva.dominio.Reserva;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RegistradorEventosReserva implements ObservadorReserva {
    private static final Logger LOG = LoggerFactory.getLogger(RegistradorEventosReserva.class);
    @Override public void alCambiarReserva(Reserva reserva) {
        LOG.info("Reserva {} actualizada: {}", reserva.identificador(), reserva.estado());
    }
}


