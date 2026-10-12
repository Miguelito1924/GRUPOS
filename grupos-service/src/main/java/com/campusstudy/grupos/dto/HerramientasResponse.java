package com.campusstudy.grupos.dto;

public class HerramientasResponse {
    private String zoom_url;
    private String jam_url;

    public HerramientasResponse() {
    }

    public HerramientasResponse(String zoom_url, String jam_url) {
        this.zoom_url = zoom_url;
        this.jam_url = jam_url;
    }

    public String getZoom_url() {
        return zoom_url;
    }

    public void setZoom_url(String zoom_url) {
        this.zoom_url = zoom_url;
    }

    public String getJam_url() {
        return jam_url;
    }

    public void setJam_url(String jam_url) {
        this.jam_url = jam_url;
    }
}
