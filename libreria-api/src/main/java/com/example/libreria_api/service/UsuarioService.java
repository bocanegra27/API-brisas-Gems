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


    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }


    public Usuario guardarUsuario(Usuario usuario) {

        String passwordPlano = usuario.getUsuPassword();

        String passwordEncriptado = passwordEncoder.encode(passwordPlano);

        usuario.setUsuPassword(passwordEncriptado);



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

