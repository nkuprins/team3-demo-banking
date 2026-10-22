package com.app.team3demobanking.exception;

public record ErrorResponse(
        int status,
        String message
) {
}
