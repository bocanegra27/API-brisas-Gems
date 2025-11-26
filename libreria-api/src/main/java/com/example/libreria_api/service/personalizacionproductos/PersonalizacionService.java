package com.example.libreria_api.service.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionResponseDTO;
import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionUpdateDTO;
import com.example.libreria_api.model.personalizacionproductos.DetallePersonalizacion;
import com.example.libreria_api.model.personalizacionproductos.OpcionPersonalizacion;
import com.example.libreria_api.model.personalizacionproductos.Personalizacion;
import com.example.libreria_api.model.personalizacionproductos.ValorPersonalizacion;
import com.example.libreria_api.model.sistemausuarios.Usuario;
import com.example.libreria_api.repository.personalizacionproductos.DetallePersonalizacionRepository;
import com.example.libreria_api.repository.personalizacionproductos.PersonalizacionRepository;
import com.example.libreria_api.repository.personalizacionproductos.ValorPersonalizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonalizacionService {

    @Autowired
    private PersonalizacionRepository personalizacionRepository;

    @Autowired
    private DetallePersonalizacionRepository detallePersonalizacionRepository;

    @Autowired
    private ValorPersonalizacionRepository valorPersonalizacionRepository;

    @Transactional(readOnly = true)
    public List<PersonalizacionResponseDTO> filtrarPersonalizaciones(Integer clienteId, LocalDate desde, LocalDate hasta) {
        List<Personalizacion> entidades = personalizacionRepository.findAll();
        return entidades.stream()
                .filter(p -> clienteId == null || (p.getUsuario() != null && p.getUsuario().getUsuId() == clienteId))
                .filter(p -> desde == null || !p.getPerFecha().isBefore(desde))
                .filter(p -> hasta == null || !p.getPerFecha().isAfter(hasta))
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PersonalizacionResponseDTO obtenerPorId(Integer id) {
        Optional<Personalizacion> opt = personalizacionRepository.findById(id);
        return opt.map(this::mapToResponseDTO).orElse(null);
    }

    @Transactional
    public PersonalizacionResponseDTO crear(PersonalizacionCreateDTO dto) {
        // 1️⃣ Crear el encabezado de personalización
        Personalizacion nueva = new Personalizacion();
        nueva.setPerFecha(dto.getFecha());

        // Solo asignar usuario si no es null (usuarios anónimos permitidos)
        if (dto.getUsuarioClienteId() != null) {
            Usuario usuario = new Usuario();
            usuario.setUsuId(dto.getUsuarioClienteId());
            nueva.setUsuario(usuario);
        }

        // Guardar primero la personalización para obtener el ID
        Personalizacion guardada = personalizacionRepository.save(nueva);

        // 2️⃣ Crear los detalles de personalización (SI HAY VALORES SELECCIONADOS)
        if (dto.getValoresSeleccionados() != null && !dto.getValoresSeleccionados().isEmpty()) {
            List<DetallePersonalizacion> detalles = new ArrayList<>();

            for (Integer valorId : dto.getValoresSeleccionados()) {
                // Buscar el valor en la base de datos
                ValorPersonalizacion valor = valorPersonalizacionRepository
                        .findById(valorId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Valor de personalización no encontrado: " + valorId));

                // Crear el detalle
                DetallePersonalizacion detalle = new DetallePersonalizacion();
                detalle.setPersonalizacion(guardada);
                detalle.setValorPersonalizacion(valor);

                detalles.add(detalle);
            }

            // Guardar todos los detalles
            detallePersonalizacionRepository.saveAll(detalles);

            // Asignar los detalles a la personalización guardada (para que se incluyan en la respuesta)
            guardada.setDetalles(detalles);
        }

        // 3️⃣ Retornar el DTO con todos los datos
        return mapToResponseDTO(guardada);
    }

    @Transactional
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

    @Transactional
    public boolean eliminar(Integer id) {
        if (!personalizacionRepository.existsById(id)) {
            return false;
        }
        personalizacionRepository.deleteById(id);
        return true;
    }

    private PersonalizacionResponseDTO mapToResponseDTO(Personalizacion p) {
        PersonalizacionResponseDTO dto = new PersonalizacionResponseDTO();
        dto.setId(p.getPerId());
        dto.setFecha(p.getPerFecha());

        // Mapear usuario (puede ser null para usuarios anónimos)
        if (p.getUsuario() != null) {
            dto.setUsuarioClienteId(p.getUsuario().getUsuId());
            dto.setUsuarioNombre(p.getUsuario().getUsuNombre());
        } else {
            dto.setUsuarioClienteId(null); // ✅ Ahora sí retorna null correctamente
        }

        // Mapear detalles
        if (p.getDetalles() != null && !p.getDetalles().isEmpty()) {
            List<PersonalizacionResponseDTO.DetalleDTO> detallesDTO = p.getDetalles().stream()
                    .map(detalle -> {
                        PersonalizacionResponseDTO.DetalleDTO detDTO = new PersonalizacionResponseDTO.DetalleDTO();
                        detDTO.setDetId(detalle.getDetId());

                        ValorPersonalizacion valor = detalle.getValorPersonalizacion();
                        if (valor != null) {
                            detDTO.setValId(valor.getValId());
                            detDTO.setValNombre(valor.getValNombre());

                            OpcionPersonalizacion opcion = valor.getOpcionPersonalizacion();
                            if (opcion != null) {
                                detDTO.setOpcionId(opcion.getOpcId());
                                detDTO.setOpcionNombre(opcion.getOpcNombre());
                            }
                        }
                        return detDTO;
                    })
                    .collect(Collectors.toList());
            dto.setDetalles(detallesDTO);
        } else {
            dto.setDetalles(Collections.emptyList());
        }

        return dto;
    }
}