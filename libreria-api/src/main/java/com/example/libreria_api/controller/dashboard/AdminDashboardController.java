package com.example.libreria_api.controller.dashboard;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminDashboardController {

    @GetMapping("/dashboard")
    public Map<String, Object> getAdminDashboard() {
        return Map.of(
                "message", "Bienvenido al Panel de Administración",
                "features", new String[] {
                        "Gestión de usuarios",
                        "Reportes del sistema",
                        "Configuración general",
                        "Monitoreo de pedidos"
                },
                "accessLevel", "ADMIN",
                "description", "Panel completo de administración del sistema Brisas Gems"
        );
    }

    @GetMapping("/usuarios")
    public Map<String, String> gestionUsuarios() {
        return Map.of("message", "Gestión de Usuarios - Administrador");
    }

    @GetMapping("/reportes")
    public Map<String, String> reportes() {
        return Map.of("message", "Reportes del Sistema - Administrador");
    }

    @GetMapping("/configuracion")
    public Map<String, String> configuracion() {
        return Map.of("message", "Configuración del Sistema - Administrador");
    }
}