package org.acme.exception;

import java.time.LocalDateTime;

public record ErroResponse(LocalDateTime timestamp, int status, String error, String message) {
    public static ErroResponse of(int status, String error, String message) {
        return new ErroResponse(LocalDateTime.now(), status, error, message);
    }
}
