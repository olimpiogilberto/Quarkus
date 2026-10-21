package org.acme.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {
    @Override
    public Response toResponse(ApiException exception) {
        int status = exception.getResponse().getStatus();
        String error = switch (status) {
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            case 409 -> "Conflict";
            default -> "Request Error";
        };
        return Response.status(status)
                .entity(ErroResponse.of(status, error, exception.getMessage()))
                .build();
    }
}
