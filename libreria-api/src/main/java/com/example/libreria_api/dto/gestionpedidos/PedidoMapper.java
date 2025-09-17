package com.example.libreria_api.dto.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.Pedido;
import java.util.Date;

public class PedidoMapper {

    public static PedidoResponseDTO toPedidoResponseDTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setPed_id(pedido.getPed_id());
        dto.setPedCodigo(pedido.getPedCodigo());
        dto.setPedFechaCreacion(pedido.getPedFechaCreacion());
        dto.setPedComentarios(pedido.getPedComentarios());
        dto.setEstId(pedido.getEstId());
        dto.setPerId(pedido.getPerId());
        dto.setUsuId(pedido.getUsuId());
        return dto;
    }


    public static Pedido toPedido(PedidoRequestDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setPedCodigo(dto.getPedCodigo());
        pedido.setPedComentarios(dto.getPedComentarios());
        pedido.setEstId(dto.getEstId());
        pedido.setPerId(dto.getPerId());
        pedido.setUsuId(dto.getUsuId());


        pedido.setPedFechaCreacion(new Date());

        return pedido;
    }
}