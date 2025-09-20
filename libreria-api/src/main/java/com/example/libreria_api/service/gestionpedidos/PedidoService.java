package com.example.libreria_api.service.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.PedidoMapper;
import com.example.libreria_api.dto.gestionpedidos.PedidoRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.PedidoResponseDTO;
import com.example.libreria_api.model.gestionpedidos.Pedido;
import com.example.libreria_api.repository.gestionpedidos.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

     public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

     public List<PedidoResponseDTO> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(PedidoMapper::toPedidoResponseDTO)
                .collect(Collectors.toList());
    }


    public PedidoResponseDTO guardarPedido(PedidoRequestDTO requestDTO) {
        Pedido nuevoPedido = PedidoMapper.toPedido(requestDTO);
        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);
        return PedidoMapper.toPedidoResponseDTO(pedidoGuardado);
    }


    public PedidoResponseDTO actualizar(Integer id, PedidoRequestDTO requestDTO) {
        return pedidoRepository.findById(id).map(pedidoExistente -> {
            // Actualizamos los campos de la entidad existente con los datos del DTO
            pedidoExistente.setPedCodigo(requestDTO.getPedCodigo());
            pedidoExistente.setPedComentarios(requestDTO.getPedComentarios());
            pedidoExistente.setEstId(requestDTO.getEstId());
            pedidoExistente.setPerId(requestDTO.getPerId());
            pedidoExistente.setUsuId(requestDTO.getUsuId());

            Pedido pedidoActualizado = pedidoRepository.save(pedidoExistente);
            return PedidoMapper.toPedidoResponseDTO(pedidoActualizado);
        }).orElse(null);
    }


    public boolean eliminarPedido(Integer id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}