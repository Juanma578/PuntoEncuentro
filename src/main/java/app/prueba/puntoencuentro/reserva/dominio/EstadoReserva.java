package app.prueba.puntoencuentro.reserva.dominio;

public interface EstadoReserva {
    EstadoReservaValor estado();
    void confirmar(Reserva reserva);
    void cancelar(Reserva reserva);
}


