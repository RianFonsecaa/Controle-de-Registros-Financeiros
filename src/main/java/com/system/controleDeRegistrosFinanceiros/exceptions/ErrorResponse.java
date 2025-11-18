package com.system.controleDeRegistrosFinanceiros.exceptions;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        long timestamp,
        Integer status,
        String error,
        String message
) {
    public ErrorResponse(HttpStatus httpStatus, String message) {
        this(
                System.currentTimeMillis(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                message
        );
    }
}