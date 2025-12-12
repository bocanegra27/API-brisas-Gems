package com.example.libreria_api.repository.seguridad;

import com.example.libreria_api.model.seguridad.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);
}