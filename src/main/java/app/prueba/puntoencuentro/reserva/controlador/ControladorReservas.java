package app.prueba.puntoencuentro.reserva.controlador;

import app.prueba.puntoencuentro.reserva.servicio.FachadaReservas;
import app.prueba.puntoencuentro.reserva.servicio.ResultadoReserva;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@RequestMapping("/api/reservas")
public class ControladorReservas {
    private final FachadaReservas fachada;

    public ControladorReservas(FachadaReservas fachada) { this.fachada = fachada; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RespuestaReservaDTO crear(@RequestBody SolicitudCrearReservaDTO solicitud) {
        ResultadoReserva resultado = fachada.crear(solicitud.idCancha(), solicitud.inicio(),
                solicitud.idJugador());
        return RespuestaReservaDTO.desde(resultado.reserva(), resultado.precioTotal());
    }

    @GetMapping("/{identificador}")
    public RespuestaReservaDTO obtener(@PathVariable UUID identificador) {
        ResultadoReserva resultado = fachada.obtener(identificador);
        return RespuestaReservaDTO.desde(resultado.reserva(), resultado.precioTotal());
    }

    @DeleteMapping("/{identificador}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable UUID identificador) { fachada.cancelar(identificador); }

    @GetMapping
    public java.util.List<RespuestaReservaDTO> listar() {
        return fachada.listar().stream()
                .map(resultado -> RespuestaReservaDTO.desde(resultado.reserva(), resultado.precioTotal()))
                .toList();
    }

    @GetMapping("/canchas")
    public java.util.List<RespuestaCanchaDTO> listarCanchas() {
        return fachada.listarCanchas().stream()
                .map(cancha -> new RespuestaCanchaDTO(cancha.identificador(), cancha.nombre(), cancha.tarifaPorHora()))
                .toList();
    }

    @PatchMapping("/{identificador}/aceptar")
    public RespuestaReservaDTO aceptar(@PathVariable UUID identificador) {
        fachada.aceptar(identificador);
        var resultado = fachada.obtener(identificador);
        return RespuestaReservaDTO.desde(resultado.reserva(), resultado.precioTotal());
    }

}

