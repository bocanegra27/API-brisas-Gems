package com.example.libreria_api.service;

import com.example.libreria_api.model.ValorPersonalizacion;
import com.example.libreria_api.repository.ValorPersonalizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValorPersonalizacionService {

    @Autowired
    private ValorPersonalizacionRepository valorPersonalizacionRepository;

    // --- GET: Obtener todos ---
    public List<ValorPersonalizacion> obtenerTodos() {
        return valorPersonalizacionRepository.findAll();
    }

    // --- POST: Crear uno nuevo ---
    public ValorPersonalizacion guardar(ValorPersonalizacion valorPersonalizacion) {
        return valorPersonalizacionRepository.save(valorPersonalizacion);
    }

    // --- GET: Obtener por ID ---
    public Optional<ValorPersonalizacion> obtenerPorId(Integer id) {
        return valorPersonalizacionRepository.findById(id);
    }

    // --- PUT: Actualizar ---
    public ValorPersonalizacion actualizar(Integer id, ValorPersonalizacion detalles) {
        return valorPersonalizacionRepository.findById(id).map(existente -> {
            existente.setValNombre(detalles.getValNombre());
            existente.setValImagen(detalles.getValImagen());
            existente.setOpcionPersonalizacion(detalles.getOpcionPersonalizacion());
            return valorPersonalizacionRepository.save(existente);
        }).orElse(null);
    }

    // --- DELETE: Eliminar ---
    public boolean eliminar(Integer id) {
        return valorPersonalizacionRepository.findById(id).map(existente -> {
            valorPersonalizacionRepository.delete(existente);
            return true;
        }).orElse(false);
    }
}
