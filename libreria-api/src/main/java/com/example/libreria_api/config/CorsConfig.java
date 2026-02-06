package com.example.libreria_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de CORS (Cross-Origin Resource Sharing)
 * Permite que el frontend (React, Angular, Vue) en un origen diferente
 * pueda hacer peticiones a esta API
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();



        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://localhost:4200",
                "http://localhost:5173",
                "http://localhost:8081",
                "http://127.0.0.1:3000"       // localhost alternativo
        ));




        configuration.setAllowedOriginPatterns(Arrays.asList("*"));

        //Metodos HTTP
        configuration.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        // ========== HEADERS PERMITIDOS ==========
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With", // Para AJAX
                "Cache-Control"
        ));


        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Disposition"
        ));


        // Permite enviar cookies y headers de autenticación
        configuration.setAllowCredentials(true);


        configuration.setMaxAge(3600L); // 1 hora

        // Aplicar la configuración a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}