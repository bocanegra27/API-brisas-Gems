package com.example.libreria_api.controller.dashboard;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('USUARIO')")
public class UserDashboardController {

    @GetMapping("/dashboard")
    public Map<String, Object> getUserDashboard() {
        return Map.of(
                "message", "Bienvenido a tu Panel de Usuario",
                "features", new String[] {
                        "Mis pedidos activos",
                        "Historial de compras",
                        "Personalizaciones guardadas",
                        "Seguimiento de mis joyas"
                },
                "accessLevel", "USER",
                "description", "Panel personal para clientes de Brisas Gems"
        );
    }

    @GetMapping("/pedidos")
    public Map<String, String> misPedidos() {
        return Map.of("message", "Mis Pedidos - Usuario");
    }

    @GetMapping("/perfil")
    public Map<String, String> miPerfil() {
        return Map.of("message", "Mi Perfil - Usuario");
    }

    @GetMapping("/personalizaciones")
    public Map<String, String> misPersonalizaciones() {
        return Map.of("message", "Mis Personalizaciones - Usuario");
    }
}