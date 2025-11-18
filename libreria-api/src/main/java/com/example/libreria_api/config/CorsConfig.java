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

        // ========== ORÍGENES PERMITIDOS ==========
        // En desarrollo: permitir localhost en diferentes puertos
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",      // React (Create React App)
                "http://localhost:3001",      // React (alternativo)
                "http://localhost:4200",      // Angular
                "http://localhost:5173",      // Vite (React/Vue)
                "http://localhost:8081",      // Otro backend
                "http://127.0.0.1:3000"       // localhost alternativo
        ));

        // PRODUCCIÓN: Especificar el dominio real
        // configuration.setAllowedOrigins(Arrays.asList("https://miapp.com"));

        // DESARROLLO: Permitir TODOS los orígenes (NO usar en producción)
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));

        // ========== MÉTODOS HTTP PERMITIDOS ==========
        configuration.setAllowedMethods(Arrays.asList(
                "GET",      // Leer recursos
                "POST",     // Crear recursos
                "PUT",      // Actualizar recursos completos
                "PATCH",    // Actualizar recursos parciales
                "DELETE",   // Eliminar recursos
                "OPTIONS"   // Peticiones preflight
        ));

        // ========== HEADERS PERMITIDOS ==========
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",    // Para JWT
                "Content-Type",     // Para JSON
                "Accept",           // Tipo de respuesta aceptada
                "X-Requested-With", // Para AJAX
                "Cache-Control"     // Control de caché
        ));

        // ========== HEADERS EXPUESTOS ==========
        // Headers que el frontend puede leer en la respuesta
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Disposition"  // Para descargas de archivos
        ));

        // ========== CREDENCIALES ==========
        // Permite enviar cookies y headers de autenticación
        configuration.setAllowCredentials(true);

        // ========== CACHÉ ==========
        // Cuánto tiempo el navegador puede cachear la respuesta preflight (en segundos)
        configuration.setMaxAge(3600L); // 1 hora

        // Aplicar la configuración a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}