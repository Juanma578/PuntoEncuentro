package app.prueba.puntoencuentro.reserva.dominio;

import app.prueba.puntoencuentro.reserva.observador.ObservadorReserva;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Reserva {
    private final UUID identificador;
    private final long idCancha;
    private final String nombreCliente;
    private final LocalDateTime inicio;
    private final LocalDateTime fin;
    private final CopyOnWriteArrayList<ObservadorReserva> observadores = new CopyOnWriteArrayList<>();
    private EstadoReserva estado = new EstadoPendiente();

    public Reserva(UUID identificador, long idCancha, String nombreCliente,
                        LocalDateTime inicio, LocalDateTime fin) {
        if (identificador == null || idCancha <= 0 || nombreCliente == null || nombreCliente.isBlank()
                || inicio == null || fin == null || !inicio.isBefore(fin)
                ) {
            throw new IllegalArgumentException("Los datos de la reserva no son validos");
        }
        this.identificador = identificador;
        this.idCancha = idCancha;
        this.nombreCliente = nombreCliente;
        this.inicio = inicio;
        this.fin = fin;
    }

    public boolean seSuperpone(LocalDateTime otroInicio, LocalDateTime otroFin) {
        return inicio.isBefore(otroFin) && otroInicio.isBefore(fin);
    }
    public boolean perteneceA(long identificador) { return idCancha == identificador; }
    public void agregarObservador(ObservadorReserva observador) {
        observadores.add(Objects.requireNonNull(observador));
    }
    public void confirmar() { estado.confirmar(this); }
    public void cancelar() { estado.cancelar(this); }
    void cambiarEstado(EstadoReserva siguiente) {
        estado = Objects.requireNonNull(siguiente);
        observadores.forEach(observador -> observador.alCambiarReserva(this));
    }
    public UUID identificador() { return identificador; }
    public long idCancha() { return idCancha; }
    public String nombreCliente() { return nombreCliente; }
    public LocalDateTime inicio() { return inicio; }
    public LocalDateTime fin() { return fin; }
    public EstadoReservaValor estado() { return estado.estado(); }
}
