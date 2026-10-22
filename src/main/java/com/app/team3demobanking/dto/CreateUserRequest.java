package com.app.team3demobanking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank(message = "must not be blank")
        String username,

        @NotBlank(message = "must not be blank")
        @Size(min = 6, message = "must be at least 6 characters")
        String password
) {
}
