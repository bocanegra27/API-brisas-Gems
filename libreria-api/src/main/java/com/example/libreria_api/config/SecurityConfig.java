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
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final AuthEntryPoint authEntryPoint;
    private final AccessDenied accessDenied;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos
                        .requestMatchers("/api/auth/**").permitAll()   //dos asteriscos para arregalr problema
                        .requestMatchers("/api/usuarios").permitAll()
                        .requestMatchers("/api/opciones/**").permitAll()
                        .requestMatchers("/api/valores/**").permitAll()
                        .requestMatchers("/api/personalizaciones/**").permitAll()
                        .requestMatchers("/api/contactos/**").permitAll()

                        // las imagenes
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers("/static/**").permitAll()

                        // HEALTH CHECK público para monitoreo
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/actuator/info").permitAll()

                        // ENDPOINTS DE PEDIDOS - SOLO PARA ADMINISTRADORES
                        .requestMatchers("/api/pedidos/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/estados-pedido/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/opciones/").permitAll()
                        .requestMatchers("/api/valores/").permitAll()
                        .requestMatchers("/api/personalizaciones/").permitAll()
                        .requestMatchers("/api/contactos/").permitAll()

                        // Dashboards por rol
                        .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/designer/**").hasRole("DISEÑADOR")
                        .requestMatchers("/api/user/**").hasRole("USUARIO")

                        // Endpoints de API generales requieren autenticación
                        .requestMatchers("/api/**").authenticated()

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                // Manejo de excepciones de seguridad
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authEntryPoint)  // 401
                        .accessDeniedHandler(accessDenied)         // 403
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}