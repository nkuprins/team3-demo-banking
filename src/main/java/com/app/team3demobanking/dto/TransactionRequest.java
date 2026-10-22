package com.app.team3demobanking.dto;

import com.app.team3demobanking.model.TransactionType;

import java.math.BigDecimal;

public record TransactionRequest(
        TransactionType type,
        BigDecimal amount,
        String note
) {
}
