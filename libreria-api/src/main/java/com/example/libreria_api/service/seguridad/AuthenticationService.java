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

        // Obtener usuario (asumiendo que 'user' es una entidad con un método getId())
        var user = usuarioRepository.findByUsuCorreo(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado después de autenticación"));

        // *** PASO 1: Obtener el ID del usuario ***
        // ASUMIENDO que tu entidad Usuario tiene un método getUsuId()
        Integer userId = user.getUsuId();

        // Generar token JWT
        var jwtToken = jwtService.generateToken(user);

        // Determinar dashboard basado en roles
        String dashboardUrl = dashboardService.determinarDashboardUrl(user);
        String userRole = dashboardService.obtenerRolPrincipal(user);

        // Construir respuesta con información completa
        return LoginResponseDTO.of(
                // *** PASO 2: Agregar el userId como primer argumento ***
                userId, // <-- Nuevo argumento
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