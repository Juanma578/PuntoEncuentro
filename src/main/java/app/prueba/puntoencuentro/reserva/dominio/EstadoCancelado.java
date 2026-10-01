package app.prueba.puntoencuentro.reserva.dominio;

final class EstadoCancelado implements EstadoReserva {
    @Override public EstadoReservaValor estado() { return EstadoReservaValor.CANCELADA; }
    @Override public void confirmar(Reserva reserva) {
        throw new IllegalStateException("No se puede confirmar una reserva cancelada");
    }
    @Override public void cancelar(Reserva reserva) {
        throw new IllegalStateException("La reserva ya esta cancelada");
    }
}


