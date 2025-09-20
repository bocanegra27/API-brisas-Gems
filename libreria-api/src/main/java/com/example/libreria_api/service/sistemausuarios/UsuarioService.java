package com.example.libreria_api.service.sistemausuarios;

import com.example.libreria_api.model.sistemausuarios.Rol;
import com.example.libreria_api.model.sistemausuarios.TipoDeDocumento;
import com.example.libreria_api.model.sistemausuarios.Usuario;
import com.example.libreria_api.repository.sistemausuarios.RolRepository;
import com.example.libreria_api.repository.sistemausuarios.TipoDeDocumentoRepository;
import com.example.libreria_api.repository.sistemausuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private TipoDeDocumentoRepository tipoDeDocumentoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario guardarUsuario(Usuario usuario) {
        // Encripta la contraseña
        String passwordPlano = usuario.getUsuPassword();
        String passwordEncriptado = passwordEncoder.encode(passwordPlano);
        usuario.setUsuPassword(passwordEncriptado);

        // Busca el Rol real en la BD usando el ID que viene en el objeto 'usuario'
        Rol rol = rolRepository.findById(usuario.getRol().getRol_id())
                .orElseThrow(() -> new RuntimeException("Error: No se encontró el Rol con ID " + usuario.getRol().getRol_id()));

        // Busca el TipoDeDocumento real en la BD
        TipoDeDocumento tipoDoc = tipoDeDocumentoRepository.findById(usuario.getTipoDeDocumento().getTipdoc_id())
                .orElseThrow(() -> new RuntimeException("Error: No se encontró el Tipo de Documento con ID " + usuario.getTipoDeDocumento().getTipdoc_id()));

        // Asigna las entidades persistentes (reales) al usuario antes de guardarlo
        usuario.setRol(rol);
        usuario.setTipoDeDocumento(tipoDoc);

        // Guarda el usuario con las referencias correctas
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> obtenerUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Usuario actualizarUsuario(Integer id, Usuario usuarioDetalles) {
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            // Busca las entidades relacionadas, igual que en el método de guardar
            Rol rol = rolRepository.findById(usuarioDetalles.getRol().getRol_id())
                    .orElseThrow(() -> new RuntimeException("Error: No se encontró el Rol con ID " + usuarioDetalles.getRol().getRol_id()));

            TipoDeDocumento tipoDoc = tipoDeDocumentoRepository.findById(usuarioDetalles.getTipoDeDocumento().getTipdoc_id())
                    .orElseThrow(() -> new RuntimeException("Error: No se encontró el Tipo de Documento con ID " + usuarioDetalles.getTipoDeDocumento().getTipdoc_id()));

            usuarioExistente.setUsuNombre(usuarioDetalles.getUsuNombre());
            usuarioExistente.setUsuCorreo(usuarioDetalles.getUsuCorreo());
            usuarioExistente.setUsuTelefono(usuarioDetalles.getUsuTelefono());
            usuarioExistente.setUsuDocnum(usuarioDetalles.getUsuDocnum());
            usuarioExistente.setUsuOrigen(usuarioDetalles.getUsuOrigen());
            usuarioExistente.setUsuActivo(usuarioDetalles.isUsuActivo());

            // Asigna las entidades reales
            usuarioExistente.setTipoDeDocumento(tipoDoc);
            usuarioExistente.setRol(rol);

            return usuarioRepository.save(usuarioExistente);
        }).orElse(null);
    }

    public boolean eliminarUsuario(Integer id) {
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setUsuActivo(false);
            usuarioRepository.save(usuarioExistente);
            return true;
        }).orElse(false);
    }
}