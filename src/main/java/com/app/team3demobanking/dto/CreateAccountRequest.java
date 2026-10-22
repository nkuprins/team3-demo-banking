package com.app.team3demobanking.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotBlank(message = "must not be blank")
        @Size(max = 100, message = "must be at most 100 characters")
        String ownerName,

        @PositiveOrZero(message = "must not be negative")
        @Digits(integer = 15, fraction = 2, message = "must have at most 15 digits and 2 decimal places")
        BigDecimal initialBalance
) {
    public CreateAccountRequest {
        if (ownerName != null) {
            ownerName = ownerName.strip();
        }
    }
}
