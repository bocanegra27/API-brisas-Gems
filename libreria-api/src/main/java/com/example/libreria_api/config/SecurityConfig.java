package com.example.libreria_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Importar AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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
                .csrf(AbstractHttpConfigurer::disable) // Usar el método de referencia para configuración moderna
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )


                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Endpoints públicos y de Autenticación
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/usuarios/**").permitAll()
                        .requestMatchers("/api/sesiones-anonimas/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/personalizaciones/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/contactos/**").permitAll()

                        // Rutas públicas de lectura (imágenes y salud)
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers("/static/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()

                        // Permite la lectura de todas las opciones/valores
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()
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

                        // Regla Catch-all: Si no coincide con lo anterior, requiere autenticación
                        .requestMatchers("/api/**").authenticated()


                        .anyRequest().authenticated()
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authEntryPoint)  // 401
                        .accessDeniedHandler(accessDenied)         // 403
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}