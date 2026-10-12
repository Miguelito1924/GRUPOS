package com.campusstudy.grupos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AgregarIntegranteRequest {

    @NotNull(message = "usuario_id es obligatorio")
    @Positive(message = "usuario_id debe ser positivo")
    private Long usuario_id;

    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
    }
}
