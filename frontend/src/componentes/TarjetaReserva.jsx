import { cancelarReserva } from "../servicios/reservas.js";

function mostrarFecha(valor) {
  return new Intl.DateTimeFormat("es-AR", {
    weekday: "short",
    day: "numeric",
    month: "short",
    hour: "2-digit",
    minute: "2-digit"
  }).format(new Date(valor));
}

function mostrarMoneda(valor) {
  return new Intl.NumberFormat("es-AR", {
    style: "currency",
    currency: "ARS",
    maximumFractionDigits: 0
  }).format(Number(valor));
}

export default function TarjetaReserva({ reserva, alCancelar }) {
  async function cancelar() {
    if (!window.confirm("¿Querés cancelar esta reserva?")) return;
    await cancelarReserva(reserva.identificador);
    alCancelar();
  }

  return (
    <article className="tarjeta-reserva">
      <div className="cabecera-tarjeta">
        <div>
          <span className="etiqueta-seccion">Reserva confirmada</span>
          <h3>{reserva.nombreCliente}</h3>
        </div>
        <span className="estado">{reserva.estado}</span>
      </div>
      <div className="detalle-reserva">
        <div><span>Cancha</span><strong>Cancha Central · Fútbol 5</strong></div>
        <div><span>Horario · 1 hora</span><strong>{mostrarFecha(reserva.inicio)} — {mostrarFecha(reserva.fin)}</strong></div>
      </div>
      <div className="resumen-pago">
        <div><span>Total del turno</span><strong>{mostrarMoneda(reserva.precioTotal)}</strong></div>
      </div>
      <button className="boton-secundario" onClick={cancelar}>Cancelar reserva</button>
      <small className="identificador">Código: {reserva.identificador}</small>
    </article>
  );
}
