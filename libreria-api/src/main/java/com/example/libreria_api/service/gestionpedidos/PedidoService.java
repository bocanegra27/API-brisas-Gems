package com.example.libreria_api.service.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.PedidoDetailResponseDTO;
import com.example.libreria_api.dto.gestionpedidos.PedidoMapper;
import com.example.libreria_api.dto.gestionpedidos.PedidoRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.PedidoResponseDTO;
import com.example.libreria_api.exception.ResourceNotFoundException;
import com.example.libreria_api.model.gestionpedidos.EstadoPedido;
import com.example.libreria_api.model.gestionpedidos.Pedido;
import com.example.libreria_api.model.personalizacionproductos.Personalizacion;
import com.example.libreria_api.model.sistemausuarios.Usuario;
import com.example.libreria_api.repository.gestionpedidos.EstadoPedidoRepository;
import com.example.libreria_api.repository.gestionpedidos.PedidoRepository;
import com.example.libreria_api.repository.personalizacionproductos.PersonalizacionRepository;
import com.example.libreria_api.repository.sistemausuarios.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoPedidoRepository estadoPedidoRepository;
    private final PersonalizacionRepository personalizacionRepository;

    public PedidoService(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository, EstadoPedidoRepository estadoPedidoRepository, PersonalizacionRepository personalizacionRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.estadoPedidoRepository = estadoPedidoRepository;
        this.personalizacionRepository = personalizacionRepository;
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll().stream().map(PedidoMapper::toPedidoResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoDetailResponseDTO obtenerPedidoPorId(Integer id) {
        // ✅ CAMBIO: Usar ResourceNotFoundException en lugar de EntityNotFoundException
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", id));

        Personalizacion personalizacion = null;
        if (pedido.getPerId() != null) {
            personalizacion = personalizacionRepository.findById(pedido.getPerId()).orElse(null);
        }

        Usuario cliente = null;
        if (personalizacion != null && personalizacion.getUsuario() != null) {
            cliente = personalizacion.getUsuario();
        }

        Usuario empleado = null;
        if (pedido.getUsuIdEmpleado() != null) {
            empleado = usuarioRepository.findById(pedido.getUsuIdEmpleado()).orElse(null);
        }

        PedidoDetailResponseDTO dto = new PedidoDetailResponseDTO();
        dto.setPed_id(pedido.getPed_id());
        dto.setPedCodigo(pedido.getPedCodigo());
        dto.setPedFechaCreacion(pedido.getPedFechaCreacion());
        dto.setPedComentarios(pedido.getPedComentarios());

        if (pedido.getEstadoPedido() != null) {
            dto.setEstId(pedido.getEstadoPedido().getEst_id());
            dto.setEstadoNombre(pedido.getEstadoPedido().getEstNombre());
        } else {
            dto.setEstadoNombre("Desconocido");
        }

        dto.setClienteNombre(cliente != null ? cliente.getUsuNombre() : "Desconocido");
        dto.setEmpleadoNombre(empleado != null ? empleado.getUsuNombre() : "No asignado");

        return dto;
    }

    @Transactional
    public PedidoResponseDTO guardarPedido(PedidoRequestDTO requestDTO) {
        try {
            Pedido nuevoPedido = PedidoMapper.toPedido(requestDTO);

            // ASIGNAR ESTADO PEDIDO CORRECTAMENTE usando el repository
            if (requestDTO.getEstId() != null) {
                EstadoPedido estado = estadoPedidoRepository.findById(requestDTO.getEstId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "EstadoPedido no encontrado con ID: " + requestDTO.getEstId()));
                nuevoPedido.setEstadoPedido(estado);
            }

            // Generar código automático si no viene
            if (nuevoPedido.getPedCodigo() == null || nuevoPedido.getPedCodigo().trim().isEmpty()) {
                nuevoPedido.setPedCodigo("PED-" + System.currentTimeMillis());
            }

            Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);
            return PedidoMapper.toPedidoResponseDTO(pedidoGuardado);

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar pedido: " + e.getMessage(), e);
        }
    }

    @Transactional
    public PedidoResponseDTO actualizar(Integer id, PedidoRequestDTO requestDTO) {
        return pedidoRepository.findById(id).map(pedidoExistente -> {
            try {
                // Usar el nuevo método del mapper para actualizar
                PedidoMapper.updatePedidoFromDTO(pedidoExistente, requestDTO);

                // ACTUALIZAR ESTADO si viene en el DTO
                if (requestDTO.getEstId() != null) {
                    EstadoPedido nuevoEstado = estadoPedidoRepository.findById(requestDTO.getEstId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "EstadoPedido no encontrado con ID: " + requestDTO.getEstId()));
                    pedidoExistente.setEstadoPedido(nuevoEstado);
                }

                Pedido pedidoActualizado = pedidoRepository.save(pedidoExistente);
                return PedidoMapper.toPedidoResponseDTO(pedidoActualizado);

            } catch (Exception e) {
                throw new RuntimeException("Error al actualizar pedido: " + e.getMessage(), e);
            }
        }).orElse(null);
    }

    @Transactional
    public boolean eliminarPedido(Integer id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public long contarPedidosPorEstado(String nombreEstado) {
        return pedidoRepository.countByEstadoPedido_EstNombre(nombreEstado);
    }
}