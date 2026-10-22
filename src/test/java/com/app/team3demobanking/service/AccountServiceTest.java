package com.app.team3demobanking.service;

import com.app.team3demobanking.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountServiceTest")

class AccountServiceTest {
    private AccountService service;

    @BeforeEach
    void setUp() {
        service = new AccountService();
    }

    @Test
    @DisplayName("createAccount stores the account with id, owner and zero balance")
    void shouldCreateAccount() {
        Account account = service.createAccount("Alice");
        assertThat(account.getId()).isNotNull();
        assertThat(account.getOwnerName()).isEqualTo("Alice");
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(account.getIban()).startsWith("LV");
    }

    @Test
    @DisplayName("createAccount gives each account a different id")
    void shouldAssignUniqueIds() {
        Account first = service.createAccount("Alice");
        Account second = service.createAccount("Bob");

        assertThat(first.getId()).isNotEqualTo(second.getId());
    }

    @Test
    @DisplayName("findById returns the account when it exists")
    void shoudldFindExistingAccounts() {
        Account created = service.createAccount("Alice");

        Optional<Account> found = service.findById(created.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getOwnerName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("findById returns empty when the account does not exist")
    void shouldReturnEmptyForMissingAccount() {
        Optional<Account> found = service.findById(999L);

        assertThat(found).isEmpty();
    }
}
