package app.prueba.puntoencuentro.reserva.controlador;

import app.prueba.puntoencuentro.reserva.servicio.FachadaReservas;
import app.prueba.puntoencuentro.reserva.servicio.Jugador;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@RequestMapping("/api/jugadores")
public class ControladorJugadores {
    private final FachadaReservas fachada;

    public ControladorJugadores(FachadaReservas fachada) {
        this.fachada = fachada;
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public RespuestaJugadorDTO registrar(@RequestBody SolicitudRegistroJugadorDTO solicitud) {
        return respuesta(fachada.registrarJugador(solicitud.nombre(), solicitud.correo(),
                solicitud.contrasena()));
    }

    @PostMapping("/inicio")
    public RespuestaJugadorDTO iniciar(@RequestBody SolicitudInicioJugadorDTO solicitud) {
        return respuesta(fachada.iniciarSesionJugador(solicitud.correo(), solicitud.contrasena()));
    }

    private RespuestaJugadorDTO respuesta(Jugador jugador) {
        return new RespuestaJugadorDTO(jugador.identificador(), jugador.nombre(), jugador.correo());
    }
}

