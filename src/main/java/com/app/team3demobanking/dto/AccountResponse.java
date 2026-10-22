package com.app.team3demobanking.dto;

import com.app.team3demobanking.model.Account;

import java.math.BigDecimal;

public record AccountResponse(
        Long id,
        String iban,
        String ownerName,
        BigDecimal balance
) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getIban(),
                account.getOwnerName(),
                account.getBalance()
        );
    }
}
