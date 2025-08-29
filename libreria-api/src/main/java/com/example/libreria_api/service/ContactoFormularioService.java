package com.example.libreria_api.service;

import com.example.libreria_api.model.ContactoFormulario;
import com.example.libreria_api.repository.ContactoFormularioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactoFormularioService {

    @Autowired
    private ContactoFormularioRepository contactoFormularioRepository;

    // Obtener todos los contactos
    public List<ContactoFormulario> obtenerTodos() {
        return contactoFormularioRepository.findAll();
    }

    // Guardar nuevo contacto
    public ContactoFormulario guardar(ContactoFormulario contacto) {
        return contactoFormularioRepository.save(contacto);
    }

    // Actualizar contacto existente
    public ContactoFormulario actualizar(Integer id, ContactoFormulario detalles) {
        return contactoFormularioRepository.findById(id).map(contactoExistente -> {
            contactoExistente.setConNombre(detalles.getConNombre());
            contactoExistente.setConEmail(detalles.getConEmail());
            contactoExistente.setConTelefono(detalles.getConTelefono());
            contactoExistente.setConMensaje(detalles.getConMensaje());
            contactoExistente.setConVia(detalles.getConVia());
            contactoExistente.setConTerminos(detalles.isConTerminos());
            contactoExistente.setUsuario(detalles.getUsuario());
            return contactoFormularioRepository.save(contactoExistente);
        }).orElse(null);
    }

    // Eliminar contacto
    public void eliminar(Integer id) {
        contactoFormularioRepository.deleteById(id);
    }
}
