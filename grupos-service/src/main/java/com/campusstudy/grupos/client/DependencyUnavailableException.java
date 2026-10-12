package com.campusstudy.grupos.client;

public class DependencyUnavailableException extends RuntimeException {

    private final String dependency;

    public DependencyUnavailableException(String dependency, String message) {
        super(message);
        this.dependency = dependency;
    }

    public DependencyUnavailableException(String dependency, String message, Throwable cause) {
        super(message, cause);
        this.dependency = dependency;
    }

    public String getDependency() {
        return dependency;
    }
}
