package app.prueba.puntoencuentro.reserva.dominio;

public enum EstadoReservaValor {
    PENDIENTE,
    CONFIRMADA,
    CANCELADA;

    public String nombre() {
        return name();
    }
}

