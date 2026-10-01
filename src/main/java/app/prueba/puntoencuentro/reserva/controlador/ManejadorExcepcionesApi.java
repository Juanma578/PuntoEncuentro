package app.prueba.puntoencuentro.reserva.controlador;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ManejadorExcepcionesApi {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, Object> notFound(IllegalArgumentException exception) {
        return Map.of("timestamp", Instant.now(), "error", exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    Map<String, Object> conflict(IllegalStateException exception) {
        return Map.of("timestamp", Instant.now(), "error", exception.getMessage());
    }
}

