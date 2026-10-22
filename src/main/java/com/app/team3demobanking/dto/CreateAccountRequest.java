package com.app.team3demobanking.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(
        @NotBlank(message = "must not be blank")
        String ownerName
) {
}