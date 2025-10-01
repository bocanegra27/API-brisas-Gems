package com.example.libreria_api.service.seguridad;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final Map<String, String> roleDashboardMap;

    public DashboardService() {
        // Mapeo de roles a URLs de dashboard
        roleDashboardMap = new HashMap<>();
        roleDashboardMap.put("ROLE_ADMINISTRADOR", "/admin/dashboard");
        roleDashboardMap.put("ROLE_DISEÑADOR", "/designer/dashboard");
        roleDashboardMap.put("ROLE_USUARIO", "/user/dashboard");
    }

    public String determinarDashboardUrl(UserDetails userDetails) {
        // Buscar el primer rol que tenga un dashboard configurado
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            String role = authority.getAuthority();
            if (roleDashboardMap.containsKey(role)) {
                return roleDashboardMap.get(role);
            }
        }

        // Dashboard por defecto si no se encuentra un rol específico
        return "/com/example/libreria_api/controller/dashboard";
    }

    public String obtenerRolPrincipal(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USUARIO");
    }

    // Método para obtener todos los dashboards disponibles (útil para el frontend)
    public Map<String, String> getAvailableDashboards() {
        return new HashMap<>(roleDashboardMap);
    }
}