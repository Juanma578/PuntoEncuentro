package app.prueba.puntoencuentro.reserva.servicio;

import app.prueba.puntoencuentro.reserva.dominio.Reserva;
import app.prueba.puntoencuentro.reserva.dominio.Cancha;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class FachadaReservas {
    private final ServicioReservas servicio;

    public FachadaReservas(ServicioReservas servicio) {
        this.servicio = servicio;
    }

    public ResultadoReserva crear(long idCancha, LocalDateTime inicio, UUID idJugador) {
        Reserva reserva = servicio.crear(idCancha, inicio, idJugador);
        return new ResultadoReserva(reserva, servicio.precio(reserva));
    }

    public ResultadoReserva obtener(UUID identificador) {
        Reserva reserva = servicio.obtener(identificador);
        return new ResultadoReserva(reserva, servicio.precio(reserva));
    }

    public void cancelar(UUID identificador, UUID idDueno) {
        servicio.cancelar(identificador, idDueno);
    }

    public void cancelar(UUID identificador) {
        servicio.cancelar(identificador);
    }

    public void aceptar(UUID identificador, UUID idDueno) {
        servicio.aceptar(identificador, idDueno);
    }

    public java.util.List<ResultadoReserva> listar() {
        return servicio.listar().stream()
                .map(reserva -> new ResultadoReserva(reserva, servicio.precio(reserva)))
                .toList();
    }

    public java.util.List<ResultadoReserva> listar(UUID idDueno) {
        return servicio.listar(idDueno).stream()
                .map(reserva -> new ResultadoReserva(reserva, servicio.precio(reserva)))
                .toList();
    }

    public java.util.List<Cancha> listarCanchas() {
        return servicio.listarCanchas();
    }

    public java.util.List<Cancha> listarCanchas(UUID idDueno) {
        return servicio.listarCanchas(idDueno);
    }

    public Cancha registrarCancha(UUID idDueno, String nombre, BigDecimal tarifaPorHora) {
        return servicio.registrarCancha(idDueno, nombre, tarifaPorHora);
    }

    public Dueno registrarDueno(String nombre, String correo, String contrasena) {
        return servicio.registrarDueno(nombre, correo, contrasena);
    }

    public Dueno iniciarSesion(String correo, String contrasena) {
        return servicio.iniciarSesion(correo, contrasena);
    }

    public Jugador registrarJugador(String nombre, String correo, String contrasena) {
        return servicio.registrarJugador(nombre, correo, contrasena);
    }

    public Jugador iniciarSesionJugador(String correo, String contrasena) {
        return servicio.iniciarSesionJugador(correo, contrasena);
    }
}
