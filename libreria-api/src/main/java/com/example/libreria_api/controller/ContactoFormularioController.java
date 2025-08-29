package com.example.libreria_api.controller;

import com.example.libreria_api.model.ContactoFormulario;
import com.example.libreria_api.service.ContactoFormularioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ContactoFormularioController {

    @Autowired
    private ContactoFormularioService contactoFormularioService;

    @GetMapping("/contacto")
    public List<ContactoFormulario> obtenerTodos() {
        return contactoFormularioService.obtenerTodos();
    }


    @PostMapping("/contacto")
    public ContactoFormulario crear(@RequestBody ContactoFormulario contacto) {
        return contactoFormularioService.guardar(contacto);
    }


    @PutMapping("/contacto/{id}")
    public ContactoFormulario actualizar(@PathVariable Integer id, @RequestBody ContactoFormulario detalles) {
        return contactoFormularioService.actualizar(id, detalles);
    }

    @DeleteMapping("/contacto/{id}")
    public void eliminar(@PathVariable Integer id) {
        contactoFormularioService.eliminar(id);
    }
}