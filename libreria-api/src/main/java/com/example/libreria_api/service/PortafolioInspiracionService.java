package com.example.libreria_api.service;

import com.example.libreria_api.model.PortafolioInspiracion;
import com.example.libreria_api.repository.PortafolioInspiracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortafolioInspiracionService {

    @Autowired
    private PortafolioInspiracionRepository portafolioInspiracionRepository;

    public List<PortafolioInspiracion> obtenerPortafolio() {
        return portafolioInspiracionRepository.findAll();
    }


    public PortafolioInspiracion guardarPortafolio(PortafolioInspiracion portafolio) {
        return portafolioInspiracionRepository.save(portafolio);
    }

    public PortafolioInspiracion actualizar(Integer id, PortafolioInspiracion portafolioInspiracion) {
        return portafolioInspiracionRepository.findById(id).map(portafolioExistente -> {
            portafolioExistente.setPorTitulo(portafolioInspiracion.getPorTitulo());
            portafolioExistente.setPorDescripcion(portafolioInspiracion.getPorDescripcion());
            portafolioExistente.setPorImagen(portafolioInspiracion.getPorImagen());
            portafolioExistente.setPorVideo(portafolioInspiracion.getPorVideo());
            portafolioExistente.setPorCategoria(portafolioInspiracion.getPorCategoria());
            portafolioExistente.setPorFecha(portafolioInspiracion.getPorFecha());



            return portafolioInspiracionRepository.save(portafolioExistente);
        }).orElse(null);
    }

    public void eliminarPortafolio (Integer id){
        portafolioInspiracionRepository.deleteById(id);
    }

}

