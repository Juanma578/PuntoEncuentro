const rutaReservas = "/api/reservas";

async function interpretarRespuesta(respuesta) {
  const contenido = await respuesta.text();
  let datos = {};

  if (contenido) {
    try {
      datos = JSON.parse(contenido);
    } catch {
      datos = { error: contenido };
    }
  }

  if (!respuesta.ok) {
    throw new Error(datos.error || "No se pudo completar la operación.");
  }

  return datos;
}

export async function crearReserva(datos) {
  const respuesta = await fetch(rutaReservas, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(datos)
  });
  return interpretarRespuesta(respuesta);
}

export async function obtenerReserva(identificador) {
  const respuesta = await fetch(`${rutaReservas}/${identificador}`);
  return interpretarRespuesta(respuesta);
}

export async function cancelarReserva(identificador, idDueno) {
  const ruta = idDueno
    ? `${rutaReservas}/${identificador}/dueno/${idDueno}`
    : `${rutaReservas}/${identificador}`;
  const respuesta = await fetch(ruta, {
    method: "DELETE"
  });
  return interpretarRespuesta(respuesta);
}

export async function listarReservas() {
  return interpretarRespuesta(await fetch(rutaReservas));
}

export async function listarReservasDueno(idDueno) {
  return interpretarRespuesta(await fetch(`${rutaReservas}/dueno/${idDueno}`));
}

export async function listarCanchas() {
  return interpretarRespuesta(await fetch(`${rutaReservas}/canchas`));
}

export async function listarCanchasDueno(idDueno) {
  return interpretarRespuesta(await fetch(`${rutaReservas}/dueno/${idDueno}/canchas`));
}

export async function aceptarReserva(identificador, idDueno) {
  return interpretarRespuesta(await fetch(`${rutaReservas}/${identificador}/aceptar/${idDueno}`, {
    method: "PATCH"
  }));
}

export async function registrarCancha(nombre) {
  return interpretarRespuesta(await fetch(`${rutaReservas}/canchas`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ nombre })
  }));
}

export async function registrarDueno(datos) {
  return interpretarRespuesta(await fetch("/api/duenos/registro", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(datos)
  }));
}

export async function iniciarSesionDueno(datos) {
  return interpretarRespuesta(await fetch("/api/duenos/inicio", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(datos)
  }));
}

export async function registrarCanchaDueno(idDueno, nombre, tarifaPorHora) {
  return interpretarRespuesta(await fetch(`/api/duenos/${idDueno}/canchas`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ nombre, tarifaPorHora: Number(tarifaPorHora) })
  }));
}

export async function registrarJugador(datos) {
  return interpretarRespuesta(await fetch("/api/jugadores/registro", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(datos)
  }));
}

export async function iniciarSesionJugador(datos) {
  return interpretarRespuesta(await fetch("/api/jugadores/inicio", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(datos)
  }));
}
