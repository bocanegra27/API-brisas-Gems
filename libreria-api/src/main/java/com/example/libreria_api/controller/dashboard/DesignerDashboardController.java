package com.example.libreria_api.controller.dashboard;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/designer")
@PreAuthorize("hasRole('DISEÑADOR')")
public class DesignerDashboardController {

    @GetMapping("/dashboard")
    public Map<String, Object> getDesignerDashboard() {
        return Map.of(
                "message", "Bienvenido al Panel de Diseñador",
                "features", new String[] {
                        "Gestión de diseños 3D",
                        "Aprobación de renders",
                        "Seguimiento de pedidos",
                        "Comunicación con clientes"
                },
                "accessLevel", "DESIGNER",
                "description", "Panel especializado para diseñadores de joyas"
        );
    }

    @GetMapping("/disenos")
    public Map<String, String> misDisenos() {
        return Map.of("message", "Mis Diseños - Diseñador");
    }

    @GetMapping("/renders")
    public Map<String, String> gestionRenders() {
        return Map.of("message", "Gestión de Renders 3D - Diseñador");
    }

    @GetMapping("/pedidos")
    public Map<String, String> pedidosAsignados() {
        return Map.of("message", "Pedidos Asignados - Diseñador");
    }
}