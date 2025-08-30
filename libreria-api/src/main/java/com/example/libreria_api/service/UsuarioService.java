package com.example.libreria_api.service;

import com.example.libreria_api.model.Usuario;
import com.example.libreria_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- 1. IMPORTANTE: Añadir este import
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 2. INYECCIÓN DE DEPENDENCIA:
    // Spring ve esto y nos "inyecta" el Bean de PasswordEncoder que creamos en SecurityConfig.
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Guarda un nuevo usuario y encripta su contraseña ANTES de almacenarla en la BD.
     */
    public Usuario guardarUsuario(Usuario usuario) {
        // 3. LÓGICA DE ENCRIPTACIÓN:
        // Obtenemos la contraseña en texto plano que viene del JSON (ej: "contraseña123").
        String passwordPlano = usuario.getUsuPassword();
        // La encriptamos usando nuestro PasswordEncoder. El resultado será algo como "$2a$10$..."
        String passwordEncriptado = passwordEncoder.encode(passwordPlano);
        // Reemplazamos la contraseña en texto plano por la versión ya encriptada.
        usuario.setUsuPassword(passwordEncriptado);

        // Finalmente, guardamos el objeto Usuario en la base de datos.
        // La contraseña que se guarda ya es segura.
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> obtenerUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Usuario actualizarUsuario(Integer id, Usuario usuarioDetalles) {
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setUsuNombre(usuarioDetalles.getUsuNombre());
            usuarioExistente.setUsuCorreo(usuarioDetalles.getUsuCorreo());
            usuarioExistente.setUsuTelefono(usuarioDetalles.getUsuTelefono());
            usuarioExistente.setUsuDocnum(usuarioDetalles.getUsuDocnum());
            usuarioExistente.setUsuOrigen(usuarioDetalles.getUsuOrigen());
            usuarioExistente.setUsuActivo(usuarioDetalles.isUsuActivo());
            usuarioExistente.setTipoDeDocumento(usuarioDetalles.getTipoDeDocumento());
            usuarioExistente.setRol(usuarioDetalles.getRol());
            // Nota importante: No actualizamos la contraseña en este método general.
            // La actualización de contraseña se suele hacer en un endpoint dedicado y seguro.
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

