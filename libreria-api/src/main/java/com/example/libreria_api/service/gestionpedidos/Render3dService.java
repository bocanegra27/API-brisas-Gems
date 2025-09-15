package com.example.libreria_api.service.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.Render3d;
import com.example.libreria_api.repository.gestionpedidos.Render3dRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class Render3dService {

    @Autowired
    private Render3dRepository render3dRepository;

    public List<Render3d> obtenerTodosLosRenders() {
        return render3dRepository.findAll();
    }

    public Optional<Render3d> obtenerRenderPorId(Integer id) {
        return render3dRepository.findById(id);
    }

    public Render3d guardarRender(Render3d render) {
        return render3dRepository.save(render);
    }

    public Render3d actualizarRender(Integer id, Render3d renderDetalles) {
        return render3dRepository.findById(id).map(renderExistente -> {
            renderExistente.setRenImagen(renderDetalles.getRenImagen());
            renderExistente.setRenFechaAprobacion(renderDetalles.getRenFechaAprobacion());
            renderExistente.setPedido(renderDetalles.getPedido());
            return render3dRepository.save(renderExistente);
        }).orElse(null);
    }

    public boolean eliminarRender(Integer id) {
        if (render3dRepository.existsById(id)) {
            render3dRepository.deleteById(id);
            return true;
        }
        return false;
    }
}