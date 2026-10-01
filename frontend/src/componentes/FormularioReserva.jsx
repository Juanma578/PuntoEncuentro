import { useEffect, useState } from "react";
import { listarCanchas } from "../servicios/reservas.js";

const valoresIniciales = {
  idCancha: "",
  fecha: "",
  hora: ""
};

function fechaActual() {
  const fecha = new Date();
  fecha.setMinutes(fecha.getMinutes() - fecha.getTimezoneOffset());
  return fecha.toISOString().slice(0, 10);
}

export default function FormularioReserva({ alCrear, cargando, idJugador }) {
  const [valores, establecerValores] = useState(valoresIniciales);
  const [canchas, establecerCanchas] = useState([]);
  const [error, establecerError] = useState("");
  const canchaSeleccionada = canchas.find(
    (cancha) => String(cancha.identificador) === String(valores.idCancha)
  );
  const horasDisponibles = Array.from({ length: 24 }, (_, hora) =>
    `${String(hora).padStart(2, "0")}:00`
  );

  useEffect(() => {
    listarCanchas()
      .then((resultado) => {
        establecerCanchas(resultado);
        if (resultado.length > 0) {
          establecerValores((actuales) => ({ ...actuales, idCancha: resultado[0].identificador }));
        }
      })
      .catch((exception) => establecerError(exception.message));
  }, []);

  function cambiar(evento) {
    const { name, value } = evento.target;
    establecerValores((actuales) => ({ ...actuales, [name]: value }));
  }

  function enviar(evento) {
    evento.preventDefault();
    const inicio = `${valores.fecha}T${valores.hora}`;
    alCrear({
      idCancha: Number(valores.idCancha),
      inicio,
      idJugador,
    });
  }

  return (
    <form className="formulario-reserva" onSubmit={enviar}>
      <div className="encabezado-seccion">
        <span className="etiqueta-seccion">Nueva reserva</span>
        <h2>Arma tu próximo partido</h2>
        <p>Elegí la cancha, el horario y dividí el costo con tu equipo.</p>
      </div>

      <label>
        Cancha
        <select name="idCancha" value={valores.idCancha} onChange={cambiar}>
          <option value="">Elegí una cancha</option>
          {canchas.map((cancha) => (
            <option key={cancha.identificador} value={cancha.identificador}>
              {cancha.nombre} — ${Number(cancha.tarifaPorHora).toLocaleString("es-AR")} por hora
            </option>
          ))}
        </select>
      </label>
      {canchaSeleccionada && (
        <p className="ayuda-horario">
          Precio de la cancha:{" "}
          <strong>
            ${Number(canchaSeleccionada.tarifaPorHora).toLocaleString("es-AR")} por hora
          </strong>
        </p>
      )}
      {error && <div className="alerta alerta-error">{error}</div>}

      <div className="fila-formulario">
        <label>
          Día
          <input
            type="date"
            name="fecha"
            min={fechaActual()}
            value={valores.fecha}
            onChange={cambiar}
            required
          />
        </label>
        <label>
          Hora de inicio
          <select name="hora" value={valores.hora} onChange={cambiar} required>
            <option value="">Elegí una hora</option>
            {horasDisponibles.map((hora) => (
              <option key={hora} value={hora}>{hora}</option>
            ))}
          </select>
        </label>
      </div>
      <p className="ayuda-horario">Cada reserva tiene una duración fija de 1 hora.</p>

      <button className="boton-principal" disabled={cargando} type="submit">
        {cargando ? "Confirmando..." : "Confirmar reserva"}
        <span aria-hidden="true">→</span>
      </button>
    </form>
  );
}
