package com.campusstudy.grupos.dto;

public class AgregarIntegranteResponse {
    private String mensaje;
    private IntegranteResponse integrante;

    public AgregarIntegranteResponse() {
    }

    public AgregarIntegranteResponse(String mensaje, IntegranteResponse integrante) {
        this.mensaje = mensaje;
        this.integrante = integrante;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public IntegranteResponse getIntegrante() {
        return integrante;
    }

    public void setIntegrante(IntegranteResponse integrante) {
        this.integrante = integrante;
    }
}
