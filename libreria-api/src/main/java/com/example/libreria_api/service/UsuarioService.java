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
            // Actualizamos todos los campos excepto la contraseña
            usuarioExistente.setUsuNombre(usuarioDetalles.getUsuNombre());
            usuarioExistente.setUsuCorreo(usuarioDetalles.getUsuCorreo());
            usuarioExistente.setUsuTelefono(usuarioDetalles.getUsuTelefono());
            usuarioExistente.setUsuDocnum(usuarioDetalles.getUsuDocnum());
            usuarioExistente.setUsuOrigen(usuarioDetalles.getUsuOrigen());
            usuarioExistente.setUsuActivo(usuarioDetalles.isUsuActivo());
            usuarioExistente.setTipoDeDocumento(usuarioDetalles.getTipoDeDocumento());
            usuarioExistente.setRol(usuarioDetalles.getRol());

            return usuarioRepository.save(usuarioExistente);
        }).orElse(null);
    }

    public boolean eliminarUsuario(Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id); 
            return true; 
        }
        return false; 
    }
}
