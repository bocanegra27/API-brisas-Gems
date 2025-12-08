package com.example.libreria_api.dto.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.Pedido;
import java.util.Date;

public class PedidoMapper {

    public static PedidoResponseDTO toPedidoResponseDTO(Pedido pedido) {
        if (pedido == null) return null;

        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setPedId(pedido.getPed_id());
        dto.setPedCodigo(pedido.getPedCodigo());
        dto.setPedFechaCreacion(pedido.getPedFechaCreacion());
        dto.setPedComentarios(pedido.getPedComentarios());

        if (pedido.getEstadoPedido() != null) {
            dto.setEstId(pedido.getEstadoPedido().getEst_id());
            dto.setEstadoNombre(pedido.getEstadoPedido().getEstNombre());
        }

        // NUEVOS MAPEOS para sesión anónima
        if (pedido.getSesion() != null) {
            dto.setSesionId(pedido.getSesion().getSesId());
            dto.setSesionToken(pedido.getSesion().getSesToken().substring(0, 8));
        }

        dto.setConId(pedido.getConId());
        dto.setPedIdentificadorCliente(pedido.getPedIdentificadorCliente());

        return dto;
    }

    public static Pedido toPedido(PedidoRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Pedido pedido = new Pedido();
        pedido.setPedComentarios(dto.getPedComentarios());
        pedido.setPedFechaCreacion(new Date());
        pedido.setPerId(dto.getPerId());
        pedido.setUsuIdEmpleado(dto.getUsuId());

        // NUEVOS MAPEOS
        pedido.setConId(dto.getConId());
        pedido.setPedIdentificadorCliente(dto.getPedIdentificadorCliente());

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

        // NUEVAS ACTUALIZACIONES
        if (dto.getConId() != null) {
            pedido.setConId(dto.getConId());
        }
        if (dto.getPedIdentificadorCliente() != null) {
            pedido.setPedIdentificadorCliente(dto.getPedIdentificadorCliente());
        }
    }
}