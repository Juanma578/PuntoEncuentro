package app.prueba.puntoencuentro.reserva.controlador;

import app.prueba.puntoencuentro.reserva.servicio.Dueno;
import app.prueba.puntoencuentro.reserva.servicio.FachadaReservas;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@RequestMapping("/api/duenos")
public class ControladorDuenos {
    private final FachadaReservas fachada;

    public ControladorDuenos(FachadaReservas fachada) {
        this.fachada = fachada;
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public RespuestaDuenoDTO registrar(@RequestBody SolicitudRegistroDuenoDTO solicitud) {
        return respuesta(fachada.registrarDueno(solicitud.nombre(), solicitud.correo(),
                solicitud.contrasena()));
    }

    @PostMapping("/inicio")
    public RespuestaDuenoDTO iniciar(@RequestBody SolicitudInicioDuenoDTO solicitud) {
        return respuesta(fachada.iniciarSesion(solicitud.correo(), solicitud.contrasena()));
    }

    @PostMapping("/{idDueno}/canchas")
    @ResponseStatus(HttpStatus.CREATED)
    public RespuestaCanchaDTO registrarCancha(@PathVariable java.util.UUID idDueno,
                                           @RequestBody SolicitudRegistrarCanchaDTO solicitud) {
        var cancha = fachada.registrarCancha(idDueno, solicitud.nombre(), solicitud.tarifaPorHora());
        return new RespuestaCanchaDTO(cancha.identificador(), cancha.nombre(), cancha.tarifaPorHora());
    }

    private RespuestaDuenoDTO respuesta(Dueno dueno) {
        return new RespuestaDuenoDTO(dueno.identificador(), dueno.nombre(), dueno.correo());
    }
}

