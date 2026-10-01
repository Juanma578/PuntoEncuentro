package app.prueba.puntoencuentro.reserva.servicio;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public record Jugador(UUID identificador, String nombre, String correo, String contrasenaHash) {
    public static Jugador crear(String nombre, String correo, String contrasena) {
        validarDatos(nombre, correo, contrasena);
        return new Jugador(UUID.randomUUID(), nombre.trim(), correo.trim().toLowerCase(), hash(contrasena));
    }

    public static void validarInicioSesion(String correo, String contrasena) {
        if (!correoValido(correo) || contrasena == null || contrasena.isBlank()) {
            throw new IllegalArgumentException("Correo y contraseña son obligatorios y válidos");
        }
    }

    private static void validarDatos(String nombre, String correo, String contrasena) {
        if (nombre == null || nombre.isBlank() || !correoValido(correo)
                || contrasena == null || contrasena.length() < 6) {
            throw new IllegalArgumentException("Nombre, correo y contraseña válida son obligatorios");
        }
    }

    private static boolean correoValido(String correo) {
        return correo != null && correo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    public boolean contrasenaValida(String contrasena) {
        return MessageDigest.isEqual(contrasenaHash.getBytes(StandardCharsets.UTF_8),
                hash(contrasena).getBytes(StandardCharsets.UTF_8));
    }

    private static String hash(String valor) {
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-256")
                    .digest(valor.getBytes(StandardCharsets.UTF_8));
            StringBuilder resultado = new StringBuilder();
            for (byte caracter : bytes) resultado.append(String.format("%02x", caracter));
            return resultado.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("No se pudo proteger la contraseña", exception);
        }
    }
}
