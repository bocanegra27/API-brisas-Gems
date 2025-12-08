package com.example.libreria_api.service.sistemausuarios;

import com.example.libreria_api.dto.sistemausuarios.SesionAnonimaCreateDTO;
import com.example.libreria_api.dto.sistemausuarios.SesionAnonimaResponseDTO;
import com.example.libreria_api.model.sistemausuarios.SesionAnonima;
import com.example.libreria_api.model.sistemausuarios.Usuario;
import com.example.libreria_api.repository.sistemausuarios.SesionAnonimaRepository;
import com.example.libreria_api.repository.sistemausuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SesionAnonimaService {

    @Autowired
    private SesionAnonimaRepository sesionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public SesionAnonimaResponseDTO crearSesion(SesionAnonimaCreateDTO dto) {
        String token = dto.getToken() != null ? dto.getToken() : UUID.randomUUID().toString();

        SesionAnonima sesion = new SesionAnonima(token);
        SesionAnonima guardada = sesionRepository.save(sesion);

        return mapToResponseDTO(guardada);
    }

    @Transactional(readOnly = true)
    public SesionAnonimaResponseDTO obtenerPorToken(String token) {
        Optional<SesionAnonima> opt = sesionRepository.findBySesToken(token);
        return opt.map(this::mapToResponseDTO).orElse(null);
    }

    @Transactional
    public SesionAnonimaResponseDTO convertirSesion(String token, Integer usuarioId) {
        SesionAnonima sesion = sesionRepository.findBySesToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Sesion no encontrada"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        sesion.setSesConvertido(true);
        sesion.setUsuarioConvertido(usuario);

        SesionAnonima guardada = sesionRepository.save(sesion);
        return mapToResponseDTO(guardada);
    }

    @Transactional(readOnly = true)
    public boolean validarSesion(String token) {
        Optional<SesionAnonima> opt = sesionRepository.findBySesToken(token);
        if (opt.isEmpty()) {
            return false;
        }
        return !opt.get().isExpirada();
    }

    private SesionAnonimaResponseDTO mapToResponseDTO(SesionAnonima s) {
        SesionAnonimaResponseDTO dto = new SesionAnonimaResponseDTO();
        dto.setSesId(s.getSesId());
        dto.setSesToken(s.getSesToken());
        dto.setSesFechaCreacion(s.getSesFechaCreacion());
        dto.setSesFechaExpiracion(s.getSesFechaExpiracion());
        dto.setSesConvertido(s.getSesConvertido());

        if (s.getUsuarioConvertido() != null) {
            dto.setUsuarioConvertidoId(s.getUsuarioConvertido().getUsuId());
        }

        return dto;
    }
}