package com.campusstudy.grupos.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class RetrySupport {

    private final int maxRetries;

    public RetrySupport(@Value("${campusstudy.clients.max-retries:2}") int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public <T> T execute(String dependency, Supplier<T> action) {
        int attempts = 0;
        RuntimeException last = null;
        while (attempts <= maxRetries) {
            try {
                return action.get();
            } catch (DependencyUnavailableException ex) {
                throw ex;
            } catch (RuntimeException ex) {
                last = ex;
                attempts++;
                if (attempts > maxRetries) {
                    break;
                }
                try {
                    Thread.sleep(100L * attempts);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new DependencyUnavailableException(dependency,
                            "Interrumpido al reintentar " + dependency, ie);
                }
            }
        }
        throw new DependencyUnavailableException(dependency,
                "Servicio dependiente no disponible tras reintentos: " + dependency, last);
    }
}
