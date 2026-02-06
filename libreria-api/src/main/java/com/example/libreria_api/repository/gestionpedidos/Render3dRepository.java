package com.example.libreria_api.repository.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.Render3d;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Render3dRepository extends JpaRepository<Render3d, Integer> {


    @Query(value = "SELECT r FROM Render3d r WHERE r.pedido.ped_id = ?1 ORDER BY r.ren_id DESC LIMIT 1")
    Optional<Render3d> findTopRenderByPedId(Integer pedId);


}