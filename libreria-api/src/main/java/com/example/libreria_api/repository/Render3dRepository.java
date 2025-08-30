package com.example.libreria_api.repository;

import com.example.libreria_api.model.Render3d;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Render3dRepository extends JpaRepository<Render3d, Integer> {
}