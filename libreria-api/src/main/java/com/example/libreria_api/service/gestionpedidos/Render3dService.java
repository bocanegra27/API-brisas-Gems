package com.example.libreria_api.service.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.Render3dMapper;
import com.example.libreria_api.dto.gestionpedidos.Render3dResponseDTO;
import com.example.libreria_api.model.gestionpedidos.Pedido;
import com.example.libreria_api.model.gestionpedidos.Render3d;
import com.example.libreria_api.repository.gestionpedidos.Render3dRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Render3dService {
    private final Render3dRepository render3dRepository;

    public Render3dService(Render3dRepository render3dRepository) {
        this.render3dRepository = render3dRepository;
    }

    // Método para la Fase 2: Registrar nuevo render desde PedidoService
    @Transactional
    public void registrarNuevoRender(Pedido pedido, String rutaImagen) {
        Render3d render = new Render3d();
        render.setPedido(pedido);
        render.setRenImagen(rutaImagen);
        render.setRenFechaDimension(LocalDate.now());
        render3dRepository.save(render);
    }

    // Métodos para el Controller
    public List<Render3dResponseDTO> obtenerTodosLosRenders() {
        return render3dRepository.findAll().stream()
                .map(Render3dMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<Render3dResponseDTO> obtenerRenderPorId(Integer id) {
        return render3dRepository.findById(id)
                .map(Render3dMapper::toResponseDTO);
    }

    public boolean eliminarRender(Integer id) {
        if (render3dRepository.existsById(id)) {
            render3dRepository.deleteById(id);
            return true;
        }
        return false;
    }
}