package com.example.libreria_api.dto.seguridad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String dashboardUrl;    // URL de redirección basada en rol
    private String userRole;        // Rol principal del usuario
    private List<String> roles;     // Todos los roles del usuario
    private String email;           // Email del usuario
    private String userName;        // Nombre del usuario
    private String message;         // Mensaje de bienvenida

    // Constructor para facilitar la creación
    public static LoginResponseDTO of(String token, String dashboardUrl,
                                      String userRole, List<String> roles,
                                      String email, String userName) {
        return LoginResponseDTO.builder()
                .token(token)
                .dashboardUrl(dashboardUrl)
                .userRole(userRole)
                .roles(roles)
                .email(email)
                .userName(userName)
                .message("Bienvenido " + userName + " (" + userRole + ")")
                .build();
    }
}