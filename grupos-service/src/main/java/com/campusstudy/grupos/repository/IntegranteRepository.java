package com.campusstudy.grupos.repository;

import com.campusstudy.grupos.model.Integrante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntegranteRepository extends JpaRepository<Integrante, Long> {

    boolean existsByGrupoIdAndUsuarioId(Long grupoId, Long usuarioId);

    Optional<Integrante> findByGrupoIdAndUsuarioId(Long grupoId, Long usuarioId);
}
