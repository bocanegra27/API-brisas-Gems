package com.example.libreria_api.service.seguridad;

import com.example.libreria_api.dto.seguridad.LoginRequestDTO;
import com.example.libreria_api.dto.seguridad.LoginResponseDTO;
import com.example.libreria_api.model.seguridad.Token;
import com.example.libreria_api.repository.seguridad.TokenRepository;
import com.example.libreria_api.repository.sistemausuarios.UsuarioRepository;
import com.example.libreria_api.service.notificaciones.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final DashboardService dashboardService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;


    private final EmailService emailService;

    public LoginResponseDTO login(LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = usuarioRepository.findByUsuCorreo(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Integer userId = user.getUsuId();
        var jwtToken = jwtService.generateToken(user);
        String dashboardUrl = dashboardService.determinarDashboardUrl(user);
        String userRole = dashboardService.obtenerRolPrincipal(user);

        return LoginResponseDTO.of(
                userId, jwtToken, dashboardUrl, userRole,
                user.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList()),
                user.getUsername(), user.getUsuNombre()
        );
    }

    /**
     * Genera un token y envía el correo REAL
     */
    public void forgotPassword(String email) {
        var userOpt = usuarioRepository.findByUsuCorreo(email);

        if (userOpt.isEmpty()) {
            return;
        }

        var usuario = userOpt.get();
        String tokenString = UUID.randomUUID().toString();

        Token token = new Token();
        token.setToken(tokenString);
        token.setTipo("recuperacion");
        token.setFechaExpiracion(LocalDateTime.now().plusHours(24));
        token.setUsuario(usuario);

        tokenRepository.save(token);


        emailService.enviarCorreoRecuperacion(email, usuario.getUsuNombre(), tokenString);

        System.out.println(">>> Proceso de recuperación iniciado para: " + email);
    }

    public void resetPassword(String tokenString, String newPassword) {

        Token token = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (token.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El token ha expirado");
        }

        var usuario = token.getUsuario();
        usuario.setUsuPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
        tokenRepository.delete(token);
    }
}