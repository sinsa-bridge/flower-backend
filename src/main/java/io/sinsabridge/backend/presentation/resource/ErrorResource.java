package io.sinsabridge.backend.presentation.resource;

import org.springframework.hateoas.RepresentationModel;

public class ErrorResource extends RepresentationModel<ErrorResource> {

    private final String error;
    private final String message;

    public ErrorResource(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}

