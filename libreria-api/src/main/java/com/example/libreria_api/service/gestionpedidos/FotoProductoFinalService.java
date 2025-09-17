package com.example.libreria_api.service.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.FotoProductoFinalMapper;
import com.example.libreria_api.dto.gestionpedidos.FotoProductoFinalRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.FotoProductoFinalResponseDTO;
import com.example.libreria_api.model.gestionpedidos.FotoProductoFinal;
import com.example.libreria_api.model.gestionpedidos.Pedido;
import com.example.libreria_api.repository.gestionpedidos.FotoProductoFinalRepository;
import com.example.libreria_api.repository.gestionpedidos.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FotoProductoFinalService {

    private final FotoProductoFinalRepository fotoProductoFinalRepository;
    private final PedidoRepository pedidoRepository;

     public FotoProductoFinalService(FotoProductoFinalRepository fotoProductoFinalRepository, PedidoRepository pedidoRepository) {
        this.fotoProductoFinalRepository = fotoProductoFinalRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public List<FotoProductoFinalResponseDTO> obtenerTodasLasFotos() {
        return fotoProductoFinalRepository.findAll()
                .stream()
                .map(FotoProductoFinalMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<FotoProductoFinalResponseDTO> obtenerFotoPorId(Integer id) {
        return fotoProductoFinalRepository.findById(id)
                .map(FotoProductoFinalMapper::toResponseDTO);
    }

    public FotoProductoFinalResponseDTO guardarFoto(FotoProductoFinalRequestDTO requestDTO) {
         Pedido pedido = pedidoRepository.findById(requestDTO.getPed_id())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + requestDTO.getPed_id()));

         FotoProductoFinal nuevaFoto = new FotoProductoFinal();
        nuevaFoto.setFotImagenFinal(requestDTO.getFotImagenFinal());
        nuevaFoto.setFotFechaSubida(LocalDate.now());
        nuevaFoto.setPedido(pedido); //

         FotoProductoFinal fotoGuardada = fotoProductoFinalRepository.save(nuevaFoto);
        return FotoProductoFinalMapper.toResponseDTO(fotoGuardada);
    }

    public FotoProductoFinalResponseDTO actualizarFoto(Integer id, FotoProductoFinalRequestDTO requestDTO) {
         FotoProductoFinal fotoExistente = fotoProductoFinalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foto no encontrada con id: " + id));

         Pedido pedido = pedidoRepository.findById(requestDTO.getPed_id())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + requestDTO.getPed_id()));

         fotoExistente.setFotImagenFinal(requestDTO.getFotImagenFinal());
        fotoExistente.setPedido(pedido);

        FotoProductoFinal fotoActualizada = fotoProductoFinalRepository.save(fotoExistente);
        return FotoProductoFinalMapper.toResponseDTO(fotoActualizada);
    }

    public boolean eliminarFoto(Integer id) {
        if (fotoProductoFinalRepository.existsById(id)) {
            fotoProductoFinalRepository.deleteById(id);
            return true;
        }
        return false;
    }
}