package com.example.libreria_api.dto.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.Pedido;
import java.util.Date;

public class PedidoMapper {

    public static PedidoResponseDTO toPedidoResponseDTO(Pedido pedido) {
        if (pedido == null) return null;

        PedidoResponseDTO dto = new PedidoResponseDTO();
        // 🟢 CORRECCIÓN DE NOMBRE: Usamos setPedId, ya que el DTO usa pedId (Resuelve 'setPedId')
        dto.setPedId(pedido.getPed_id());

        dto.setPedCodigo(pedido.getPedCodigo());
        dto.setPedFechaCreacion(pedido.getPedFechaCreacion());
        dto.setPedComentarios(pedido.getPedComentarios());

        if (pedido.getEstadoPedido() != null) {
            dto.setEstId(pedido.getEstadoPedido().getEst_id());
            dto.setEstadoNombre(pedido.getEstadoPedido().getEstNombre());
        }

        return dto;
    }

    public static Pedido toPedido(PedidoRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Pedido pedido = new Pedido();
        // El código se genera en el Servicio.

        pedido.setPedComentarios(dto.getPedComentarios());
        pedido.setPedFechaCreacion(new Date());
        pedido.setPerId(dto.getPerId());

        // Mapeamos el ID de usuario al empleado
        pedido.setUsuIdEmpleado(dto.getUsuId());

        return pedido;
    }

    public static void updatePedidoFromDTO(Pedido pedido, PedidoRequestDTO dto) {
        if (dto == null) return;

        if (dto.getPedComentarios() != null) {
            pedido.setPedComentarios(dto.getPedComentarios());
        }
        if (dto.getPerId() != null) {
            pedido.setPerId(dto.getPerId());
        }
        if (dto.getUsuId() != null) {
            pedido.setUsuIdEmpleado(dto.getUsuId());
        }
    }
}