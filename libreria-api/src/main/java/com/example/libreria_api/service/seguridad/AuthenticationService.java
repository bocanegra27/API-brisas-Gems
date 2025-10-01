package com.example.libreria_api.service.seguridad;

import com.example.libreria_api.dto.seguridad.LoginRequestDTO;
import com.example.libreria_api.dto.seguridad.LoginResponseDTO;
import com.example.libreria_api.repository.sistemausuarios.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final DashboardService dashboardService;

    public LoginResponseDTO login(LoginRequestDTO request) {
        // Autenticar credenciales
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Obtener usuario
        var user = usuarioRepository.findByUsuCorreo(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado después de autenticación"));

        // Generar token JWT
        var jwtToken = jwtService.generateToken(user);

        // Determinar dashboard basado en roles
        String dashboardUrl = dashboardService.determinarDashboardUrl(user);
        String userRole = dashboardService.obtenerRolPrincipal(user);

        // Construir respuesta con información completa
        return LoginResponseDTO.of(
                jwtToken,
                dashboardUrl,
                userRole,
                user.getAuthorities().stream()
                        .map(auth -> auth.getAuthority())
                        .collect(Collectors.toList()),
                user.getUsername(),
                user.getUsuNombre()
        );
    }
}