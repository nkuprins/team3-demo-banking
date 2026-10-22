package com.app.team3demobanking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAccountRequest(
        @NotBlank(message = "must not be blank")
        @Size(max = 100, message = "must be at most 100 characters")
        String ownerName
) {
    public CreateAccountRequest {
        if (ownerName != null) {
            ownerName = ownerName.strip();
        }
    }
}
