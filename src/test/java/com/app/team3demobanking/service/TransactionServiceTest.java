package com.app.team3demobanking.service;

import com.app.team3demobanking.model.Account;
import com.app.team3demobanking.model.Transaction;
import com.app.team3demobanking.model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TransactionService")
class TransactionServiceTest {

    private TransactionService service;
    private Account account;

    @BeforeEach
    void setUp() {
        service = new TransactionService();
        account = new Account(1L, "LV00TEST0000000000001", "Alice", BigDecimal.ZERO);
    }

    @Test
    @DisplayName("recordTransaction stores and returns the transaction")
    void shouldRecordTransaction() {
        Transaction tx = service.recordTransaction(account, TransactionType.CREDIT, new BigDecimal("100"), "deposit");

        assertThat(tx.getId()).isNotNull();
        assertThat(tx.getAccount()).isEqualTo(account);
        assertThat(tx.getType()).isEqualTo(TransactionType.CREDIT);
        assertThat(tx.getAmount()).isEqualByComparingTo(new BigDecimal("100"));
        assertThat(tx.getNote()).isEqualTo("deposit");
        assertThat(tx.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("recordTransaction throws for null account")
    void shouldThrowForNullAccount() {
        assertThatThrownBy(() -> service.recordTransaction(null, TransactionType.CREDIT, new BigDecimal("100"), "x"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("recordTransaction throws for null amount")
    void shouldThrowForNullAmount() {
        assertThatThrownBy(() -> service.recordTransaction(account, TransactionType.CREDIT, null, "x"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("findByAccountId returns only that account's transactions")
    void shouldFindTransactionsByAccount() {
        Account other = new Account(2L, "LV00TEST0000000000002", "Bob", BigDecimal.ZERO);
        service.recordTransaction(account, TransactionType.CREDIT, new BigDecimal("100"), "a");
        service.recordTransaction(account, TransactionType.DEBIT, new BigDecimal("50"), "b");
        service.recordTransaction(other, TransactionType.CREDIT, new BigDecimal("30"), "c");

        List<Transaction> result = service.findByAccountId(1L);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(tx -> tx.getAccount().getId().equals(1L));
    }

    @Test
    @DisplayName("findByAccountId returns empty list when account has no transactions")
    void shouldReturnEmptyWhenNoTransactions() {
        List<Transaction> result = service.findByAccountId(999L);

        assertThat(result).isEmpty();
    }
}
