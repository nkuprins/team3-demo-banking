package com.app.team3demobanking.service;

import com.app.team3demobanking.exception.NotFoundException;
import com.app.team3demobanking.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountServiceTest")
class AccountServiceTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private AccountService service;

    @Test
    @DisplayName("createAccount stores the account with id, owner and zero balance")
    void shouldCreateAccount() {
        Account account = service.createAccount("Alice", null);
        assertThat(account.getId()).isNotNull();
        assertThat(account.getOwnerName()).isEqualTo("Alice");
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(account.getIban()).startsWith("LV");
    }

    @Test
    @DisplayName("createAccount sets the initial balance with 2 decimal places")
    void shouldCreateAccountWithInitialBalance() {
        Account account = service.createAccount("Alice", new BigDecimal("500"));

        assertThat(account.getBalance()).isEqualTo(new BigDecimal("500.00"));
    }

    @Test
    @DisplayName("getTransactions throws when the account does not exist")
    void shouldThrowForTransactionsOfMissingAccount() {
        assertThatThrownBy(() -> service.getTransactions(999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("createAccount gives each account a different id")
    void shouldAssignUniqueIds() {
        Account first = service.createAccount("Alice", null);
        Account second = service.createAccount("Bob", null);

        assertThat(first.getId()).isNotEqualTo(second.getId());
    }

    @Test
    @DisplayName("findById returns the account when it exists")
    void shouldFindExistingAccounts() {
        Account created = service.createAccount("Alice", null);

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
