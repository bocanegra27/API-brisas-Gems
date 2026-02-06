package com.example.libreria_api.dto.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.Pedido;
import com.example.libreria_api.model.sistemausuarios.SesionAnonima; // <- Podría ser necesario

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


        if (pedido.getPersonalizacion() != null) {
            dto.setPerId(pedido.getPersonalizacion().getPerId());
        }


        if (pedido.getEmpleadoAsignado() != null) {
            dto.setUsuIdEmpleado(pedido.getEmpleadoAsignado().getUsuId());
        }


        if (pedido.getCliente() != null) {
            dto.setUsuIdCliente(pedido.getCliente().getUsuId());
        }


        if (pedido.getSesion() != null) {
            dto.setSesionId(pedido.getSesion().getSesId());
            String sesToken = pedido.getSesion().getSesToken();
            if (sesToken != null) {
                dto.setSesionToken(sesToken.substring(0, Math.min(sesToken.length(), 8)));
            }
        }


        dto.setConId(pedido.getConId());
        dto.setPedIdentificadorCliente(pedido.getPedIdentificadorCliente());

        return dto;
    }


}