package com.campusstudy.grupos.repository;

import com.campusstudy.grupos.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    boolean existsByNombreIgnoreCaseAndMateriaId(String nombre, Long materiaId);

    @Query("""
            SELECT DISTINCT g FROM Grupo g
            JOIN FETCH g.integrantes
            WHERE g.id IN (
                SELECT i.grupo.id FROM Integrante i WHERE i.usuarioId = :usuarioId
            )
            ORDER BY g.fechaCreacion DESC
            """)
    List<Grupo> findByIntegranteUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("""
            SELECT g FROM Grupo g
            LEFT JOIN FETCH g.integrantes
            WHERE g.id = :id
            """)
    Optional<Grupo> findByIdWithIntegrantes(@Param("id") Long id);

    @Query("""
            SELECT g FROM Grupo g
            LEFT JOIN FETCH g.integrantes
            WHERE g.id = :id
            """)
    Optional<Grupo> findByIdWithDetails(@Param("id") Long id);

    @Query("""
            SELECT COUNT(DISTINCT g) FROM Grupo g
            JOIN g.integrantes i
            WHERE i.usuarioId = :usuarioId
            """)
    long countByIntegranteUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("""
            SELECT COUNT(DISTINCT g) FROM Grupo g
            JOIN g.integrantes i
            WHERE i.usuarioId = :usuarioId AND g.activo = true
            """)
    long countActivosByIntegranteUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("""
            SELECT COUNT(DISTINCT g) FROM Grupo g
            JOIN g.integrantes i
            WHERE i.usuarioId = :usuarioId AND i.rol = com.campusstudy.grupos.model.RolIntegrante.lider
            """)
    long countLideradosByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("""
            SELECT COUNT(DISTINCT g) FROM Grupo g
            JOIN g.integrantes i
            WHERE i.usuarioId = :usuarioId AND i.rol = com.campusstudy.grupos.model.RolIntegrante.miembro
            """)
    long countMiembroByUsuarioId(@Param("usuarioId") Long usuarioId);
}
