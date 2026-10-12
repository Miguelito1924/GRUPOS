package com.campusstudy.grupos.dto;

public class IntegranteResponse {
    private Long usuario_id;
    private String rol;
    private String unido_en;

    public IntegranteResponse() {
    }

    public IntegranteResponse(Long usuario_id, String rol, String unido_en) {
        this.usuario_id = usuario_id;
        this.rol = rol;
        this.unido_en = unido_en;
    }

    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getUnido_en() {
        return unido_en;
    }

    public void setUnido_en(String unido_en) {
        this.unido_en = unido_en;
    }
}
