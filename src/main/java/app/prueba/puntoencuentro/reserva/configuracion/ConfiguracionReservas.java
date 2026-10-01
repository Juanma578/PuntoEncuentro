package app.prueba.puntoencuentro.reserva.configuracion;

import app.prueba.puntoencuentro.reserva.observador.RegistradorEventosReserva;
import app.prueba.puntoencuentro.reserva.observador.ObservadorReserva;
import app.prueba.puntoencuentro.reserva.precios.EstrategiaPrecioPico;
import app.prueba.puntoencuentro.reserva.precios.EstrategiaPrecio;
import app.prueba.puntoencuentro.reserva.precios.EstrategiaPrecioEstandar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionReservas {
    @Bean
    EstrategiaPrecio estrategiaPrecioPico() { return new EstrategiaPrecioPico(); }
    @Bean
    EstrategiaPrecio estrategiaPrecioEstandar() { return new EstrategiaPrecioEstandar(); }
    @Bean
    ObservadorReserva registradorEventosReserva() { return new RegistradorEventosReserva(); }
}


