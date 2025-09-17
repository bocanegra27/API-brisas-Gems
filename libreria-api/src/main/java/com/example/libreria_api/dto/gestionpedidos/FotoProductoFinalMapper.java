package com.example.libreria_api.dto.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.FotoProductoFinal;
import com.example.libreria_api.model.gestionpedidos.Pedido;
import java.time.LocalDate;

public class FotoProductoFinalMapper {

    public static FotoProductoFinalResponseDTO toResponseDTO(FotoProductoFinal foto) {
        FotoProductoFinalResponseDTO dto = new FotoProductoFinalResponseDTO();
        dto.setFot_id(foto.getFot_id());
        dto.setFotImagenFinal(foto.getFotImagenFinal());
        dto.setFotFechaSubida(foto.getFotFechaSubida());

         if (foto.getPedido() != null) {
            dto.setPed_id(foto.getPedido().getPed_id());
        }
        return dto;
    }


    public static FotoProductoFinal toEntity(FotoProductoFinalRequestDTO requestDTO) {
        FotoProductoFinal foto = new FotoProductoFinal();
        foto.setFotImagenFinal(requestDTO.getFotImagenFinal());

         foto.setFotFechaSubida(LocalDate.now());

         if (requestDTO.getPed_id() != null) {
            Pedido pedido = new Pedido();
            pedido.setPed_id(requestDTO.getPed_id());
            foto.setPedido(pedido);
        }

        return foto;
    }
}