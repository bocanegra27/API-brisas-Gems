package com.example.libreria_api.service.sistemausuarios;

import com.example.libreria_api.dto.sistemausuarios.RolResponseDTO;
import com.example.libreria_api.model.sistemausuarios.Rol;
import com.example.libreria_api.repository.sistemausuarios.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public List<RolResponseDTO> listarTodos() {
        return rolRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private RolResponseDTO mapToDto(Rol rol) {
        RolResponseDTO dto = new RolResponseDTO();
        dto.setId(rol.getRolId());
        dto.setNombre(rol.getRolNombre());
        return dto;
    }
}