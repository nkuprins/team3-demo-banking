package com.app.team3demobanking.dto;

import com.app.team3demobanking.model.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionResponse {
    private final Long id;
    private final Long accountId;
    private final TransactionType type;
    private final BigDecimal amount;
    private final Instant createdAt;
    private final String note;

    public TransactionResponse(
            Long id,
            Long accountId,
            TransactionType type,
            BigDecimal amount,
            Instant createdAt,
            String note
    ) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.createdAt = createdAt;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getNote() {
        return note;
    }
}
