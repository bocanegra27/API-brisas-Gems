package com.example.libreria_api.dto.seguridad;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}