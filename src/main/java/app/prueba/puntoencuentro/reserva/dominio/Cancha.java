package app.prueba.puntoencuentro.reserva.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Cancha {
    private final long identificador;
    private final String nombre;
    private final TipoCancha tipo;
    private final BigDecimal tarifaPorHora;
    private final UUID idDueno;

    public Cancha(long identificador, String nombre, TipoCancha tipo,
                  BigDecimal tarifaPorHora, UUID idDueno) {
        if (identificador <= 0 || nombre == null || nombre.isBlank() || tipo == null
                || tarifaPorHora == null || tarifaPorHora.signum() < 0) {
            throw new IllegalArgumentException("Los datos de la cancha no son validos");
        }
        this.identificador = identificador;
        this.nombre = nombre;
        this.tipo = tipo;
        this.tarifaPorHora = tarifaPorHora;
        this.idDueno = idDueno;
    }

    public Cancha(long identificador, String nombre, TipoCancha tipo, BigDecimal tarifaPorHora) {
        this(identificador, nombre, tipo, tarifaPorHora, null);
    }

    public boolean estaDisponible(LocalDateTime inicio, LocalDateTime fin,
                               Iterable<Reserva> reservas) {
        Objects.requireNonNull(inicio);
        Objects.requireNonNull(fin);
        if (!inicio.isBefore(fin)) {
            throw new IllegalArgumentException("El inicio debe ser anterior al fin");
        }
        for (Reserva reserva : reservas) {
            if (reserva.perteneceA(identificador) && reserva.seSuperpone(inicio, fin)
                    && reserva.estado() != EstadoReservaValor.CANCELADA) {
                return false;
            }
        }
        return true;
    }

    public long identificador() { return identificador; }
    public String nombre() { return nombre; }
    public TipoCancha tipo() { return tipo; }
    public BigDecimal tarifaPorHora() { return tarifaPorHora; }
    public UUID idDueno() { return idDueno; }
}
