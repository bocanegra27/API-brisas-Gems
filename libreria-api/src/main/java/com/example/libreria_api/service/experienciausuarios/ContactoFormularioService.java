package com.example.libreria_api.service.experienciausuarios;

import com.example.libreria_api.dto.experienciausuarios.ContactoFormularioCreateDTO;
import com.example.libreria_api.dto.experienciausuarios.ContactoFormularioUpdateDTO;
import com.example.libreria_api.dto.experienciausuarios.ContactoFormularioResponseDTO;
import com.example.libreria_api.model.experienciausuarios.ContactoFormulario;
import com.example.libreria_api.model.experienciausuarios.EstadoContacto;
import com.example.libreria_api.model.experienciausuarios.ViaContacto;
import com.example.libreria_api.model.personalizacionproductos.DetallePersonalizacion;
import com.example.libreria_api.model.personalizacionproductos.OpcionPersonalizacion;
import com.example.libreria_api.model.personalizacionproductos.Personalizacion;
import com.example.libreria_api.model.personalizacionproductos.ValorPersonalizacion;
import com.example.libreria_api.model.sistemausuarios.SesionAnonima;
import com.example.libreria_api.model.sistemausuarios.Usuario;
import com.example.libreria_api.repository.experienciausuarios.ContactoFormularioRepository;
import com.example.libreria_api.repository.sistemausuarios.UsuarioRepository;
import com.example.libreria_api.repository.personalizacionproductos.PersonalizacionRepository;
import com.example.libreria_api.repository.personalizacionproductos.DetallePersonalizacionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContactoFormularioService {

    private final ContactoFormularioRepository contactoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PersonalizacionRepository personalizacionRepository;
    private final DetallePersonalizacionRepository detallePersonalizacionRepository;

    public ContactoFormularioService(ContactoFormularioRepository contactoRepository,
                                     UsuarioRepository usuarioRepository,
                                     PersonalizacionRepository personalizacionRepository,
                                     DetallePersonalizacionRepository detallePersonalizacionRepository) {
        this.contactoRepository = contactoRepository;
        this.usuarioRepository = usuarioRepository;
        this.personalizacionRepository = personalizacionRepository;
        this.detallePersonalizacionRepository = detallePersonalizacionRepository;
    }

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
            entidad.setConVia(ViaContacto.formulario);
        }

        entidad.setConTerminos(dto.isTerminos());
        entidad.setConFechaEnvio(LocalDateTime.now());
        entidad.setConEstado(EstadoContacto.pendiente);

        // FIX: Mapear la identidad del cliente (Usuario o Sesión Anónima)
        // La entidad ContactoFormulario usa objetos relacionados, no IDs directos.
        if (dto.getUsuarioId() != null && dto.getUsuarioId() > 0) {
            // Es un usuario registrado
            Usuario usuario = new Usuario();
            usuario.setUsuId(dto.getUsuarioId());
            entidad.setUsuario(usuario); // Mapea al objeto Usuario
            // Asegurarse de que el campo de sesión sea nulo
            entidad.setSesion(null);
        }
        else if (dto.getSesionId() != null && dto.getSesionId() > 0) {
            // Es un usuario anónimo
            SesionAnonima sesion = new SesionAnonima();
            sesion.setSesId(dto.getSesionId());
            entidad.setSesion(sesion); // Mapea al objeto Sesion
            // Asegurarse de que el campo de usuario sea nulo
            entidad.setUsuario(null);
        }

        // NUEVO: Mapear personalizacion si existe
        if (dto.getPersonalizacionId() != null) {
            Personalizacion personalizacion = new Personalizacion();
            personalizacion.setPerId(dto.getPersonalizacionId());
            entidad.setPersonalizacion(personalizacion);
        }

        ContactoFormulario guardado = contactoRepository.save(entidad);
        return mapToResponseDTO(guardado);
    }

    @Transactional
    public ContactoFormularioResponseDTO actualizar(Integer id, ContactoFormularioUpdateDTO dto) {
        ContactoFormulario entidad = contactoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contacto no encontrado"));
        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId()).orElseThrow(() -> new EntityNotFoundException("Usuario cliente no encontrado"));
            entidad.setUsuario(usuario);
        }
        if (dto.getUsuarioIdAdmin() != null) {
            Usuario admin = usuarioRepository.findById(dto.getUsuarioIdAdmin()).orElseThrow(() -> new EntityNotFoundException("Usuario admin no encontrado"));
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

    @Transactional(readOnly = true)
    public ContactoFormularioResponseDTO obtenerPorId(Integer id) {
        ContactoFormulario entidad = contactoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contacto no encontrado"));
        return mapToResponseDTO(entidad);
    }

    @Transactional(readOnly = true)
    public List<ContactoFormularioResponseDTO> listarConFiltros(String via, String estado, Integer usuarioId, LocalDateTime desde, LocalDateTime hasta) {
        ViaContacto viaFiltro = (via != null) ? ViaContacto.valueOf(via) : null;
        EstadoContacto estadoFiltro = (estado != null) ? EstadoContacto.valueOf(estado) : null;
        return contactoRepository.buscarConFiltros(viaFiltro, estadoFiltro, usuarioId, desde, hasta).stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!contactoRepository.existsById(id)) {
            throw new EntityNotFoundException("Contacto no encontrado");
        }
        contactoRepository.deleteById(id);
    }

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

        // Mapear usuario registrado
        if (entidad.getUsuario() != null) {
            dto.setUsuarioId(entidad.getUsuario().getUsuId());
            dto.setUsuarioNombre(entidad.getUsuario().getUsuNombre());
            dto.setTipoCliente("registrado");
        }
        // Mapear sesion anonima
        else if (entidad.getSesion() != null) {
            dto.setSesionId(entidad.getSesion().getSesId());

            // ✅ CAMBIO: Verificación segura del token
            String token = entidad.getSesion().getSesToken();
            if (token != null && token.length() >= 8) {
                dto.setSesionToken(token.substring(0, 8));
            } else if (token != null) {
                dto.setSesionToken(token);
            }
            // Si token es null, simplemente no se setea y queda null en el DTO

            dto.setTipoCliente("anonimo");
        }
        // Sin usuario ni sesion
        else {
            dto.setTipoCliente("externo");
        }

        // Mapear admin
        if (entidad.getUsuarioAdmin() != null) {
            dto.setUsuarioIdAdmin(entidad.getUsuarioAdmin().getUsuId());
            dto.setUsuarioAdminNombre(entidad.getUsuarioAdmin().getUsuNombre());
        }

        // Mapear personalizacion vinculada
        if (entidad.getPersonalizacion() != null) {
            dto.setPersonalizacionId(entidad.getPersonalizacion().getPerId());
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public long contarContactosPorEstado(String estado) {
        try {
             EstadoContacto estadoEnum = EstadoContacto.valueOf(estado.toLowerCase());
             return contactoRepository.countByConEstado(estadoEnum);
        } catch (IllegalArgumentException e) {
             return 0;
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerContactoConPersonalizacion(Integer contactoId) {
        // Obtener contacto
        ContactoFormulario contacto = contactoRepository.findById(contactoId)
                .orElseThrow(() -> new EntityNotFoundException("Contacto no encontrado"));

        Map<String, Object> resultado = new HashMap<>();

        // Datos básicos del contacto
        resultado.put("contacto", mapToResponseDTO(contacto));

        // Si tiene personalización vinculada, obtener detalles
        if (contacto.getPersonalizacion() != null) {
            Integer perId = contacto.getPersonalizacion().getPerId();

            // Obtener personalización completa
            Personalizacion personalizacion = personalizacionRepository.findById(perId)
                    .orElse(null);

            if (personalizacion != null) {
                Map<String, Object> datosPersonalizacion = new HashMap<>();
                datosPersonalizacion.put("id", personalizacion.getPerId());
                datosPersonalizacion.put("fecha", personalizacion.getPerFecha());

                // Obtener detalles de personalización
                List<DetallePersonalizacion> detalles =
                        detallePersonalizacionRepository.findByPersonalizacion_PerId(perId);

                // Construir resumen legible
                Map<String, String> resumen = new HashMap<>();
                List<Map<String, Object>> detallesFormateados = new ArrayList<>();

                for (DetallePersonalizacion detalle : detalles) {
                    // Obtener nombres desde las relaciones
                    ValorPersonalizacion valor = detalle.getValorPersonalizacion();
                    OpcionPersonalizacion opcion = valor.getOpcionPersonalizacion();

                    String opcionNombre = opcion.getOpcNombre();
                    String valorNombre = valor.getValNombre();

                    // Agregar al resumen
                    resumen.put(opcionNombre, valorNombre);

                    // Agregar a detalles formateados
                    Map<String, Object> detalleFormateado = new HashMap<>();
                    detalleFormateado.put("detId", detalle.getDetId());
                    detalleFormateado.put("opcionId", opcion.getOpcId());
                    detalleFormateado.put("opcionNombre", opcionNombre);
                    detalleFormateado.put("valorId", valor.getValId());
                    detalleFormateado.put("valorNombre", valorNombre);
                    detalleFormateado.put("valorImagen", valor.getValImagen());

                    detallesFormateados.add(detalleFormateado);
                }

                datosPersonalizacion.put("detalles", detallesFormateados);
                datosPersonalizacion.put("resumen", resumen);

                resultado.put("personalizacion", datosPersonalizacion);
                resultado.put("tienePersonalizacion", true);
            } else {
                resultado.put("tienePersonalizacion", false);
            }
        } else {
            resultado.put("tienePersonalizacion", false);
        }

        return resultado;
    }
}