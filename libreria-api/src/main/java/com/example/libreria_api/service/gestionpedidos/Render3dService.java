package com.example.libreria_api.service.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.Render3dMapper;
import com.example.libreria_api.dto.gestionpedidos.Render3dRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.Render3dResponseDTO;
import com.example.libreria_api.model.gestionpedidos.Pedido;
import com.example.libreria_api.model.gestionpedidos.Render3d;
import com.example.libreria_api.repository.gestionpedidos.PedidoRepository;
import com.example.libreria_api.repository.gestionpedidos.Render3dRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Render3dService {

    private final Render3dRepository render3dRepository;
    private final PedidoRepository pedidoRepository;

     public Render3dService(Render3dRepository render3dRepository, PedidoRepository pedidoRepository) {
        this.render3dRepository = render3dRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public List<Render3dResponseDTO> obtenerTodosLosRenders() {
        return render3dRepository.findAll()
                .stream()
                .map(Render3dMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<Render3dResponseDTO> obtenerRenderPorId(Integer id) {
        return render3dRepository.findById(id)
                .map(Render3dMapper::toResponseDTO);
    }

    public Render3dResponseDTO guardarRender(Render3dRequestDTO requestDTO) {
         Pedido pedido = pedidoRepository.findById(requestDTO.getPed_id())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + requestDTO.getPed_id()));

         Render3d nuevoRender = Render3dMapper.toEntity(requestDTO);

         nuevoRender.setPedido(pedido);

         Render3d renderGuardado = render3dRepository.save(nuevoRender);
        return Render3dMapper.toResponseDTO(renderGuardado);
    }

    public Render3dResponseDTO actualizarRender(Integer id, Render3dRequestDTO requestDTO) {
         Render3d renderExistente = render3dRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Render no encontrado con id: " + id));

         Pedido pedido = pedidoRepository.findById(requestDTO.getPed_id())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + requestDTO.getPed_id()));

         renderExistente.setRenImagen(requestDTO.getRenImagen());
        renderExistente.setRenFechaAprobacion(requestDTO.getRenFechaAprobacion());
        renderExistente.setPedido(pedido);

        Render3d renderActualizado = render3dRepository.save(renderExistente);
        return Render3dMapper.toResponseDTO(renderActualizado);
    }

    public boolean eliminarRender(Integer id) {
        if (render3dRepository.existsById(id)) {
            render3dRepository.deleteById(id);
            return true;
        }
        return false;
    }
}