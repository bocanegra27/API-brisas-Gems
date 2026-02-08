package com.example.libreria_api.service.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionResponseDTO;
import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionUpdateDTO;
import com.example.libreria_api.model.personalizacionproductos.*;
import com.example.libreria_api.model.sistemausuarios.Usuario;
import com.example.libreria_api.model.sistemausuarios.SesionAnonima;
import com.example.libreria_api.repository.personalizacionproductos.CategoriaProductoRepository;
import com.example.libreria_api.repository.personalizacionproductos.DetallePersonalizacionRepository;
import com.example.libreria_api.repository.personalizacionproductos.PersonalizacionRepository;
import com.example.libreria_api.repository.personalizacionproductos.ValorPersonalizacionRepository;
import com.example.libreria_api.repository.sistemausuarios.SesionAnonimaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonalizacionService {

    // Declaramos todos como final para asegurar inmutabilidad e inyección correcta
    private final PersonalizacionRepository personalizacionRepository;
    private final DetallePersonalizacionRepository detallePersonalizacionRepository;
    private final ValorPersonalizacionRepository valorPersonalizacionRepository;
    private final SesionAnonimaRepository sesionAnonimaRepository;
    private final CategoriaProductoRepository categoriaRepo;

    // INYECCIÓN POR CONSTRUCTOR (La forma recomendada)
    public PersonalizacionService(PersonalizacionRepository personalizacionRepository,
                                  DetallePersonalizacionRepository detallePersonalizacionRepository,
                                  ValorPersonalizacionRepository valorPersonalizacionRepository,
                                  SesionAnonimaRepository sesionAnonimaRepository,
                                  CategoriaProductoRepository categoriaRepo) {
        this.personalizacionRepository = personalizacionRepository;
        this.detallePersonalizacionRepository = detallePersonalizacionRepository;
        this.valorPersonalizacionRepository = valorPersonalizacionRepository;
        this.sesionAnonimaRepository = sesionAnonimaRepository;
        this.categoriaRepo = categoriaRepo;
    }

    @Transactional(readOnly = true)
    public List<PersonalizacionResponseDTO> filtrarPersonalizaciones(Integer clienteId, LocalDateTime desde, LocalDateTime hasta) {
        // Nota: findAll() y luego filtrar en memoria no es lo más eficiente para millones de registros,
        // pero para este proyecto está bien. Lo ideal sería usar Specifications o Query Methods.
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
        // 1. Validaciones de Usuario vs Sesión
        if (dto.getUsuarioClienteId() != null && dto.getSesionId() != null) {
            throw new IllegalArgumentException("No puede especificar usuario y sesion simultaneamente");
        }
        if (dto.getUsuarioClienteId() == null && dto.getSesionId() == null) {
            throw new IllegalArgumentException("Debe especificar usuario o sesion");
        }

        // 2. NUEVO: Buscar la categoría (Obligatorio)
        if (dto.getCatId() == null) {
            throw new IllegalArgumentException("El ID de la categoría es obligatorio");
        }
        CategoriaProducto categoria = categoriaRepo.findById(dto.getCatId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + dto.getCatId()));

        // 3. Crear entidad Personalizacion
        Personalizacion nueva = new Personalizacion();
        nueva.setPerFecha(dto.getFecha() != null ? dto.getFecha() : LocalDateTime.now());
        nueva.setCategoria(categoria); // <--- ASIGNAMOS LA CATEGORÍA

        // 4. Asignar Usuario o Sesión
        if (dto.getUsuarioClienteId() != null) {
            // Creamos referencia (hibernate se encarga del link si el ID existe)
            Usuario usuario = new Usuario();
            usuario.setUsuId(dto.getUsuarioClienteId());
            nueva.setUsuario(usuario);
        }

        if (dto.getSesionId() != null) {
            SesionAnonima sesion = sesionAnonimaRepository.findById(dto.getSesionId())
                    .orElseThrow(() -> new IllegalArgumentException("Sesion no encontrada"));
            nueva.setSesion(sesion);
        }

        // 5. Guardar la cabecera (Personalizacion)
        Personalizacion guardada = personalizacionRepository.save(nueva);

        // 6. Guardar los detalles (Valores seleccionados)
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
            // Normalmente no permitimos cambiar el usuario o la categoría en un update simple,
            // pero si necesitas cambiar usuario, aquí iría.
            if (dto.getUsuarioClienteId() != null) {
                Usuario usuario = new Usuario();
                usuario.setUsuId(dto.getUsuarioClienteId());
                p.setUsuario(usuario);
            }

            // Si necesitaras actualizar los valores seleccionados, tendrías que borrar los viejos
            // y crear los nuevos aquí. Por ahora mantenemos la lógica simple.

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

    // Método helper para convertir Entidad a DTO
    private PersonalizacionResponseDTO mapToResponseDTO(Personalizacion p) {
        PersonalizacionResponseDTO dto = new PersonalizacionResponseDTO();
        dto.setId(p.getPerId());
        dto.setFecha(p.getPerFecha());

        // Mapear Categoría (NUEVO)
        if (p.getCategoria() != null) {
            dto.setCatId(p.getCategoria().getCatId());
            dto.setCatNombre(p.getCategoria().getCatNombre());
        }

        // Mapear Usuario o Sesión
        if (p.getUsuario() != null) {
            dto.setUsuarioClienteId(p.getUsuario().getUsuId());
            dto.setUsuarioNombre(p.getUsuario().getUsuNombre());
            dto.setTipoCliente("registrado");
        } else if (p.getSesion() != null) {
            dto.setSesionId(p.getSesion().getSesId());

            String token = p.getSesion().getSesToken();
            if (token != null && token.length() >= 8) {
                dto.setSesionToken(token.substring(0, 8));
            } else if (token != null) {
                dto.setSesionToken(token);
            }
            dto.setTipoCliente("anonimo");
        }

        // Mapear Detalles
        if (p.getDetalles() != null && !p.getDetalles().isEmpty()) {
            List<PersonalizacionResponseDTO.DetalleDTO> detallesDTO = p.getDetalles().stream()
                    .map(detalle -> {
                        PersonalizacionResponseDTO.DetalleDTO detDTO = new PersonalizacionResponseDTO.DetalleDTO();
                        detDTO.setDetId(detalle.getDetId());

                        // --- LÍNEA ELIMINADA: detDTO.setPerId(...) ---
                        // No es necesaria y causaba el error

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