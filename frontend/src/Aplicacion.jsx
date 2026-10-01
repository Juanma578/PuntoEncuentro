import { useState } from "react";
import FormularioReserva from "./componentes/FormularioReserva.jsx";
import TarjetaReserva from "./componentes/TarjetaReserva.jsx";
import { crearReserva } from "./servicios/reservas.js";
import PanelDueno from "./componentes/PanelDueno.jsx";
import AccesoJugador from "./componentes/AccesoJugador.jsx";

export default function Aplicacion() {
  const [reserva, establecerReserva] = useState(null);
  const [cargando, establecerCargando] = useState(false);
  const [error, establecerError] = useState("");
  const [mensaje, establecerMensaje] = useState("");
  const [vista, establecerVista] = useState("jugador");
  const [jugador, establecerJugador] = useState(null);

  async function crear(datos) {
    establecerCargando(true);
    establecerError("");
    establecerMensaje("");
    try {
      establecerReserva(await crearReserva(datos));
      establecerMensaje("Solicitud enviada. El dueño debe aceptarla.");
    } catch (exception) {
      establecerError(exception.message);
    } finally {
      establecerCargando(false);
    }
  }

  function reservaCancelada() {
    establecerReserva(null);
    establecerMensaje("La reserva fue cancelada correctamente.");
  }

  return (
    <main className="pagina">
      <nav className="navegacion">
        <a className="marca" href="/">
          <span className="marca-icono">✦</span>
          <span>Punto <b>Encuentro</b></span>
        </a>
        <span className="estado-conexion"><i /> Reservas online</span>
        <div className="selector-vista">
          <button className={vista === "jugador" ? "activo" : ""} onClick={() => establecerVista("jugador")}>Reservar</button>
          <button className={vista === "dueno" ? "activo" : ""} onClick={() => establecerVista("dueno")}>Soy dueño</button>
          {jugador && (
            <button
              className="boton-sesion"
              onClick={() => {
                establecerJugador(null);
                establecerReserva(null);
                establecerMensaje("");
                establecerError("");
              }}
            >
              Cerrar sesión
            </button>
          )}
        </div>
      </nav>

      <section className="hero">
        <div className="hero-contenido">
          <span className="etiqueta-seccion">Tu partido empieza acá</span>
          <h1>Reservá. <em>Jugá.</em><br />Disfrutá.</h1>
          <p>Encontrá tu cancha, elegí el horario y compartí el partido con quienes más querés.</p>
          <div className="estadisticas">
            <div><strong>01</strong><span>Cancha disponible</span></div>
            <div><strong>24/7</strong><span>Reservas online</span></div>
            <div><strong>100%</strong><span>Sin solapamientos</span></div>
          </div>
        </div>
        <div className="forma-decorativa"><span>⚽</span></div>
      </section>

      {vista === "dueno" ? <PanelDueno /> : !jugador ? <AccesoJugador alIngresar={(sesion) => {
        establecerJugador(sesion);
        establecerMensaje("");
        establecerError("");
      }} /> : <section className="contenido">
        <div className="columna-formulario">
          <FormularioReserva alCrear={crear} cargando={cargando} idJugador={jugador.identificador} />
        </div>
        <div className="columna-resultado">
          {error && <div className="alerta alerta-error">{error}</div>}
          {mensaje && <div className="alerta alerta-exito">{mensaje}</div>}
          {reserva ? (
            <TarjetaReserva reserva={reserva} alCancelar={reservaCancelada} />
          ) : (
            <div className="estado-vacio">
              <span className="pelota">⚽</span>
              <h2>Todavía no hay una reserva</h2>
              <p>Completá el formulario para asegurar tu próximo partido.</p>
            </div>
          )}
        </div>
      </section>}

      <footer>© 2026 Punto Encuentro <span>·</span> La pasión se juega en equipo.</footer>
    </main>
  );
}
