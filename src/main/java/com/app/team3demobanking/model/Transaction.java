package com.app.team3demobanking.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaction {
    @EqualsAndHashCode.Include
    private final Long id;
    private final Account account;
    private final TransactionType type;
    private final BigDecimal amount;
    private final Instant createdAt = Instant.now();
    private final String note;
}
