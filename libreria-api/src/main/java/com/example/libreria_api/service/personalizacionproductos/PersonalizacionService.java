package com.example.libreria_api.service.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionResponseDTO;
import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionUpdateDTO;
import com.example.libreria_api.model.personalizacionproductos.DetallePersonalizacion;
import com.example.libreria_api.model.personalizacionproductos.OpcionPersonalizacion;
import com.example.libreria_api.model.personalizacionproductos.Personalizacion;
import com.example.libreria_api.model.personalizacionproductos.ValorPersonalizacion;
import com.example.libreria_api.model.sistemausuarios.Usuario;
import com.example.libreria_api.model.sistemausuarios.SesionAnonima;
import com.example.libreria_api.repository.personalizacionproductos.DetallePersonalizacionRepository;
import com.example.libreria_api.repository.personalizacionproductos.PersonalizacionRepository;
import com.example.libreria_api.repository.personalizacionproductos.ValorPersonalizacionRepository;
import com.example.libreria_api.repository.sistemausuarios.SesionAnonimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    private SesionAnonimaRepository sesionAnonimaRepository;

    @Transactional(readOnly = true)
    public List<PersonalizacionResponseDTO> filtrarPersonalizaciones(Integer clienteId, LocalDateTime desde, LocalDateTime hasta) {
        List<Personalizacion> entidades = personalizacionRepository.findAll();
        return entidades.stream()
                .filter(p -> clienteId == null || (p.getUsuario() != null && p.getUsuario().getUsuId().equals(clienteId)))
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
        // Validar que tenga usuario O sesion (no ambos, no ninguno)
        if (dto.getUsuarioClienteId() != null && dto.getSesionId() != null) {
            throw new IllegalArgumentException("No puede especificar usuario y sesion simultaneamente");
        }
        if (dto.getUsuarioClienteId() == null && dto.getSesionId() == null) {
            throw new IllegalArgumentException("Debe especificar usuario o sesion");
        }

        Personalizacion nueva = new Personalizacion();
        nueva.setPerFecha(dto.getFecha());

        if (dto.getUsuarioClienteId() != null) {
            Usuario usuario = new Usuario();
            usuario.setUsuId(dto.getUsuarioClienteId());
            nueva.setUsuario(usuario);
        }

        if (dto.getSesionId() != null) {
            SesionAnonima sesion = sesionAnonimaRepository.findById(dto.getSesionId())
                    .orElseThrow(() -> new IllegalArgumentException("Sesion no encontrada"));
            nueva.setSesion(sesion);
        }

        Personalizacion guardada = personalizacionRepository.save(nueva);

        if (dto.getValoresSeleccionados() != null && !dto.getValoresSeleccionados().isEmpty()) {
            List<DetallePersonalizacion> detalles = new ArrayList<>();

            for (Integer valorId : dto.getValoresSeleccionados()) {
                ValorPersonalizacion valor = valorPersonalizacionRepository
                        .findById(valorId)
                        .orElseThrow(() -> new IllegalArgumentException("Valor no encontrado: " + valorId));

                DetallePersonalizacion detalle = new DetallePersonalizacion();
                detalle.setPersonalizacion(guardada);
                detalle.setValorPersonalizacion(valor);
                detalles.add(detalle);
            }

            detallePersonalizacionRepository.saveAll(detalles);
            guardada.setDetalles(detalles);
        }

        return mapToResponseDTO(guardada);
    }

    @Transactional
    public PersonalizacionResponseDTO actualizar(Integer id, PersonalizacionUpdateDTO dto) {
        return personalizacionRepository.findById(id).map(p -> {
            if (dto.getFecha() != null) {
                p.setPerFecha(dto.getFecha());
            }
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

        if (p.getUsuario() != null) {
            dto.setUsuarioClienteId(p.getUsuario().getUsuId());
            dto.setUsuarioNombre(p.getUsuario().getUsuNombre());
            dto.setTipoCliente("registrado");
        } else if (p.getSesion() != null) {
            dto.setSesionId(p.getSesion().getSesId());

            // ✅ CAMBIO: Verificación segura del token
            String token = p.getSesion().getSesToken();
            if (token != null && token.length() >= 8) {
                dto.setSesionToken(token.substring(0, 8));
            } else if (token != null) {
                dto.setSesionToken(token);
            }

            dto.setTipoCliente("anonimo");
        }

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