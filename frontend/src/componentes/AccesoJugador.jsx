import { useState } from "react";
import { iniciarSesionJugador, registrarJugador } from "../servicios/reservas.js";

export default function AccesoJugador({ alIngresar }) {
  const [modo, establecerModo] = useState("registro");
  const [datos, establecerDatos] = useState({ nombre: "", correo: "", contrasena: "" });
  const [error, establecerError] = useState("");

  function cambiar(evento) {
    establecerDatos((actuales) => ({ ...actuales, [evento.target.name]: evento.target.value }));
  }

  async function enviar(evento) {
    evento.preventDefault();
    establecerError("");
    try {
      const jugador = modo === "registro"
        ? await registrarJugador(datos)
        : await iniciarSesionJugador({ correo: datos.correo, contrasena: datos.contrasena });
      alIngresar(jugador);
    } catch (exception) {
      establecerError(exception.message);
    }
  }

  return (
    <section className="panel-dueno panel-acceso">
      <span className="etiqueta-seccion">Acceso de jugadores</span>
      <h2>{modo === "registro" ? "Creá tu cuenta" : "Iniciá sesión"}</h2>
      <p>Necesitás una cuenta para solicitar y consultar tus reservas.</p>
      <form className="formulario-acceso" onSubmit={enviar}>
        {modo === "registro" && <label>Nombre completo<input name="nombre" value={datos.nombre} onChange={cambiar} required /></label>}
        <label>Correo electrónico<input type="email" name="correo" value={datos.correo} onChange={cambiar} required /></label>
        <label>Contraseña<input type="password" name="contrasena" minLength="6" value={datos.contrasena} onChange={cambiar} required /></label>
        <button className="boton-principal" type="submit">
          {modo === "registro" ? "Crear cuenta" : "Iniciar sesión"}
        </button>
      </form>
      {error && <div className="alerta alerta-error">{error}</div>}
      <button className="enlace-modo" onClick={() => establecerModo(modo === "registro" ? "inicio" : "registro")}>
        {modo === "registro" ? "Ya tengo una cuenta" : "Crear una cuenta nueva"}
      </button>
    </section>
  );
}
