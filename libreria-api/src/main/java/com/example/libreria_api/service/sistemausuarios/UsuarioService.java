package com.example.libreria_api.service.sistemausuarios;

import com.example.libreria_api.dto.sistemausuarios.*;
import com.example.libreria_api.exception.BadRequestException;
import com.example.libreria_api.exception.DuplicateResourceException;
import com.example.libreria_api.exception.ResourceNotFoundException;
import com.example.libreria_api.model.sistemausuarios.*;
import com.example.libreria_api.repository.sistemausuarios.RolRepository;
import com.example.libreria_api.repository.sistemausuarios.TipoDeDocumentoRepository;
import com.example.libreria_api.repository.sistemausuarios.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final TipoDeDocumentoRepository tipoDocumentoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          TipoDeDocumentoRepository tipoDocumentoRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> listarUsuarios(Integer rolId, Boolean activo, Pageable pageable) {
        Page<Usuario> pagina;
        if (rolId != null && activo != null) {
            pagina = usuarioRepository.findByRol_RolIdAndUsuActivo(rolId, activo, pageable);
        } else if (rolId != null) {
            pagina = usuarioRepository.findByRol_RolIdAndUsuActivo(rolId, true, pageable);
        } else if (activo != null) {
            pagina = usuarioRepository.findByUsuActivo(activo, pageable);
        } else {
            pagina = usuarioRepository.findByUsuActivo(true, pageable);
        }
        return pagina.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Integer id) {
        // ✅ CAMBIO: ResourceNotFoundException
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        return toResponse(u);
    }

    @Transactional
    public UsuarioResponseDTO crear(UsuarioCreateDTO dto) {
        // ✅ CAMBIO: DuplicateResourceException para email
        if (usuarioRepository.existsByUsuCorreo(dto.getCorreo())) {
            throw new DuplicateResourceException("Usuario", "correo", dto.getCorreo());
        }

        // ✅ CAMBIO: DuplicateResourceException para documento
        if (dto.getDocnum() != null && usuarioRepository.existsByUsuDocnum(dto.getDocnum())) {
            throw new DuplicateResourceException("Usuario", "documento", dto.getDocnum());
        }

        // ✅ CAMBIO: ResourceNotFoundException para rol
        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", dto.getRolId()));

        TipoDeDocumento tip = null;
        if (dto.getTipdocId() != null) {
            // ✅ CAMBIO: ResourceNotFoundException para tipo documento
            tip = tipoDocumentoRepository.findById(dto.getTipdocId())
                    .orElseThrow(() -> new ResourceNotFoundException("TipoDeDocumento", "id", dto.getTipdocId()));
        }

        Usuario u = new Usuario();
        u.setUsuNombre(dto.getNombre());
        u.setUsuCorreo(dto.getCorreo());
        u.setUsuTelefono(dto.getTelefono());
        u.setUsuPassword(passwordEncoder.encode(dto.getPassword()));
        u.setUsuDocnum(dto.getDocnum());
        u.setUsuOrigen(dto.getOrigen() != null ? OrigenUsuario.valueOf(dto.getOrigen()) : OrigenUsuario.formulario);
        u.setUsuActivo(dto.getActivo() != null ? dto.getActivo() : false);
        u.setRol(rol);
        u.setTipoDocumento(tip);

        return toResponse(usuarioRepository.save(u));
    }

    @Transactional
    public UsuarioResponseDTO actualizar(Integer id, UsuarioUpdateDTO dto) {
        // ✅ CAMBIO: ResourceNotFoundException
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        // ✅ CAMBIO: DuplicateResourceException para correo
        if (dto.getCorreo() != null && !dto.getCorreo().equalsIgnoreCase(u.getUsuCorreo())) {
            if (usuarioRepository.existsByUsuCorreo(dto.getCorreo())) {
                throw new DuplicateResourceException("Usuario", "correo", dto.getCorreo());
            }
            u.setUsuCorreo(dto.getCorreo());
        }

        // ✅ CAMBIO: DuplicateResourceException para documento
        if (dto.getDocnum() != null && !dto.getDocnum().equals(u.getUsuDocnum())) {
            if (usuarioRepository.existsByUsuDocnum(dto.getDocnum())) {
                throw new DuplicateResourceException("Usuario", "documento", dto.getDocnum());
            }
            u.setUsuDocnum(dto.getDocnum());
        }

        if (dto.getNombre() != null) u.setUsuNombre(dto.getNombre());
        if (dto.getTelefono() != null) u.setUsuTelefono(dto.getTelefono());
        if (dto.getActivo() != null) u.setUsuActivo(dto.getActivo());
        if (dto.getOrigen() != null) u.setUsuOrigen(OrigenUsuario.valueOf(dto.getOrigen()));

        if (dto.getRolId() != null) {
            // ✅ CAMBIO: ResourceNotFoundException
            Rol rol = rolRepository.findById(dto.getRolId())
                    .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", dto.getRolId()));
            u.setRol(rol);
        }

        if (dto.getTipdocId() != null) {
            // ✅ CAMBIO: ResourceNotFoundException
            TipoDeDocumento tip = tipoDocumentoRepository.findById(dto.getTipdocId())
                    .orElseThrow(() -> new ResourceNotFoundException("TipoDeDocumento", "id", dto.getTipdocId()));
            u.setTipoDocumento(tip);
        }

        return toResponse(usuarioRepository.save(u));
    }

    @Transactional
    public void eliminar(Integer id) {
        // ✅ Ya tenías ResourceNotFoundException aquí, perfecto
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        usuario.setUsuActivo(false);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void actualizarPassword(Integer id, PasswordUpdateDTO dto) {
        // ✅ CAMBIO: ResourceNotFoundException
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        // ✅ CAMBIO: BadRequestException para contraseña incorrecta
        if (!passwordEncoder.matches(dto.getPasswordActual(), u.getUsuPassword())) {
            throw new BadRequestException("La contraseña actual no es correcta");
        }

        u.setUsuPassword(passwordEncoder.encode(dto.getPasswordNueva()));
        usuarioRepository.save(u);
    }

    @Transactional
    public UsuarioResponseDTO actualizarRol(Integer id, Integer nuevoRolId) {
        // ✅ CAMBIO: ResourceNotFoundException
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        // ✅ CAMBIO: ResourceNotFoundException para rol
        Rol nuevoRol = rolRepository.findById(nuevoRolId)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", nuevoRolId));

        u.setRol(nuevoRol);
        return toResponse(usuarioRepository.save(u));
    }

    private UsuarioResponseDTO toResponse(Usuario u) {
        return new UsuarioResponseDTO(
                u.getUsuId(),
                u.getUsuNombre(),
                u.getUsuCorreo(),
                u.getUsuTelefono(),
                u.getUsuActivo(),
                u.getUsuDocnum(),
                u.getUsuOrigen().name(),
                u.getRol() != null ? u.getRol().getRolId() : null,
                u.getRol() != null ? u.getRol().getRolNombre() : null,
                u.getTipoDocumento() != null ? u.getTipoDocumento().getTipdocId() : null,
                u.getTipoDocumento() != null ? u.getTipoDocumento().getTipdocNombre() : null
        );
    }

    @Transactional(readOnly = true)
    public long contarUsuariosPorActivo(boolean activo) {
        return usuarioRepository.countByUsuActivo(activo);
    }
}
