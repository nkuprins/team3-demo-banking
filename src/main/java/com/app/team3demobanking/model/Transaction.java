package com.app.team3demobanking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private Long id;
    private Account account;
    private TransactionType type;
    private BigDecimal amount;
    private Instant createdAt;
    private String note;
}
