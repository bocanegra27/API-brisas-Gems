package com.example.libreria_api.service.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.ValorPersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.ValorPersonalizacionResponseDTO;
import com.example.libreria_api.dto.personalizacionproductos.ValorPersonalizacionUpdateDTO;

import com.example.libreria_api.model.personalizacionproductos.CategoriaProducto;
import com.example.libreria_api.model.personalizacionproductos.OpcionPersonalizacion;
import com.example.libreria_api.model.personalizacionproductos.ValorPersonalizacion;

import com.example.libreria_api.repository.personalizacionproductos.OpcionPersonalizacionRepository;
import com.example.libreria_api.repository.personalizacionproductos.ValorPersonalizacionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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


    @Transactional(readOnly = true)
    public ValorPersonalizacionResponseDTO obtenerPorId(Integer id) {
        ValorPersonalizacion valor = valorRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Valor no encontrado con id: " + id));

        return mapToResponseDTO(valor);
    }


    @Transactional
    public ValorPersonalizacionResponseDTO crear(ValorPersonalizacionCreateDTO dto, MultipartFile archivo) {
        try {
            OpcionPersonalizacion opcion = opcionRepo.findById(dto.getOpcId())
                    .orElseThrow(() -> new EntityNotFoundException("Opción no encontrada"));

            ValorPersonalizacion valor = new ValorPersonalizacion();
            valor.setValNombre(dto.getNombre());
            valor.setOpcionPersonalizacion(opcion);

            // LOGICA SIMPLIFICADA: Solo guardamos imagen si REALMENTE viene un archivo
            if (archivo != null && !archivo.isEmpty()) {
                CategoriaProducto categoria = opcion.getCategoria();
                String catSlug = categoria.getCatSlug();

                // ... (Lógica de crear carpetas igual que antes) ...
                String folderPath = "src/main/resources/static/assets/img/personalizacion/"
                        + catSlug + "/opciones/" + opcion.getOpcId() + "/";

                File directory = new File(folderPath);
                if (!directory.exists()) directory.mkdirs();

                String fileName = archivo.getOriginalFilename();
                Path path = Paths.get(folderPath + fileName);
                Files.copy(archivo.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                valor.setValImagen(fileName);
            } else {
                // SI ES TEXTO PURO (Tu caso actual)
                valor.setValImagen(null);
            }

            ValorPersonalizacion guardada = valorRepo.save(valor);
            return mapToResponseDTO(guardada);

        } catch (java.io.IOException e) {
            throw new RuntimeException("Error de archivo: " + e.getMessage());
        }
    }

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


    @Transactional
    public void eliminar(Integer id) {
        ValorPersonalizacion valor = valorRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Valor no encontrado con id: " + id));

        valorRepo.delete(valor);
    }


    private ValorPersonalizacionResponseDTO mapToResponseDTO(ValorPersonalizacion valor) {
        ValorPersonalizacionResponseDTO dto = new ValorPersonalizacionResponseDTO();
        dto.setId(valor.getValId());
        dto.setNombre(valor.getValNombre());
        dto.setImagen(valor.getValImagen());
        if (valor.getOpcionPersonalizacion() != null) {
            dto.setOpcId(valor.getOpcionPersonalizacion().getOpcId());
            dto.setOpcionNombre(valor.getOpcionPersonalizacion().getOpcNombre());
        }
        return dto;
    }
}
