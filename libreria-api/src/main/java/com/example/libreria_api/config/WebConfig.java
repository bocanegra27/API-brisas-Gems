package com.example.libreria_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Detectamos la ruta donde se está ejecutando el servidor
        String projectPath = Paths.get("").toAbsolutePath().normalize().toString().replace("\\", "/");

        // Determinamos la ruta del módulo para llegar a los recursos de personalización
        String modulePath = projectPath.endsWith("libreria-api") ? projectPath : projectPath + "/libreria-api";

        // Determinamos la ruta raíz (donde vive la carpeta /uploads de pedidos)
        String rootPath = projectPath.endsWith("libreria-api") ?
                Paths.get(projectPath).getParent().toString().replace("\\", "/") :
                projectPath;

        // -----------------------------------------------------------
        // 1. CONFIGURACIÓN PARA PEDIDOS (./uploads) - NO SE TOCA LA LÓGICA
        // -----------------------------------------------------------
        String finalUploadPath = "file:///" + rootPath + "/uploads/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(finalUploadPath);

        System.out.println("📂 [Pedidos] Manteniendo carpeta uploads en: " + finalUploadPath);


        // -----------------------------------------------------------
        // 2. CONFIGURACIÓN PARA PERSONALIZACIÓN (Internal Assets)
        // -----------------------------------------------------------
        // Apuntamos a la carpeta assets dentro del módulo real
        String assetsPath = "file:///" + modulePath + "/src/main/resources/static/assets/";

        registry.addResourceHandler("/assets/**")
                .addResourceLocations(assetsPath);

        System.out.println("💎 [Personalización] Assets mapeados en: " + assetsPath);
    }
}