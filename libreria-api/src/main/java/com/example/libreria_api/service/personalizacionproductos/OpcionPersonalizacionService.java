package com.example.libreria_api.service.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.OpcionPersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.OpcionPersonalizacionResponseDTO;
import com.example.libreria_api.dto.personalizacionproductos.OpcionPersonalizacionUpdateDTO;
import com.example.libreria_api.model.personalizacionproductos.OpcionPersonalizacion;
import com.example.libreria_api.model.personalizacionproductos.ValorPersonalizacion;
import com.example.libreria_api.repository.personalizacionproductos.OpcionPersonalizacionRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OpcionPersonalizacionService {

    private final OpcionPersonalizacionRepository opcionRepo;

    public OpcionPersonalizacionService(OpcionPersonalizacionRepository opcionRepo) {
        this.opcionRepo = opcionRepo;
    }

    // ==============================
    // CREAR
    // ==============================
    @Transactional
    public OpcionPersonalizacionResponseDTO crear(OpcionPersonalizacionCreateDTO dto) {
        if (opcionRepo.findByOpcNombre(dto.getNombre()).isPresent()) {
            throw new DataIntegrityViolationException("Ya existe una opción con ese nombre");
        }

        OpcionPersonalizacion nueva = new OpcionPersonalizacion(dto.getNombre());
        OpcionPersonalizacion guardada = opcionRepo.save(nueva);

        return new OpcionPersonalizacionResponseDTO(guardada.getOpcId(), guardada.getOpcNombre());
    }

    // ==============================
    // ACTUALIZAR
    // ==============================
    @Transactional
    public OpcionPersonalizacionResponseDTO actualizar(int id, OpcionPersonalizacionUpdateDTO dto) {
        OpcionPersonalizacion opcion = opcionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Opción no encontrada con id: " + id));

        Optional<OpcionPersonalizacion> existente = opcionRepo.findByOpcNombre(dto.getNombre());
        if (existente.isPresent() && existente.get().getOpcId() != id) {
            throw new DataIntegrityViolationException("Ya existe otra opción con ese nombre");
        }

        opcion.setOpcNombre(dto.getNombre());
        OpcionPersonalizacion actualizada = opcionRepo.save(opcion);

        return new OpcionPersonalizacionResponseDTO(actualizada.getOpcId(), actualizada.getOpcNombre());
    }

    // ==============================
    // LISTAR (con búsqueda opcional)
    // ==============================
    @Transactional(readOnly = true)
    public List<OpcionPersonalizacionResponseDTO> listar(String filtro) {
        List<OpcionPersonalizacion> entidades;

        if (filtro == null || filtro.isBlank()) {
            entidades = opcionRepo.findAll();
        } else {
            entidades = opcionRepo.findByOpcNombreContainingIgnoreCase(filtro);
        }

        return entidades.stream()
                .map(op -> new OpcionPersonalizacionResponseDTO(op.getOpcId(), op.getOpcNombre()))
                .collect(Collectors.toList());
    }

    // ==============================
    // OBTENER POR ID
    // ==============================
    @Transactional(readOnly = true)
    public OpcionPersonalizacionResponseDTO obtenerPorId(int id) {
        OpcionPersonalizacion opcion = opcionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Opción no encontrada con id: " + id));

        return new OpcionPersonalizacionResponseDTO(opcion.getOpcId(), opcion.getOpcNombre());
    }

    // ==============================
    // ELIMINAR
    // ==============================
    @Transactional
    public void eliminar(int id) {
        OpcionPersonalizacion opcion = opcionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Opción no encontrada con id: " + id));

        List<ValorPersonalizacion> valores = opcion.getValores();
        if (valores != null && !valores.isEmpty()) {
            throw new DataIntegrityViolationException("No se puede eliminar: tiene valores asociados");
        }

        opcionRepo.delete(opcion);
    }
}
