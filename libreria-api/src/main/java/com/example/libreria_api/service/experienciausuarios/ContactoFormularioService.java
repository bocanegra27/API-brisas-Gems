package com.example.libreria_api.service.experienciausuarios;

import com.example.libreria_api.model.experienciausuarios.ContactoFormulario;
import com.example.libreria_api.repository.experienciausuarios.ContactoFormularioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactoFormularioService {

    @Autowired
    private ContactoFormularioRepository contactoFormularioRepository;


    public List<ContactoFormulario> obtenerTodos() {
        return contactoFormularioRepository.findAll();
    }


    public ContactoFormulario guardar(ContactoFormulario contacto) {
        return contactoFormularioRepository.save(contacto);
    }


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


    public void eliminar(Integer id) {
        contactoFormularioRepository.deleteById(id);
    }
}
