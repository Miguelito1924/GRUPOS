package com.campusstudy.grupos.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class NotificacionesClient {

    private static final Logger log = LoggerFactory.getLogger(NotificacionesClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final boolean stubsEnabled;
    private final ConcurrentLinkedQueue<Map<String, Object>> colaReintentos = new ConcurrentLinkedQueue<>();

    public NotificacionesClient(
            RestTemplate restTemplate,
            @Value("${campusstudy.clients.notificaciones-base-url}") String baseUrl,
            @Value("${campusstudy.stubs.enabled:true}") boolean stubsEnabled) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.stubsEnabled = stubsEnabled;
    }

    public void enviarNotificacion(Map<String, Object> payload) {
        if (stubsEnabled) {
            log.info("[stub] Notificación encolada/simulada: {}", payload);
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            restTemplate.postForEntity(
                    baseUrl + "/api/v1/notification/create-notifications",
                    new HttpEntity<>(payload, headers),
                    Map.class
            );
        } catch (RestClientException ex) {
            // Contrato: no bloquea la operación principal; se encola para reintento
            colaReintentos.offer(payload);
            log.warn("notificaciones-service no disponible; notificación encolada. Cola={}",
                    colaReintentos.size());
        }
    }

    public int tamañoCola() {
        return colaReintentos.size();
    }
}
