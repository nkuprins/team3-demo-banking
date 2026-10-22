package com.app.team3demobanking.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@RequiredArgsConstructor
@ToString
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
