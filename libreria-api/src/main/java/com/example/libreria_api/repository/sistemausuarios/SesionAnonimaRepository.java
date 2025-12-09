package com.example.libreria_api.repository.sistemausuarios;

import com.example.libreria_api.model.sistemausuarios.SesionAnonima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SesionAnonimaRepository extends JpaRepository<SesionAnonima, Integer> {

    Optional<SesionAnonima> findBySesToken(String token);

    List<SesionAnonima> findBySesConvertido(Boolean convertido);

    @Query("SELECT s FROM SesionAnonima s WHERE s.sesFechaExpiracion < :fecha AND s.sesConvertido = false")
    List<SesionAnonima> findSesionesExpiradas(LocalDateTime fecha);

    @Query("SELECT COUNT(s) FROM SesionAnonima s WHERE s.sesConvertido = true")
    Long countConversiones();
}