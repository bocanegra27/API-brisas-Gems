package com.example.libreria_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Habilita la configuración de seguridad web de Spring
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Este es el Bean de configuración principal para la seguridad.
     * Aquí definimos qué rutas están protegidas y cuáles no.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Deshabilitamos CSRF (Cross-Site Request Forgery) porque no usamos sesiones,
        // sino que nuestra API es 'stateless' (sin estado), común en APIs REST.
        http.csrf(csrf -> csrf.disable());

        // Aquí viene la regla clave:
        http.authorizeHttpRequests(auth ->
                // Para cualquier petición ("anyRequest")...
                auth.anyRequest()
                        // ...permite el acceso sin necesidad de autenticación ("permitAll").
                        .permitAll()
        );

        return http.build();
    }
}

