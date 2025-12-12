package com.example.libreria_api.dto.seguridad;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordDTO {
    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;
}