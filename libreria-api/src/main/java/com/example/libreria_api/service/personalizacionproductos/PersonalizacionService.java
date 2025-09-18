package com.example.libreria_api.service.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionResponseDTO;
import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionUpdateDTO;

import com.example.libreria_api.model.personalizacionproductos.Personalizacion;
import com.example.libreria_api.model.sistemausuarios.Usuario;
import com.example.libreria_api.repository.personalizacionproductos.PersonalizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonalizacionService {

    @Autowired
    private PersonalizacionRepository personalizacionRepository;

    // ==============================
    // LISTAR / FILTRAR
    // ==============================
    public List<PersonalizacionResponseDTO> filtrarPersonalizaciones(Integer clienteId,
                                                                     LocalDate desde,
                                                                     LocalDate hasta) {
        List<Personalizacion> entidades = personalizacionRepository.findAll();

        return entidades.stream()
                .filter(p -> clienteId == null || (p.getUsuario() != null && p.getUsuario().getUsuId() == clienteId))
                .filter(p -> desde == null || !p.getPerFecha().isBefore(desde))
                .filter(p -> hasta == null || !p.getPerFecha().isAfter(hasta))
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ==============================
    // OBTENER POR ID
    // ==============================
    public PersonalizacionResponseDTO obtenerPorId(Integer id) {
        Optional<Personalizacion> opt = personalizacionRepository.findById(id);
        return opt.map(this::mapToResponseDTO).orElse(null);
    }

    // ==============================
    // CREAR
    // ==============================
    public PersonalizacionResponseDTO crear(PersonalizacionCreateDTO dto) {
        Personalizacion nueva = new Personalizacion();
        nueva.setPerFecha(dto.getFecha());

        // Relación con Usuario (solo seteamos el id)
        Usuario usuario = new Usuario();
        usuario.setUsuId(dto.getUsuarioClienteId());
        nueva.setUsuario(usuario);

        Personalizacion guardada = personalizacionRepository.save(nueva);
        return mapToResponseDTO(guardada);
    }

    // ==============================
    // ACTUALIZAR
    // ==============================
    public PersonalizacionResponseDTO actualizar(Integer id, PersonalizacionUpdateDTO dto) {
        return personalizacionRepository.findById(id).map(p -> {
            if (dto.getFecha() != null) p.setPerFecha(dto.getFecha());
            if (dto.getUsuarioClienteId() != null) {
                Usuario usuario = new Usuario();
                usuario.setUsuId(dto.getUsuarioClienteId());
                p.setUsuario(usuario);
            }
            Personalizacion guardada = personalizacionRepository.save(p);
            return mapToResponseDTO(guardada);
        }).orElse(null);
    }

    // ==============================
    // ELIMINAR
    // ==============================
    public boolean eliminar(Integer id) {
        if (!personalizacionRepository.existsById(id)) {
            return false;
        }
        personalizacionRepository.deleteById(id);
        return true;
    }

    // ==============================
    // MAPPER (Entity → DTO)
    // ==============================
    private PersonalizacionResponseDTO mapToResponseDTO(Personalizacion p) {
        PersonalizacionResponseDTO dto = new PersonalizacionResponseDTO();
        dto.setId(p.getPerId());
        dto.setFecha(p.getPerFecha());

        if (p.getUsuario() != null) {
            dto.setUsuarioClienteId(p.getUsuario().getUsuId());
            dto.setUsuarioNombre(p.getUsuario().getUsuNombre());
        }

        // por ahora dejamos detalles vacío (lo implementamos al avanzar con DetallePersonalizacion)
        dto.setDetalles(new ArrayList<>());

        return dto;
    }
}
