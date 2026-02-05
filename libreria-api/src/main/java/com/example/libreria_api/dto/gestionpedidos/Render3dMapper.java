package com.example.libreria_api.dto.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.Pedido;
import com.example.libreria_api.model.gestionpedidos.Render3d;

public class Render3dMapper {

    public static Render3dResponseDTO toResponseDTO(Render3d render) {
        Render3dResponseDTO dto = new Render3dResponseDTO();
        dto.setRen_id(render.getRen_id());
        dto.setRenImagen(render.getRenImagen());
        dto.setRenFechaAprobacion(render.getRenFechaDimension());

         if (render.getPedido() != null) {
            dto.setPed_id(render.getPedido().getPed_id());
        }
        return dto;
    }

    public static Render3d toEntity(Render3dRequestDTO requestDTO) {
        Render3d render = new Render3d();
        render.setRenImagen(requestDTO.getRenImagen());
        render.setRenFechaDimension(requestDTO.getRenFechaAprobacion());
        return render;
    }
}