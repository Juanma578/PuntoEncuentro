package app.prueba.puntoencuentro.reserva.dominio;

final class EstadoConfirmado implements EstadoReserva {
    @Override public EstadoReservaValor estado() { return EstadoReservaValor.CONFIRMADA; }

    @Override
    public void confirmar(Reserva reserva) {
        throw new IllegalStateException("La reserva ya esta confirmada");
    }

    @Override
    public void cancelar(Reserva reserva) {
        reserva.cambiarEstado(new EstadoCancelado());
    }
}


