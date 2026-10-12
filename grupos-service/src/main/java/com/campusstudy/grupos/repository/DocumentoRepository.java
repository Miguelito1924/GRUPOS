package com.campusstudy.grupos.repository;

import com.campusstudy.grupos.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    List<Documento> findByGrupoIdOrderByFechaActualizacionDesc(Long grupoId);
}
