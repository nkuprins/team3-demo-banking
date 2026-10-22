package com.app.team3demobanking.dto;

import java.math.BigDecimal;

public record AccountResponse(
        Long id,
        String iban,
        String ownerName,
        BigDecimal balance
) {
}