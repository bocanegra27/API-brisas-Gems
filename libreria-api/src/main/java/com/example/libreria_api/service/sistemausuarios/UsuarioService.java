package com.example.libreria_api.service.sistemausuarios;

import com.example.libreria_api.dto.sistemausuarios.*;
import com.example.libreria_api.model.sistemausuarios.*;
import com.example.libreria_api.repository.sistemausuarios.UsuarioRepository;
import com.example.libreria_api.repository.sistemausuarios.RolRepository;
import com.example.libreria_api.repository.sistemausuarios.TipoDeDocumentoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    // ==============================
    // LISTAR con paginación y filtros
    // ==============================
    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> listarUsuarios(Integer rolId, Boolean activo, Pageable pageable) {
        Page<Usuario> pagina;

        if (rolId != null && activo != null) {
            pagina = usuarioRepository.findByRol_RolIdAndUsuActivo(rolId, activo, pageable);
        } else if (rolId != null) {
            pagina = usuarioRepository.findByRol_RolId(rolId, pageable);
        } else if (activo != null) {
            pagina = usuarioRepository.findByUsuActivo(activo, pageable);
        } else {
            pagina = usuarioRepository.findAll(pageable);
        }

        return pagina.map(this::toResponse);
    }

    // ==============================
    // OBTENER POR ID
    // ==============================
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Integer id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + id));
        return toResponse(u);
    }

    // ==============================
    // CREAR
    // ==============================
    @Transactional
    public UsuarioResponseDTO crear(UsuarioCreateDTO dto) {
        if (usuarioRepository.existsByUsuCorreo(dto.getCorreo())) {
            throw new DataIntegrityViolationException("Correo ya registrado");
        }
        if (dto.getDocnum() != null && usuarioRepository.existsByUsuDocnum(dto.getDocnum())) {
            throw new DataIntegrityViolationException("Documento ya registrado");
        }

        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + dto.getRolId()));

        TipoDeDocumento tip = null;
        if (dto.getTipdocId() != null) {
            tip = tipoDocumentoRepository.findById(dto.getTipdocId())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo de documento no encontrado: " + dto.getTipdocId()));
        }

        Usuario u = new Usuario();
        u.setUsuNombre(dto.getNombre());
        u.setUsuCorreo(dto.getCorreo());
        u.setUsuTelefono(dto.getTelefono());
        u.setUsuPassword(passwordEncoder.encode(dto.getPassword())); // 🔐 Hash
        u.setUsuDocnum(dto.getDocnum());
        u.setUsuOrigen(dto.getOrigen() != null ? OrigenUsuario.valueOf(dto.getOrigen()) : OrigenUsuario.formulario);
        u.setUsuActivo(dto.getActivo() != null ? dto.getActivo() : false);
        u.setRol(rol);
        u.setTipoDocumento(tip);

        return toResponse(usuarioRepository.save(u));
    }

    // ==============================
    // ACTUALIZAR
    // ==============================
    @Transactional
    public UsuarioResponseDTO actualizar(Integer id, UsuarioUpdateDTO dto) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + id));

        if (dto.getCorreo() != null && !dto.getCorreo().equalsIgnoreCase(u.getUsuCorreo())) {
            if (usuarioRepository.existsByUsuCorreo(dto.getCorreo())) {
                throw new DataIntegrityViolationException("Correo ya registrado");
            }
            u.setUsuCorreo(dto.getCorreo());
        }

        if (dto.getDocnum() != null && !dto.getDocnum().equals(u.getUsuDocnum())) {
            if (usuarioRepository.existsByUsuDocnum(dto.getDocnum())) {
                throw new DataIntegrityViolationException("Documento ya registrado");
            }
            u.setUsuDocnum(dto.getDocnum());
        }

        if (dto.getNombre() != null) u.setUsuNombre(dto.getNombre());
        if (dto.getTelefono() != null) u.setUsuTelefono(dto.getTelefono());
        if (dto.getActivo() != null) u.setUsuActivo(dto.getActivo());
        if (dto.getOrigen() != null) u.setUsuOrigen(OrigenUsuario.valueOf(dto.getOrigen()));

        if (dto.getRolId() != null) {
            Rol rol = rolRepository.findById(dto.getRolId())
                    .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + dto.getRolId()));
            u.setRol(rol);
        }

        if (dto.getTipdocId() != null) {
            TipoDeDocumento tip = tipoDocumentoRepository.findById(dto.getTipdocId())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo de documento no encontrado: " + dto.getTipdocId()));
            u.setTipoDocumento(tip);
        }

        return toResponse(usuarioRepository.save(u));
    }

    // ==============================
    // ELIMINAR
    // ==============================
    @Transactional
    public void eliminar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // ==============================
    // CAMBIO DE PASSWORD
    // ==============================
    @Transactional
    public void actualizarPassword(Integer id, PasswordUpdateDTO dto) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + id));

        if (!passwordEncoder.matches(dto.getPasswordActual(), u.getUsuPassword())) {
            throw new IllegalArgumentException("La contraseña actual no es correcta");
        }

        u.setUsuPassword(passwordEncoder.encode(dto.getPasswordNueva()));
        usuarioRepository.save(u);
    }

    // ==============================
    // Mapper
    // ==============================
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
}
