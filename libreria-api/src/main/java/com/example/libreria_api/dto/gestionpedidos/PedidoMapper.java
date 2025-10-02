package com.example.libreria_api.dto.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.EstadoPedido;
import com.example.libreria_api.model.gestionpedidos.Pedido;
import java.util.Date;

public class PedidoMapper {

    public static PedidoResponseDTO toPedidoResponseDTO(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setPed_id(pedido.getPed_id());
        dto.setPedCodigo(pedido.getPedCodigo());
        dto.setPedFechaCreacion(pedido.getPedFechaCreacion());
        dto.setPedComentarios(pedido.getPedComentarios());

        if (pedido.getEstadoPedido() != null) {
            dto.setEstId(pedido.getEstadoPedido().getEst_id());
        }

        dto.setPerId(pedido.getPerId());

        // ✅ CORRECCIÓN CRÍTICA: Mapear usuIdEmpleado a usuId
        dto.setUsuId(pedido.getUsuIdEmpleado()); // Esto debería resolver el problema del campo

        return dto;
    }

    public static Pedido toPedido(PedidoRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Pedido pedido = new Pedido();
        pedido.setPedCodigo(dto.getPedCodigo());
        pedido.setPedComentarios(dto.getPedComentarios());
        pedido.setPedFechaCreacion(new Date());
        pedido.setPerId(dto.getPerId());

        // ✅ CORRECCIÓN: Mapear usuId a usuIdEmpleado
        pedido.setUsuIdEmpleado(dto.getUsuId());

        return pedido;
    }

    public static void updatePedidoFromDTO(Pedido pedido, PedidoRequestDTO dto) {
        if (pedido == null || dto == null) {
            return;
        }

        pedido.setPedCodigo(dto.getPedCodigo());
        pedido.setPedComentarios(dto.getPedComentarios());
        pedido.setPerId(dto.getPerId());

        // ✅ CORRECCIÓN: Mapear usuId a usuIdEmpleado
        pedido.setUsuIdEmpleado(dto.getUsuId());
    }
}