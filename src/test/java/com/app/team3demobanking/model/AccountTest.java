package com.app.team3demobanking.model;

import com.app.team3demobanking.exception.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Account")
class AccountTest {
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(1L, "LV00TEST0000000000001", "Alice");
        account.deposit(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("deposit adds to the balance")
    void shouldDeposit() {
        account.deposit(new BigDecimal("50.00"));

        assertThat(account.getBalance()).isEqualTo(new BigDecimal("150.00"));
    }

    @Test
    @DisplayName("withdraw subtracts from the balance")
    void shouldWithdraw() {
        account.withdraw(new BigDecimal("30.00"));

        assertThat(account.getBalance()).isEqualTo(new BigDecimal("70.00"));
    }

    @Test
    @DisplayName("withdraw more than the balance throws and leaves the balance unchanged")
    void shouldRejectWithdrawOverBalance() {
        assertThatThrownBy(() -> account.withdraw(new BigDecimal("100.01")))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage("Insufficient balance");

        assertThat(account.getBalance()).isEqualTo(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("deposit and withdraw reject zero and negative amounts")
    void shouldRejectNonPositiveAmounts() {
        assertThatThrownBy(() -> account.deposit(BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> account.withdraw(new BigDecimal("-10.00")))
                .isInstanceOf(IllegalArgumentException.class);

        assertThat(account.getBalance()).isEqualTo(new BigDecimal("100.00"));
    }
}
