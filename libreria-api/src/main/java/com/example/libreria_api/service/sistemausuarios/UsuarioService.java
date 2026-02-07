package com.example.libreria_api.service.sistemausuarios;

import com.example.libreria_api.dto.sistemausuarios.*;
import com.example.libreria_api.exception.BadRequestException;
import com.example.libreria_api.exception.DuplicateResourceException;
import com.example.libreria_api.exception.ResourceNotFoundException;
import com.example.libreria_api.model.sistemausuarios.*;
import com.example.libreria_api.repository.sistemausuarios.RolRepository;
import com.example.libreria_api.repository.sistemausuarios.TipoDeDocumentoRepository;
import com.example.libreria_api.repository.sistemausuarios.UsuarioRepository;
import com.example.libreria_api.repository.sistemausuarios.SesionAnonimaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final TipoDeDocumentoRepository tipoDocumentoRepository;
    private final PasswordEncoder passwordEncoder;
    private final SesionAnonimaRepository sesionAnonimaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          TipoDeDocumentoRepository tipoDocumentoRepository,
                          PasswordEncoder passwordEncoder,
                          SesionAnonimaRepository sesionAnonimaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.passwordEncoder = passwordEncoder;
        this.sesionAnonimaRepository = sesionAnonimaRepository;
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> listarUsuarios(Integer rolId, Boolean activo, Pageable pageable) {
        Page<Usuario> pagina;

        // Si se especifican rolId y activo
        if (rolId != null && activo != null) {
            pagina = usuarioRepository.findByRol_RolIdAndUsuActivo(rolId, activo, pageable);
        }
        // Si solo se especifica rolId (asumimos activo=true si no se indica el filtro 'activo')
        // OJO: Si quieres listar todos los de ese rol (activos e inactivos), deberías
        // crear un método findByRol_RolId en el repository.
        else if (rolId != null) {
            // Asumiendo que SIEMPRE quieres ver solo los ACTIVOS de un rol, si no se especifica 'activo'
            pagina = usuarioRepository.findByRol_RolIdAndUsuActivo(rolId, true, pageable);
        }
        // Si solo se especifica activo
        else if (activo != null) {
            pagina = usuarioRepository.findByUsuActivo(activo, pageable);
        }
        // Si no se especifica ningún filtro (rolId == null AND activo == null)
        else {
            // CAMBIO A REALIZAR: Llamar a findAll() para obtener activos e inactivos
            pagina = usuarioRepository.findAll(pageable); // <--- CAMBIO CLAVE
        }

        return pagina.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Integer id) {

        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        return toResponse(u);
    }

    @Transactional
    public UsuarioResponseDTO crear(UsuarioCreateDTO dto) {
        if (usuarioRepository.existsByUsuCorreo(dto.getCorreo())) {
            throw new DuplicateResourceException("Usuario", "correo", dto.getCorreo());
        }


        if (dto.getDocnum() != null && usuarioRepository.existsByUsuDocnum(dto.getDocnum())) {
            throw new DuplicateResourceException("Usuario", "documento", dto.getDocnum());
        }


        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", dto.getRolId()));

        TipoDeDocumento tip = null;
        if (dto.getTipdocId() != null) {

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

        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));


        if (dto.getCorreo() != null && !dto.getCorreo().equalsIgnoreCase(u.getUsuCorreo())) {
            if (usuarioRepository.existsByUsuCorreo(dto.getCorreo())) {
                throw new DuplicateResourceException("Usuario", "correo", dto.getCorreo());
            }
            u.setUsuCorreo(dto.getCorreo());
        }


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

            TipoDeDocumento tip = tipoDocumentoRepository.findById(dto.getTipdocId())
                    .orElseThrow(() -> new ResourceNotFoundException("TipoDeDocumento", "id", dto.getTipdocId()));
            u.setTipoDocumento(tip);
        }

        return toResponse(usuarioRepository.save(u));
    }

    @Transactional
    public void eliminar(Integer id) {
        // La comprobación de existencia antes de eliminar es buena práctica
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", "id", id);
        }


        usuarioRepository.deleteById(id);


    }
    @Transactional
    public void actualizarPassword(Integer id, PasswordUpdateDTO dto) {

        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));


        if (!passwordEncoder.matches(dto.getPasswordActual(), u.getUsuPassword())) {
            throw new BadRequestException("La contraseña actual no es correcta");
        }

        u.setUsuPassword(passwordEncoder.encode(dto.getPasswordNueva()));
        usuarioRepository.save(u);
    }

    @Transactional
    public UsuarioResponseDTO actualizarRol(Integer id, Integer nuevoRolId) {

        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));


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


    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obtenerUsuariosPorRoles(List<Integer> rolIds) {

        return usuarioRepository.findByRol_RolIdInAndUsuActivo(rolIds, true)
                .stream()
                .map(this::toResponse) // Reutiliza tu método de mapeo existente
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public UsuarioResponseDTO crearYConvertir(UsuarioCreateDTO dto, String sesionToken) {
        // 1. Validaciones base (correo, documento)
        if (usuarioRepository.existsByUsuCorreo(dto.getCorreo())) {
            throw new DuplicateResourceException("Usuario", "correo", dto.getCorreo());
        }
        if (dto.getDocnum() != null && usuarioRepository.existsByUsuDocnum(dto.getDocnum())) {
            throw new DuplicateResourceException("Usuario", "documento", dto.getDocnum());
        }

        // 2. Buscar Sesión Anónima
        SesionAnonima sesion = sesionAnonimaRepository.findBySesToken(sesionToken) // Usa el método que tienes en tu SesionAnonimaRepository
                .orElseThrow(() -> new ResourceNotFoundException("Sesión Anónima", "token", sesionToken));

        if (sesion.getSesConvertido()) {
            throw new BadRequestException("La sesión ya fue convertida a usuario.");
        }

        // 3. Crear el nuevo Usuario (Usa la misma lógica que tu método crear)
        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", dto.getRolId()));
        TipoDeDocumento tip = (dto.getTipdocId() != null) ?
                tipoDocumentoRepository.findById(dto.getTipdocId())
                        .orElseThrow(() -> new ResourceNotFoundException("TipoDeDocumento", "id", dto.getTipdocId())) : null;

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsuNombre(dto.getNombre());
        nuevoUsuario.setUsuCorreo(dto.getCorreo());
        nuevoUsuario.setUsuTelefono(dto.getTelefono());
        nuevoUsuario.setUsuPassword(passwordEncoder.encode(dto.getPassword()));
        nuevoUsuario.setUsuDocnum(dto.getDocnum());
        nuevoUsuario.setUsuOrigen(OrigenUsuario.registro);
        nuevoUsuario.setUsuActivo(true);
        nuevoUsuario.setRol(rol);
        nuevoUsuario.setTipoDocumento(tip);

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        sesion.setSesConvertido(true);
        sesion.setUsuarioConvertido(usuarioGuardado); // Vínculo con el nuevo usuario
        sesionAnonimaRepository.save(sesion);


        return toResponse(usuarioGuardado);
    }

}
