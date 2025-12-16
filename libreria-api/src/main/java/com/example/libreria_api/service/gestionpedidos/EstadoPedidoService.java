package com.example.libreria_api.service.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.EstadoPedidoMapper;
import com.example.libreria_api.dto.gestionpedidos.EstadoPedidoRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.EstadoPedidoResponseDTO;
import com.example.libreria_api.model.gestionpedidos.EstadoPedido;
import com.example.libreria_api.repository.gestionpedidos.EstadoPedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstadoPedidoService {

    private final EstadoPedidoRepository estadoPedidoRepository;

    public EstadoPedidoService(EstadoPedidoRepository estadoPedidoRepository) {
        this.estadoPedidoRepository = estadoPedidoRepository;
    }

    @Transactional(readOnly = true)
    public List<EstadoPedidoResponseDTO> obtenerTodos() {
        return estadoPedidoRepository.findAll()
                .stream()
                .map(EstadoPedidoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<EstadoPedidoResponseDTO> obtenerPorId(Integer id) {
        return estadoPedidoRepository.findById(id)
                .map(EstadoPedidoMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Optional<EstadoPedidoResponseDTO> obtenerPorNombre(String nombre) {
        return estadoPedidoRepository.findByEstNombre(nombre)
                .map(EstadoPedidoMapper::toResponseDTO);
    }

    @Transactional
    public EstadoPedidoResponseDTO guardar(EstadoPedidoRequestDTO requestDTO) {

        if (estadoPedidoRepository.existsByEstNombre(requestDTO.getEstNombre())) {
            throw new IllegalArgumentException("Ya existe un estado con el nombre: " + requestDTO.getEstNombre());
        }

        EstadoPedido nuevoEstado = EstadoPedidoMapper.toEntity(requestDTO);
        EstadoPedido estadoGuardado = estadoPedidoRepository.save(nuevoEstado);
        return EstadoPedidoMapper.toResponseDTO(estadoGuardado);
    }

    @Transactional
    public EstadoPedidoResponseDTO actualizar(Integer id, EstadoPedidoRequestDTO requestDTO) {
        return estadoPedidoRepository.findById(id).map(estadoExistente -> {

            if (!estadoExistente.getEstNombre().equals(requestDTO.getEstNombre()) &&
                    estadoPedidoRepository.existsByEstNombre(requestDTO.getEstNombre())) {
                throw new IllegalArgumentException("Ya existe un estado con el nombre: " + requestDTO.getEstNombre());
            }

            estadoExistente.setEstNombre(requestDTO.getEstNombre());
            if (requestDTO.getEstDescripcion() != null) {
                estadoExistente.setEstDescripcion(requestDTO.getEstDescripcion());
            }

            EstadoPedido estadoActualizado = estadoPedidoRepository.save(estadoExistente);
            return EstadoPedidoMapper.toResponseDTO(estadoActualizado);
        }).orElse(null);
    }

    @Transactional
    public boolean eliminar(Integer id) {
        if (!estadoPedidoRepository.existsById(id)) {
            return false;
        }


        Long countPedidos = estadoPedidoRepository.countPedidosByEstadoId(id);
        if (countPedidos > 0) {
            throw new IllegalStateException(
                    "No se puede eliminar el estado porque tiene " + countPedidos + " pedidos asociados");
        }

        estadoPedidoRepository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombre) {
        return estadoPedidoRepository.existsByEstNombre(nombre);
    }

    @Transactional(readOnly = true)
    public EstadoPedido obtenerEntidadPorId(Integer id) {
        return estadoPedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EstadoPedido no encontrado con ID: " + id));
    }
}