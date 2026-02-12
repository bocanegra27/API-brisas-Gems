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

        // Determinamos la ruta raíz (donde está la carpeta /uploads)
        String rootPath = projectPath.endsWith("libreria-api") ?
                Paths.get(projectPath).getParent().toString().replace("\\", "/") :
                projectPath;

        // Definimos la ruta absoluta hacia la carpeta uploads externa
        String finalUploadPath = "file:///" + rootPath + "/uploads/";

        // -----------------------------------------------------------
        // 1. CONFIGURACIÓN ÚNICA PARA /uploads
        // -----------------------------------------------------------
        // Esto servirá tanto para pedidos como para personalización manual
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(finalUploadPath);

        System.out.println("📂 [Servidor] Carpeta global de archivos activa en: " + finalUploadPath);

        // -----------------------------------------------------------
        // 2. MANTENER OTROS ASSETS (Opcional)
        // -----------------------------------------------------------
        // Si aún tienes CSS o JS internos, mantenemos esta línea,
        // pero ya NO para las imágenes de las joyas.
        String internalAssetsPath = "file:///" + projectPath + "/src/main/resources/static/assets/";
        registry.addResourceHandler("/assets/**")
                .addResourceLocations(internalAssetsPath);
    }
}