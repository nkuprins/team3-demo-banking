package com.app.team3demobanking.dto;

import com.app.team3demobanking.model.Transaction;
import com.app.team3demobanking.model.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
        Long id,
        Long accountId,
        TransactionType type,
        BigDecimal amount,
        Instant createdAt,
        String note
) {
    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAccount().getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCreatedAt(),
                transaction.getNote()
        );
    }
}
