package com.example.libreria_api.service.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.PedidoDetailResponseDTO;
import com.example.libreria_api.dto.gestionpedidos.PedidoMapper;
import com.example.libreria_api.dto.gestionpedidos.PedidoRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.PedidoResponseDTO;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoPedidoRepository estadoPedidoRepository;
    private final PersonalizacionRepository personalizacionRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         UsuarioRepository usuarioRepository,
                         EstadoPedidoRepository estadoPedidoRepository,
                         PersonalizacionRepository personalizacionRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.estadoPedidoRepository = estadoPedidoRepository;
        this.personalizacionRepository = personalizacionRepository;
    }

    public List<PedidoResponseDTO> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(PedidoMapper::toPedidoResponseDTO)
                .collect(Collectors.toList());
    }

    // --- MÉTODO CORREGIDO ---
    public PedidoDetailResponseDTO obtenerPedidoPorId(Integer id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + id));

        // Declaramos las variables para los objetos relacionados
        EstadoPedido estado = null;
        Personalizacion personalizacion = null;
        Usuario cliente = null;
        Usuario empleado = null;

        // Buscamos el estado SOLO SI el ID no es nulo
        if (pedido.getEstId() != null) {
            estado = estadoPedidoRepository.findById(pedido.getEstId()).orElse(null);
        }

        // Buscamos la personalización SOLO SI el ID no es nulo
        if (pedido.getPerId() != null) {
            personalizacion = personalizacionRepository.findById(pedido.getPerId()).orElse(null);
            if (personalizacion != null && personalizacion.getUsuario() != null) {
                cliente = personalizacion.getUsuario();
            }
        }

        // Buscamos el empleado SOLO SI el ID no es nulo
        if (pedido.getUsuId() != null) {
            empleado = usuarioRepository.findById(pedido.getUsuId()).orElse(null);
        }

        // Construimos el DTO de respuesta detallado
        PedidoDetailResponseDTO dto = new PedidoDetailResponseDTO();
        dto.setPed_id(pedido.getPed_id());
        dto.setPedCodigo(pedido.getPedCodigo());
        dto.setPedFechaCreacion(pedido.getPedFechaCreacion());
        dto.setPedComentarios(pedido.getPedComentarios());

        dto.setEstadoNombre(estado != null ? estado.getEstNombre() : "Desconocido");
        dto.setClienteNombre(cliente != null ? cliente.getUsuNombre() : "Desconocido");
        dto.setEmpleadoNombre(empleado != null ? empleado.getUsuNombre() : "No asignado");

        return dto;
    }

    public PedidoResponseDTO guardarPedido(PedidoRequestDTO requestDTO) {
        Pedido nuevoPedido = PedidoMapper.toPedido(requestDTO);
        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);
        return PedidoMapper.toPedidoResponseDTO(pedidoGuardado);
    }

    public PedidoResponseDTO actualizar(Integer id, PedidoRequestDTO requestDTO) {
        return pedidoRepository.findById(id).map(pedidoExistente -> {
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