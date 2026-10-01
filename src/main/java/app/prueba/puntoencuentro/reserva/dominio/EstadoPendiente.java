package app.prueba.puntoencuentro.reserva.dominio;

final class EstadoPendiente implements EstadoReserva {
    @Override public EstadoReservaValor estado() { return EstadoReservaValor.PENDIENTE; }

    @Override
    public void confirmar(Reserva reserva) {
        reserva.cambiarEstado(new EstadoConfirmado());
    }

    @Override
    public void cancelar(Reserva reserva) {
        reserva.cambiarEstado(new EstadoCancelado());
    }
}


