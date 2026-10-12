package com.campusstudy.grupos.dto;

public class ResumenGruposResponse {
    private Long usuario_id;
    private long total_grupos;
    private long grupos_activos;
    private long grupos_liderados;
    private long grupos_miembro;

    public ResumenGruposResponse() {
    }

    public ResumenGruposResponse(Long usuario_id, long total_grupos, long grupos_activos,
                                 long grupos_liderados, long grupos_miembro) {
        this.usuario_id = usuario_id;
        this.total_grupos = total_grupos;
        this.grupos_activos = grupos_activos;
        this.grupos_liderados = grupos_liderados;
        this.grupos_miembro = grupos_miembro;
    }

    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
    }

    public long getTotal_grupos() {
        return total_grupos;
    }

    public void setTotal_grupos(long total_grupos) {
        this.total_grupos = total_grupos;
    }

    public long getGrupos_activos() {
        return grupos_activos;
    }

    public void setGrupos_activos(long grupos_activos) {
        this.grupos_activos = grupos_activos;
    }

    public long getGrupos_liderados() {
        return grupos_liderados;
    }

    public void setGrupos_liderados(long grupos_liderados) {
        this.grupos_liderados = grupos_liderados;
    }

    public long getGrupos_miembro() {
        return grupos_miembro;
    }

    public void setGrupos_miembro(long grupos_miembro) {
        this.grupos_miembro = grupos_miembro;
    }
}
