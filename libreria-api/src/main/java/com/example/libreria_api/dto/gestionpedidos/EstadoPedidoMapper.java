package com.example.libreria_api.dto.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.EstadoPedido;

public class EstadoPedidoMapper {

    public static EstadoPedidoResponseDTO toResponseDTO(EstadoPedido estado) {
        if (estado == null) {
            return null;
        }

        EstadoPedidoResponseDTO dto = new EstadoPedidoResponseDTO();
        dto.setEst_id(estado.getEst_id());
        dto.setEstNombre(estado.getEstNombre());
        dto.setEstDescripcion(estado.getEstDescripcion());
        return dto;
    }

    public static EstadoPedido toEntity(EstadoPedidoRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        EstadoPedido estado = new EstadoPedido();
        estado.setEstNombre(requestDTO.getEstNombre());
        estado.setEstDescripcion(requestDTO.getEstDescripcion());
        return estado;
    }
}