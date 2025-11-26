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
    // Nuevo campo para el ID del usuario
    private Integer userId;
    private String token;
    private String dashboardUrl;    // URL de redirección basada en rol
    private String userRole;        // Rol principal del usuario
    private List<String> roles;     // Todos los roles del usuario
    private String email;           // Email del usuario
    private String userName;        // Nombre del usuario
    private String message;         // Mensaje de bienvenida

    // Constructor estático actualizado para incluir el userId
    public static LoginResponseDTO of(Integer userId, String token, String dashboardUrl,
                                      String userRole, List<String> roles,
                                      String email, String userName) {
        return LoginResponseDTO.builder()
                .userId(userId) // Se agrega el ID al builder
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