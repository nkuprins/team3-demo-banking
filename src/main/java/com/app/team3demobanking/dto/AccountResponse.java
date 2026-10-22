package com.app.team3demobanking.dto;

import java.math.BigDecimal;

public class AccountResponse {
    private final Long id;
    private final String iban;
    private final String ownerName;
    private final BigDecimal balance;

    public AccountResponse(Long id, String iban, String ownerName, BigDecimal balance) {
        this.id = id;
        this.iban = iban;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getIban() {
        return iban;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
