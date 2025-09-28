package com.example.libreria_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                                // Endpoints públicos que siempre estarán abiertos
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/usuarios").permitAll()



                                // =================== MODO DE PRUEBA ===================
                                // Permite todas las solicitudes sin autenticación.
                                // Ideal para hacer pruebas en tus endpoints sin necesidad de un token.
                                //.anyRequest().permitAll()//

                        // =================== MODO DE PRODUCCIÓN ===================
                        // Para reactivar la autenticación, comenta la línea de arriba (.anyRequest().permitAll())
                        // y descomenta la siguiente línea. Esto requerirá un token para cualquier solicitud
                        // que no sea "/api/auth/**".
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
