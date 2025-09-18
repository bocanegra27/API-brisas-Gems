package com.example.libreria_api.service.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.ValorPersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.ValorPersonalizacionResponseDTO;
import com.example.libreria_api.dto.personalizacionproductos.ValorPersonalizacionUpdateDTO;

import com.example.libreria_api.model.personalizacionproductos.OpcionPersonalizacion;
import com.example.libreria_api.model.personalizacionproductos.ValorPersonalizacion;

import com.example.libreria_api.repository.personalizacionproductos.OpcionPersonalizacionRepository;
import com.example.libreria_api.repository.personalizacionproductos.ValorPersonalizacionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValorPersonalizacionService {

    private final ValorPersonalizacionRepository valorRepo;
    private final OpcionPersonalizacionRepository opcionRepo;

    public ValorPersonalizacionService(ValorPersonalizacionRepository valorRepo,
                                       OpcionPersonalizacionRepository opcionRepo) {
        this.valorRepo = valorRepo;
        this.opcionRepo = opcionRepo;
    }

    // ==============================
    // LISTAR / FILTRAR
    // ==============================
    @Transactional(readOnly = true)
    public List<ValorPersonalizacionResponseDTO> listar(Integer opcId, String search) {
        List<ValorPersonalizacion> entidades;

        if (opcId != null && search != null && !search.isBlank()) {
            entidades = valorRepo.findByOpcionPersonalizacion_OpcIdAndValNombreContainingIgnoreCase(opcId, search);
        } else if (opcId != null) {
            entidades = valorRepo.findByOpcionPersonalizacion_OpcId(opcId);
        } else if (search != null && !search.isBlank()) {
            entidades = valorRepo.findByValNombreContainingIgnoreCase(search);
        } else {
            entidades = valorRepo.findAll();
        }

        return entidades.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ==============================
    // OBTENER POR ID
    // ==============================
    @Transactional(readOnly = true)
    public ValorPersonalizacionResponseDTO obtenerPorId(Integer id) {
        ValorPersonalizacion valor = valorRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Valor no encontrado con id: " + id));

        return mapToResponseDTO(valor);
    }

    // ==============================
    // CREAR
    // ==============================
    @Transactional
    public ValorPersonalizacionResponseDTO crear(ValorPersonalizacionCreateDTO dto) {
        // Validar existencia de la opción
        OpcionPersonalizacion opcion = opcionRepo.findById(dto.getOpcId())
                .orElseThrow(() -> new EntityNotFoundException("Opción no encontrada con id: " + dto.getOpcId()));

        // Validar duplicado
        List<ValorPersonalizacion> duplicados = valorRepo
                .findByOpcionPersonalizacion_OpcIdAndValNombreContainingIgnoreCase(dto.getOpcId(), dto.getNombre());
        if (!duplicados.isEmpty()) {
            throw new DataIntegrityViolationException("Ya existe un valor con ese nombre en la misma opción");
        }

        ValorPersonalizacion nueva = new ValorPersonalizacion();
        nueva.setValNombre(dto.getNombre());
        nueva.setValImagen(dto.getImagen());
        nueva.setOpcionPersonalizacion(opcion);

        ValorPersonalizacion guardada = valorRepo.save(nueva);
        return mapToResponseDTO(guardada);
    }

    // ==============================
    // ACTUALIZAR
    // ==============================
    @Transactional
    public ValorPersonalizacionResponseDTO actualizar(Integer id, ValorPersonalizacionUpdateDTO dto) {
        ValorPersonalizacion valor = valorRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Valor no encontrado con id: " + id));

        if (dto.getNombre() != null) {
            valor.setValNombre(dto.getNombre());
        }
        if (dto.getImagen() != null) {
            valor.setValImagen(dto.getImagen());
        }
        if (dto.getOpcId() != null) {
            OpcionPersonalizacion opcion = opcionRepo.findById(dto.getOpcId())
                    .orElseThrow(() -> new EntityNotFoundException("Opción no encontrada con id: " + dto.getOpcId()));
            valor.setOpcionPersonalizacion(opcion);
        }

        ValorPersonalizacion actualizada = valorRepo.save(valor);
        return mapToResponseDTO(actualizada);
    }

    // ==============================
    // ELIMINAR
    // ==============================
    @Transactional
    public void eliminar(Integer id) {
        ValorPersonalizacion valor = valorRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Valor no encontrado con id: " + id));

        valorRepo.delete(valor);
    }

    // ==============================
    // MAPPER (Entity → DTO)
    // ==============================
    private ValorPersonalizacionResponseDTO mapToResponseDTO(ValorPersonalizacion v) {
        ValorPersonalizacionResponseDTO dto = new ValorPersonalizacionResponseDTO();
        dto.setId(v.getValId());
        dto.setNombre(v.getValNombre());
        dto.setImagen(v.getValImagen());

        if (v.getOpcionPersonalizacion() != null) {
            dto.setOpcId(v.getOpcionPersonalizacion().getOpcId());
            dto.setOpcionNombre(v.getOpcionPersonalizacion().getOpcNombre());
        }

        return dto;
    }
}
