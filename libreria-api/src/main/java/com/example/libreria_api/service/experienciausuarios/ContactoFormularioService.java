package com.example.libreria_api.service.experienciausuarios;

import com.example.libreria_api.dto.experienciausuarios.ContactoFormularioCreateDTO;
import com.example.libreria_api.dto.experienciausuarios.ContactoFormularioUpdateDTO;
import com.example.libreria_api.dto.experienciausuarios.ContactoFormularioResponseDTO;
import com.example.libreria_api.model.experienciausuarios.ContactoFormulario;
import com.example.libreria_api.model.experienciausuarios.EstadoContacto;
import com.example.libreria_api.model.experienciausuarios.ViaContacto;
import com.example.libreria_api.model.sistemausuarios.Usuario;
import com.example.libreria_api.repository.experienciausuarios.ContactoFormularioRepository;
import com.example.libreria_api.repository.sistemausuarios.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactoFormularioService {

    private final ContactoFormularioRepository contactoRepository;
    private final UsuarioRepository usuarioRepository;

    public ContactoFormularioService(ContactoFormularioRepository contactoRepository,
                                     UsuarioRepository usuarioRepository) {
        this.contactoRepository = contactoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // ==============================
    // CREAR (público)
    // ==============================
    @Transactional
    public ContactoFormularioResponseDTO crear(ContactoFormularioCreateDTO dto) {
        ContactoFormulario entidad = new ContactoFormulario();
        entidad.setConNombre(dto.getNombre());
        entidad.setConCorreo(dto.getCorreo());
        entidad.setConTelefono(dto.getTelefono());
        entidad.setConMensaje(dto.getMensaje());
        if (dto.getVia() != null) {
            entidad.setConVia(ViaContacto.valueOf(dto.getVia()));
        } else {
            entidad.setConVia(ViaContacto.formulario); // default coherente con la BD
        }
        entidad.setConTerminos(dto.isTerminos());
        entidad.setConFechaEnvio(LocalDateTime.now());
        entidad.setConEstado(EstadoContacto.pendiente);

        ContactoFormulario guardado = contactoRepository.save(entidad);
        return mapToResponseDTO(guardado);
    }

    // ==============================
    // ACTUALIZAR (admin)
    // ==============================
    @Transactional
    public ContactoFormularioResponseDTO actualizar(Integer id, ContactoFormularioUpdateDTO dto) {
        ContactoFormulario entidad = contactoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contacto no encontrado"));

        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario cliente no encontrado"));
            entidad.setUsuario(usuario);
        }

        if (dto.getUsuarioIdAdmin() != null) {
            Usuario admin = usuarioRepository.findById(dto.getUsuarioIdAdmin())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario admin no encontrado"));
            entidad.setUsuarioAdmin(admin);
        }

        if (dto.getVia() != null) {
            entidad.setConVia(ViaContacto.valueOf(dto.getVia()));
        }

        if (dto.getEstado() != null) {
            entidad.setConEstado(EstadoContacto.valueOf(dto.getEstado()));
        }

        if (dto.getNotas() != null) {
            entidad.setConNotas(dto.getNotas());
        }

        ContactoFormulario actualizado = contactoRepository.save(entidad);
        return mapToResponseDTO(actualizado);
    }

    // ==============================
    // OBTENER POR ID
    // ==============================
    @Transactional(readOnly = true)
    public ContactoFormularioResponseDTO obtenerPorId(Integer id) {
        ContactoFormulario entidad = contactoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contacto no encontrado"));
        return mapToResponseDTO(entidad);
    }

    // ==============================
    // LISTAR CON FILTROS
    // ==============================
    @Transactional(readOnly = true)
    public List<ContactoFormularioResponseDTO> listarConFiltros(String via, String estado,
                                                                Integer usuarioId,
                                                                LocalDateTime desde,
                                                                LocalDateTime hasta) {
        ViaContacto viaFiltro = (via != null) ? ViaContacto.valueOf(via) : null;
        EstadoContacto estadoFiltro = (estado != null) ? EstadoContacto.valueOf(estado) : null;

        return contactoRepository.buscarConFiltros(viaFiltro, estadoFiltro, usuarioId, desde, hasta)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ==============================
    // ELIMINAR
    // ==============================
    @Transactional
    public void eliminar(Integer id) {
        if (!contactoRepository.existsById(id)) {
            throw new EntityNotFoundException("Contacto no encontrado");
        }
        contactoRepository.deleteById(id);
    }

    // ==============================
    // MAPEO Entidad -> ResponseDTO
    // ==============================
    private ContactoFormularioResponseDTO mapToResponseDTO(ContactoFormulario entidad) {
        ContactoFormularioResponseDTO dto = new ContactoFormularioResponseDTO();
        dto.setId(entidad.getConId());
        dto.setNombre(entidad.getConNombre());
        dto.setCorreo(entidad.getConCorreo());
        dto.setTelefono(entidad.getConTelefono());
        dto.setMensaje(entidad.getConMensaje());
        dto.setFechaEnvio(entidad.getConFechaEnvio());
        dto.setVia(entidad.getConVia().name());
        dto.setTerminos(entidad.isConTerminos());
        dto.setEstado(entidad.getConEstado().name());
        dto.setNotas(entidad.getConNotas());

        if (entidad.getUsuario() != null) {
            dto.setUsuarioId(entidad.getUsuario().getUsuId());
            dto.setUsuarioNombre(entidad.getUsuario().getUsuNombre());
        }

        if (entidad.getUsuarioAdmin() != null) {
            dto.setUsuarioIdAdmin(entidad.getUsuarioAdmin().getUsuId());
            dto.setUsuarioAdminNombre(entidad.getUsuarioAdmin().getUsuNombre());
        }

        return dto;
    }
}
