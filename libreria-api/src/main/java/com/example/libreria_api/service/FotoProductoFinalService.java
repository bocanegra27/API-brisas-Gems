package com.example.libreria_api.service;

import com.example.libreria_api.model.FotoProductoFinal;
import com.example.libreria_api.repository.FotoProductoFinalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FotoProductoFinalService {

    @Autowired
    private FotoProductoFinalRepository fotoProductoFinalRepository;

    /**
     * Obtiene todas las fotos de la base de datos.
     * @return una lista de fotos.
     */
    public List<FotoProductoFinal> obtenerTodasLasFotos() {
        return fotoProductoFinalRepository.findAll();
    }

    public FotoProductoFinal guardarFoto(FotoProductoFinal foto) {
        return fotoProductoFinalRepository.save(foto);
    }

    public Optional<FotoProductoFinal> obtenerFotoPorId(Integer id) {
        return fotoProductoFinalRepository.findById(id);
    }

    public FotoProductoFinal actualizarFoto(Integer id, FotoProductoFinal fotoDetalles) {
        // Busca la foto existente por ID.
        return fotoProductoFinalRepository.findById(id).map(fotoExistente -> {
            // Actualiza los campos necesarios.
            fotoExistente.setFotImagenFinal(fotoDetalles.getFotImagenFinal());
            fotoExistente.setFotFechaSubida(fotoDetalles.getFotFechaSubida());
            fotoExistente.setPedido(fotoDetalles.getPedido());
            // Guarda y devuelve la foto actualizada.
            return fotoProductoFinalRepository.save(fotoExistente);
        }).orElse(null); // Devuelve null si no se encuentra la foto con ese ID.
    }

    public boolean eliminarFoto(Integer id) {
        if (fotoProductoFinalRepository.existsById(id)) {
            fotoProductoFinalRepository.deleteById(id);
            return true;
        }
        return false;
    }
}