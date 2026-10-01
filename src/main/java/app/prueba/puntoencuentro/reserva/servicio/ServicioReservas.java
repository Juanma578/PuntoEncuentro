package app.prueba.puntoencuentro.reserva.servicio;

import app.prueba.puntoencuentro.reserva.dominio.Cancha;
import app.prueba.puntoencuentro.reserva.dominio.Reserva;
import app.prueba.puntoencuentro.reserva.observador.ObservadorReserva;
import app.prueba.puntoencuentro.reserva.precios.EstrategiaPrecio;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Service;

@Service
public class ServicioReservas {
    private static final long DURACION_RESERVA_EN_HORAS = 1;
    private final ConcurrentMap<Long, Cancha> canchas = new ConcurrentHashMap<>();
    private final ConcurrentMap<UUID, Reserva> reservas = new ConcurrentHashMap<>();
    private final List<EstrategiaPrecio> estrategiasPrecio;
    private final List<ObservadorReserva> observadores;
    private final ReentrantLock bloqueoReservas = new ReentrantLock();
    private final AtomicLong siguienteCancha = new AtomicLong(2);
    private final ConcurrentMap<UUID, Dueno> duenos = new ConcurrentHashMap<>();
    private final ConcurrentMap<UUID, Jugador> jugadores = new ConcurrentHashMap<>();

    public ServicioReservas(List<EstrategiaPrecio> estrategiasPrecio,
                               List<ObservadorReserva> observadores) {
        this.estrategiasPrecio = estrategiasPrecio;
        this.observadores = observadores;
        canchas.put(1L, new Cancha(1, "Cancha Central", app.prueba.puntoencuentro.reserva.dominio.TipoCancha.FUTBOL_5,
                BigDecimal.valueOf(30000)));
    }

    public Reserva crear(long idCancha, LocalDateTime inicio, UUID idJugador) {
        bloqueoReservas.lock();
        try {
            if (idCancha <= 0 || idJugador == null || inicio == null || !inicio.isAfter(LocalDateTime.now())
                    || inicio.getMinute() != 0 || inicio.getSecond() != 0
                    || inicio.getNano() != 0) {
                throw new IllegalArgumentException("La cancha, el jugador y una fecha futura en punto son obligatorios");
            }
            LocalDateTime fin = inicio.plusHours(DURACION_RESERVA_EN_HORAS);
            Jugador jugador = exigirJugador(idJugador);
            Cancha cancha = exigirCancha(idCancha);
            if (!cancha.estaDisponible(inicio, fin, reservas.values())) {
                throw new IllegalStateException("La cancha no esta disponible en ese horario");
            }
            Reserva reserva = new Reserva(UUID.randomUUID(), idCancha, jugador.nombre(),
                    inicio, fin);
            observadores.forEach(reserva::agregarObservador);
            reservas.put(reserva.identificador(), reserva);
            return reserva;
        } finally {
            bloqueoReservas.unlock();
        }
    }

    public void cancelar(UUID identificador) {
        bloqueoReservas.lock();
        try {
            Reserva reserva = exigirReserva(identificador);
            reserva.cancelar();
        } finally {
            bloqueoReservas.unlock();
        }
    }

    public void aceptar(UUID identificador) {
        bloqueoReservas.lock();
        try {
            exigirReserva(identificador).confirmar();
        } finally {
            bloqueoReservas.unlock();
        }
    }

    public List<Reserva> listar() {
        return List.copyOf(reservas.values());
    }

    public List<Cancha> listarCanchas() {
        return List.copyOf(canchas.values());
    }

    public Cancha registrarCancha(UUID idDueno, String nombre, BigDecimal tarifaPorHora) {
        exigirDueno(idDueno);
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la cancha es obligatorio");
        }
        if (tarifaPorHora == null || tarifaPorHora.signum() <= 0) {
            throw new IllegalArgumentException("La tarifa por hora debe ser mayor que cero");
        }
        long identificador = siguienteCancha.getAndIncrement();
        Cancha cancha = new Cancha(identificador, nombre.trim(),
                app.prueba.puntoencuentro.reserva.dominio.TipoCancha.FUTBOL_5,
                tarifaPorHora);
        canchas.put(identificador, cancha);
        return cancha;
    }

    public Dueno registrarDueno(String nombre, String correo, String contrasena) {
        if (duenos.values().stream().anyMatch(dueno -> dueno.correo().equalsIgnoreCase(correo))) {
            throw new IllegalStateException("Ya existe un dueño registrado con ese correo");
        }
        Dueno dueno = Dueno.crear(nombre, correo, contrasena);
        duenos.put(dueno.identificador(), dueno);
        return dueno;
    }

    public Dueno iniciarSesion(String correo, String contrasena) {
        Dueno.validarInicioSesion(correo, contrasena);
        Optional<Dueno> dueno = duenos.values().stream()
                .filter(actual -> actual.correo().equalsIgnoreCase(correo))
                .findFirst();
        if (dueno.isEmpty() || !dueno.get().contrasenaValida(contrasena)) {
            throw new IllegalArgumentException("Correo o contraseña incorrectos");
        }
        return dueno.get();
    }

    public Jugador registrarJugador(String nombre, String correo, String contrasena) {
        if (jugadores.values().stream().anyMatch(jugador -> jugador.correo().equalsIgnoreCase(correo))) {
            throw new IllegalStateException("Ya existe un jugador registrado con ese correo");
        }
        Jugador jugador = Jugador.crear(nombre, correo, contrasena);
        jugadores.put(jugador.identificador(), jugador);
        return jugador;
    }

    public Jugador iniciarSesionJugador(String correo, String contrasena) {
        Jugador.validarInicioSesion(correo, contrasena);
        return jugadores.values().stream()
                .filter(jugador -> jugador.correo().equalsIgnoreCase(correo))
                .filter(jugador -> jugador.contrasenaValida(contrasena))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Correo o contraseña incorrectos"));
    }

    private Jugador exigirJugador(UUID identificador) {
        Jugador jugador = jugadores.get(identificador);
        if (jugador == null) throw new IllegalArgumentException("Jugador inexistente");
        return jugador;
    }

    private Dueno exigirDueno(UUID idDueno) {
        Dueno dueno = duenos.get(idDueno);
        if (dueno == null) throw new IllegalArgumentException("Dueño inexistente");
        return dueno;
    }

    public BigDecimal precio(Reserva reserva) {
        Cancha cancha = exigirCancha(reserva.idCancha());
        return estrategiasPrecio.stream().filter(estrategia -> estrategia.esCompatible(reserva.inicio()))
                .findFirst().orElseThrow(() -> new IllegalStateException("No existe estrategia de precio"))
                .calcular(cancha, reserva.inicio(), reserva.fin());
    }

    public Reserva obtener(UUID identificador) { return exigirReserva(identificador); }
    private Cancha exigirCancha(long identificador) {
        Cancha cancha = canchas.get(identificador);
        if (cancha == null) throw new IllegalArgumentException("Cancha inexistente: " + identificador);
        return cancha;
    }
    private Reserva exigirReserva(UUID identificador) {
        Reserva reserva = reservas.get(identificador);
        if (reserva == null) throw new IllegalArgumentException("Reserva inexistente: " + identificador);
        return reserva;
    }
}
