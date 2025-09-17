package com.example.libreria_api.service.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.EstadoPedidoMapper;
import com.example.libreria_api.dto.gestionpedidos.EstadoPedidoRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.EstadoPedidoResponseDTO;
import com.example.libreria_api.model.gestionpedidos.EstadoPedido;
import com.example.libreria_api.repository.gestionpedidos.EstadoPedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoPedidoService {

    private final EstadoPedidoRepository estadoPedidoRepository;

    // Inyección de dependencias por constructor
    public EstadoPedidoService(EstadoPedidoRepository estadoPedidoRepository) {
        this.estadoPedidoRepository = estadoPedidoRepository;
    }

    public List<EstadoPedidoResponseDTO> obtenerTodos() {
        return estadoPedidoRepository.findAll()
                .stream()
                .map(EstadoPedidoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public EstadoPedidoResponseDTO guardar(EstadoPedidoRequestDTO requestDTO) {
        EstadoPedido nuevoEstado = EstadoPedidoMapper.toEntity(requestDTO);
        EstadoPedido estadoGuardado = estadoPedidoRepository.save(nuevoEstado);
        return EstadoPedidoMapper.toResponseDTO(estadoGuardado);
    }

    public EstadoPedidoResponseDTO actualizar(Integer id, EstadoPedidoRequestDTO requestDTO) {
        return estadoPedidoRepository.findById(id).map(estadoExistente -> {
            estadoExistente.setEstNombre(requestDTO.getEstNombre());
            EstadoPedido estadoActualizado = estadoPedidoRepository.save(estadoExistente);
            return EstadoPedidoMapper.toResponseDTO(estadoActualizado);
        }).orElse(null);
    }

    public void eliminar(Integer id) {
        estadoPedidoRepository.deleteById(id);
    }
}