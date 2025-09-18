package com.example.libreria_api.dto.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.EstadoPedido;

public class EstadoPedidoMapper {

    public static EstadoPedidoResponseDTO toResponseDTO(EstadoPedido estadoPedido) {
        EstadoPedidoResponseDTO dto = new EstadoPedidoResponseDTO();
        dto.setEst_id(estadoPedido.getEst_id());
        dto.setEstNombre(estadoPedido.getEstNombre());
        return dto;
    }

    public static EstadoPedido toEntity(EstadoPedidoRequestDTO requestDTO) {
        EstadoPedido estadoPedido = new EstadoPedido();
        estadoPedido.setEstNombre(requestDTO.getEstNombre());
        return estadoPedido;
    }
}