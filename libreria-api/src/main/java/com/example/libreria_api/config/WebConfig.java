package com.example.libreria_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // -----------------------------------------------------------
        // 1. CONFIGURACIÓN PARA PEDIDOS (./uploads)
        // -----------------------------------------------------------
        Path uploadDir = Paths.get("./uploads");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        // Aseguramos que el path tenga el formato correcto para file protocol
        String finalUploadPath = "file:///" + uploadPath + "/";

        // Fix para Windows (a veces las barras invertidas dan problemas)
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            finalUploadPath = "file:///" + uploadPath.replace("\\", "/") + "/";
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(finalUploadPath);

        System.out.println("📂 [Pedidos] Carpeta uploads expuesta en: " + finalUploadPath);


        // -----------------------------------------------------------
        // 2. CONFIGURACIÓN PARA PERSONALIZACIÓN (src/.../assets)
        // -----------------------------------------------------------
        String projectPath = Paths.get(".").toAbsolutePath().normalize().toString();
        String assetsPath = "file:///" + projectPath + "/src/main/resources/static/assets/";

        // Fix para Windows
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            assetsPath = "file:///" + projectPath.replace("\\", "/") + "/src/main/resources/static/assets/";
        }

        registry.addResourceHandler("/assets/**")
                .addResourceLocations(assetsPath);

        System.out.println("💎 [Personalización] Assets dinámicos expuestos en: " + assetsPath);
    }
}