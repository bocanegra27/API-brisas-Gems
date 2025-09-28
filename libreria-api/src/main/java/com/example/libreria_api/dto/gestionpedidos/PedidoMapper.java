package com.example.libreria_api.dto.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.EstadoPedido;
import com.example.libreria_api.model.gestionpedidos.Pedido;
import java.util.Date;

public class PedidoMapper {

    public static PedidoResponseDTO toPedidoResponseDTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setPed_id(pedido.getPed_id());
        dto.setPedCodigo(pedido.getPedCodigo());
        dto.setPedFechaCreacion(pedido.getPedFechaCreacion());
        dto.setPedComentarios(pedido.getPedComentarios());

        if (pedido.getEstadoPedido() != null) {
            dto.setEstId(pedido.getEstadoPedido().getEst_id());
        }

        dto.setPerId(pedido.getPerId());
        dto.setUsuId(pedido.getUsuIdEmpleado());
        return dto;
    }

    public static Pedido toPedido(PedidoRequestDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setPedCodigo(dto.getPedCodigo());
        pedido.setPedComentarios(dto.getPedComentarios());

        if (dto.getEstId() != null) {
            EstadoPedido estado = new EstadoPedido();
            estado.setEst_id(dto.getEstId());
            pedido.setEstadoPedido(estado);
        }

        pedido.setPerId(dto.getPerId());
        pedido.setUsuIdEmpleado(dto.getUsuId());
        pedido.setPedFechaCreacion(new Date());
        return pedido;
    }
}