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

    private Integer userId;
    private String token;
    private String dashboardUrl;
    private String userRole;
    private List<String> roles;
    private String email;
    private String userName;
    private String message;


    public static LoginResponseDTO of(Integer userId, String token, String dashboardUrl,
                                      String userRole, List<String> roles,
                                      String email, String userName) {
        return LoginResponseDTO.builder()
                .userId(userId)
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