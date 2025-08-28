package com.example.libreria_api.service;

import com.example.libreria_api.model.Usuario;
import com.example.libreria_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario guardarUsuario(Usuario usuario) {
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

            return usuarioRepository.save(usuarioExistente);
        }).orElse(null);
    }

    public void eliminarUsuario (Integer id) {
        usuarioRepository.deleteById(id);
    }

}