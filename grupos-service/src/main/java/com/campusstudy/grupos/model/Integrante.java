package com.campusstudy.grupos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "integrantes", uniqueConstraints = {
        @UniqueConstraint(name = "uk_integrante_grupo_usuario", columnNames = {"grupo_id", "usuario_id"})
})
public class Integrante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupo_id", nullable = false)
    private Grupo grupo;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolIntegrante rol;

    @Column(name = "unido_en", nullable = false)
    private LocalDateTime unidoEn;

    @PrePersist
    void onCreate() {
        if (unidoEn == null) {
            unidoEn = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public RolIntegrante getRol() {
        return rol;
    }

    public void setRol(RolIntegrante rol) {
        this.rol = rol;
    }

    public LocalDateTime getUnidoEn() {
        return unidoEn;
    }

    public void setUnidoEn(LocalDateTime unidoEn) {
        this.unidoEn = unidoEn;
    }
}
