package com.example.libreria_api.service.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.DetallePersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.DetallePersonalizacionUpdateDTO;
import com.example.libreria_api.dto.personalizacionproductos.DetallePersonalizacionResponseDTO;
import com.example.libreria_api.model.personalizacionproductos.DetallePersonalizacion;
import com.example.libreria_api.model.personalizacionproductos.Personalizacion;
import com.example.libreria_api.model.personalizacionproductos.ValorPersonalizacion;
import com.example.libreria_api.repository.personalizacionproductos.DetallePersonalizacionRepository;
import com.example.libreria_api.repository.personalizacionproductos.PersonalizacionRepository;
import com.example.libreria_api.repository.personalizacionproductos.ValorPersonalizacionRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetallePersonalizacionService {

    private final DetallePersonalizacionRepository detalleRepo;
    private final PersonalizacionRepository personalizacionRepo;
    private final ValorPersonalizacionRepository valorRepo;

    public DetallePersonalizacionService(
            DetallePersonalizacionRepository detalleRepo,
            PersonalizacionRepository personalizacionRepo,
            ValorPersonalizacionRepository valorRepo) {
        this.detalleRepo = detalleRepo;
        this.personalizacionRepo = personalizacionRepo;
        this.valorRepo = valorRepo;
    }


    @Transactional
    public DetallePersonalizacionResponseDTO crear(int perId, DetallePersonalizacionCreateDTO dto) {
        Personalizacion personalizacion = personalizacionRepo.findById(perId)
                .orElseThrow(() -> new EntityNotFoundException("Personalización no encontrada con id: " + perId));

        ValorPersonalizacion valor = valorRepo.findById(dto.getValId())
                .orElseThrow(() -> new EntityNotFoundException("Valor no encontrado con id: " + dto.getValId()));

        // Evitar duplicados en la misma personalización
        boolean existe = detalleRepo.findByPersonalizacion_PerIdAndValorPersonalizacion_ValId(perId, dto.getValId())
                .stream().findAny().isPresent();
        if (existe) {
            throw new DataIntegrityViolationException("Ya existe un detalle con ese valor en la personalización");
        }

        DetallePersonalizacion nuevo = new DetallePersonalizacion(personalizacion, valor);
        DetallePersonalizacion guardado = detalleRepo.save(nuevo);

        return mapToDTO(guardado);
    }


    @Transactional
    public DetallePersonalizacionResponseDTO actualizar(int detId, DetallePersonalizacionUpdateDTO dto) {
        DetallePersonalizacion detalle = detalleRepo.findById(detId)
                .orElseThrow(() -> new EntityNotFoundException("Detalle no encontrado con id: " + detId));

        ValorPersonalizacion valor = valorRepo.findById(dto.getValId())
                .orElseThrow(() -> new EntityNotFoundException("Valor no encontrado con id: " + dto.getValId()));

        detalle.setValorPersonalizacion(valor);
        DetallePersonalizacion actualizado = detalleRepo.save(detalle);

        return mapToDTO(actualizado);
    }



    @Transactional(readOnly = true)
    public List<DetallePersonalizacionResponseDTO> listarPorPersonalizacion(int perId) {
        return detalleRepo.findByPersonalizacion_PerId(perId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public DetallePersonalizacionResponseDTO obtenerPorId(int detId) {
        DetallePersonalizacion detalle = detalleRepo.findById(detId)
                .orElseThrow(() -> new EntityNotFoundException("Detalle no encontrado con id: " + detId));
        return mapToDTO(detalle);
    }


    @Transactional
    public void eliminar(int detId) {
        DetallePersonalizacion detalle = detalleRepo.findById(detId)
                .orElseThrow(() -> new EntityNotFoundException("Detalle no encontrado con id: " + detId));
        detalleRepo.delete(detalle);
    }


    private DetallePersonalizacionResponseDTO mapToDTO(DetallePersonalizacion det) {
        DetallePersonalizacionResponseDTO dto = new DetallePersonalizacionResponseDTO();
        dto.setDetId(det.getDetId());
        dto.setPerId(det.getPersonalizacion().getPerId());
        dto.setValId(det.getValorPersonalizacion().getValId());
        dto.setValNombre(det.getValorPersonalizacion().getValNombre());
        dto.setOpcionId(det.getValorPersonalizacion().getOpcionPersonalizacion().getOpcId());
        dto.setOpcionNombre(det.getValorPersonalizacion().getOpcionPersonalizacion().getOpcNombre());
        return dto;
    }
}
