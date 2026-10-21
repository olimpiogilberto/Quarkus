package org.acme.exception;

import jakarta.ws.rs.WebApplicationException;

public class ApiException extends WebApplicationException {
    public ApiException(int status, String message) {
        super(message, status);
    }

    public static ApiException notFound(String message) {
        return new ApiException(404, message);
    }

    public static ApiException conflict(String message) {
        return new ApiException(409, message);
    }

    public static ApiException badRequest(String message) {
        return new ApiException(400, message);
    }
}
