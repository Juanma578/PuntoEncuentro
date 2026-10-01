import { useEffect, useState } from "react";
import {
  aceptarReserva, cancelarReserva, iniciarSesionDueno, listarReservas,
  listarCanchas, registrarCanchaDueno, registrarDueno
} from "../servicios/reservas.js";

const registroInicial = { nombre: "", correo: "", contrasena: "" };

export default function PanelDueno() {
  const [dueno, establecerDueno] = useState(null);
  const [modo, establecerModo] = useState("registro");
  const [datos, establecerDatos] = useState(registroInicial);
  const [nombreCancha, establecerNombreCancha] = useState("");
  const [tarifaPorHora, establecerTarifaPorHora] = useState("");
  const [canchas, establecerCanchas] = useState([]);
  const [reservas, establecerReservas] = useState([]);
  const [error, establecerError] = useState("");
  const [mensaje, establecerMensaje] = useState("");

  async function cargarDatos() {
    try {
      const [reservasActuales, canchasActuales] = await Promise.all([
        listarReservas(),
        listarCanchas()
      ]);
      establecerReservas(reservasActuales);
      establecerCanchas(canchasActuales);
    } catch (exception) { establecerError(exception.message); }
  }

  useEffect(() => { if (dueno) cargarDatos(); }, [dueno]);

  function cambiarDato(evento) {
    establecerDatos((actuales) => ({ ...actuales, [evento.target.name]: evento.target.value }));
  }

  async function autenticar(evento) {
    evento.preventDefault();
    establecerError("");
    try {
      const resultado = modo === "registro"
        ? await registrarDueno(datos)
        : await iniciarSesionDueno({ correo: datos.correo, contrasena: datos.contrasena });
      establecerDueno(resultado);
      establecerMensaje(modo === "registro" ? "Cuenta creada correctamente." : "Sesión iniciada.");
    } catch (exception) { establecerError(exception.message); }
  }

  async function registrarCancha(evento) {
    evento.preventDefault();
    establecerError("");
    try {
      const cancha = await registrarCanchaDueno(dueno.identificador, nombreCancha, tarifaPorHora);
      establecerCanchas((actuales) => [...actuales, cancha]);
      establecerNombreCancha("");
      establecerTarifaPorHora("");
      establecerMensaje(`Cancha "${cancha.nombre}" registrada correctamente.`);
    } catch (exception) { establecerError(exception.message); }
  }

  async function cambiarEstado(identificador, aceptar) {
    try {
      if (aceptar) await aceptarReserva(identificador);
      else await cancelarReserva(identificador);
      await cargarDatos();
    } catch (exception) { establecerError(exception.message); }
  }

  if (!dueno) {
    return (
      <section className="panel-dueno panel-acceso">
        <span className="etiqueta-seccion">Acceso para dueños</span>
        <h2>{modo === "registro" ? "Registrá tu cuenta" : "Ingresá a tu panel"}</h2>
        <p>Administrá tus canchas y confirmá los turnos desde un solo lugar.</p>
        <form className="formulario-acceso" onSubmit={autenticar}>
          {modo === "registro" && <label>Nombre completo<input name="nombre" value={datos.nombre} onChange={cambiarDato} required /></label>}
          <label>Correo electrónico<input type="email" name="correo" value={datos.correo} onChange={cambiarDato} required /></label>
          <label>Contraseña<input type="password" name="contrasena" minLength="6" value={datos.contrasena} onChange={cambiarDato} required /></label>
          <button className="boton-principal" type="submit">{modo === "registro" ? "Crear cuenta" : "Iniciar sesión"}</button>
        </form>
        {error && <div className="alerta alerta-error">{error}</div>}
        <button className="enlace-modo" onClick={() => establecerModo(modo === "registro" ? "inicio" : "registro")}>
          {modo === "registro" ? "Ya tengo una cuenta" : "Crear una cuenta nueva"}
        </button>
      </section>
    );
  }

  return (
    <section className="panel-dueno">
      <div className="cabecera-panel">
        <div><span className="etiqueta-seccion">Panel del dueño</span><h2>Hola, {dueno.nombre}</h2></div>
        <button className="enlace-modo" onClick={() => establecerDueno(null)}>Cerrar sesión</button>
      </div>
      <p>Registrá tu cancha con el precio por hora y administrá las solicitudes.</p>
      <form className="registro-cancha" onSubmit={registrarCancha}>
        <input value={nombreCancha} onChange={(evento) => establecerNombreCancha(evento.target.value)} placeholder="Nombre de la cancha" required />
        <input type="number" min="1" step="0.01" value={tarifaPorHora} onChange={(evento) => establecerTarifaPorHora(evento.target.value)} placeholder="Precio por hora" required />
        <button className="boton-principal" type="submit">Registrar cancha</button>
      </form>
      <div className="lista-canchas">
        <h3>Tus canchas</h3>
        {canchas.length === 0
          ? <p className="texto-suave">Todavía no registraste canchas.</p>
          : <ul>{canchas.map((cancha) => <li key={cancha.identificador}>{cancha.nombre} — ${Number(cancha.tarifaPorHora).toLocaleString("es-AR")} por hora</li>)}</ul>}
      </div>
      {mensaje && <div className="alerta alerta-exito">{mensaje}</div>}
      {error && <div className="alerta alerta-error">{error}</div>}
      <div className="lista-reservas">
        <h3>Solicitudes de reserva</h3>
        {reservas.length === 0 && <p className="texto-suave">No hay reservas todavía.</p>}
        {reservas.map((reserva) => (
          <article className="fila-reserva" key={reserva.identificador}>
            <div><strong>{reserva.nombreCliente}</strong><span>{new Date(reserva.inicio).toLocaleString("es-AR")} · 1 hora</span></div>
            <span className="estado">{reserva.estado}</span>
            {reserva.estado === "PENDIENTE" && <div className="acciones-reserva">
              <button onClick={() => cambiarEstado(reserva.identificador, true)}>Aceptar</button>
              <button onClick={() => cambiarEstado(reserva.identificador, false)}>Cancelar</button>
            </div>}
          </article>
        ))}
      </div>
    </section>
  );
}
