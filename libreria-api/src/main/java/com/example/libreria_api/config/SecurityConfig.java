package com.example.libreria_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        // =================================================================
                        // 🔓 PERMISOS PARA SWAGGER
                        // =================================================================
                        .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui.html")).permitAll()

                        // Endpoints públicos y de Autenticación
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/usuarios/**").permitAll()
                        .requestMatchers("/api/sesiones-anonimas/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/personalizaciones/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/contactos/**").permitAll()

                        // Rutas públicas de lectura
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers("/static/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()

                        // =================================================================
                        // 👤 CLIENTES (MIS PEDIDOS) - IMPORTANTE: Antes del permitAll general
                        // =================================================================
                        .requestMatchers("/api/pedidos/mis-pedidos/**").hasAnyRole("USUARIO", "ADMINISTRADOR", "DISEÑADOR")

                        // =================================================================
                        // 🟢 CONFIGURACIÓN PARA PRUEBAS (PEDIDOS GENERAL)
                        // Permite CRUD general (Útil para pruebas, cuidado en producción)
                        .requestMatchers("/api/pedidos/**").permitAll()

                        // Permite la lectura de todas las opciones/valores
                        .requestMatchers(HttpMethod.GET, "/api/opciones/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/valores/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/personalizaciones/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/contactos/**").permitAll()

                        // =================================================================

                        // ENDPOINTS RESTRINGIDOS
                        .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/designer/**").hasRole("DISEÑADOR")
                        .requestMatchers("/api/user/**").hasRole("USUARIO")
                        .requestMatchers("/api/estados-pedido/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/pedidos/desde-contacto/**").hasRole("ADMINISTRADOR")

                        // Regla Catch-all
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDenied)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}