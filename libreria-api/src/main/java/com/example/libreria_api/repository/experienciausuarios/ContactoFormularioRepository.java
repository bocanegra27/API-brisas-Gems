package com.example.libreria_api.repository.experienciausuarios;

import com.example.libreria_api.model.experienciausuarios.ContactoFormulario;
import com.example.libreria_api.model.experienciausuarios.ViaContacto;
import com.example.libreria_api.model.experienciausuarios.EstadoContacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ContactoFormularioRepository extends JpaRepository<ContactoFormulario, Integer> {


    List<ContactoFormulario> findByConVia(ViaContacto via);


    List<ContactoFormulario> findByConEstado(EstadoContacto estado);


    List<ContactoFormulario> findByConFechaEnvioBetween(LocalDateTime desde, LocalDateTime hasta);


    List<ContactoFormulario> findByUsuario_UsuId(Integer usuarioId);

    List<ContactoFormulario> findByUsuarioAdmin_UsuId(Integer usuarioAdminId);

    long countByConEstado(EstadoContacto estado);


    @Query("SELECT c FROM ContactoFormulario c " +
            "WHERE (:via IS NULL OR c.conVia = :via) " +
            "AND (:estado IS NULL OR c.conEstado = :estado) " +
            "AND (:usuarioId IS NULL OR c.usuario.usuId = :usuarioId) " +
            "AND (:desde IS NULL OR c.conFechaEnvio >= :desde) " +
            "AND (:hasta IS NULL OR c.conFechaEnvio <= :hasta)" +
            "ORDER BY c.conFechaEnvio DESC")
    List<ContactoFormulario> buscarConFiltros(@Param("via") ViaContacto via,
                                              @Param("estado") EstadoContacto estado,
                                              @Param("usuarioId") Integer usuarioId,
                                              @Param("desde") LocalDateTime desde,
                                              @Param("hasta") LocalDateTime hasta);

    @Query("SELECT c FROM ContactoFormulario c WHERE c.sesion.sesId = :sesionId")
    List<ContactoFormulario> findBySesionId(@Param("sesionId") Integer sesionId);

    @Query("SELECT c FROM ContactoFormulario c WHERE c.personalizacion.perId = :personalizacionId")
    List<ContactoFormulario> findByPersonalizacionId(@Param("personalizacionId") Integer personalizacionId);
}
