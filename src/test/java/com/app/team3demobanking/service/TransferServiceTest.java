
package com.app.team3demobanking.service;

import com.app.team3demobanking.model.Account;
import com.app.team3demobanking.model.Transaction;
import com.app.team3demobanking.model.TransactionType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferServiceTest {

    private AccountService accountService;
    private TransactionService transactionService;
    private TransferService transferService;

    private Account sender;
    private Account receiver;

    @BeforeEach
    void setUp() {
        accountService = mock(AccountService.class);
        transactionService = mock(TransactionService.class);

        transferService = new TransferService(
                accountService, transactionService);

        sender = new Account(
                1L, "LV01TEST", "Alice",
                new BigDecimal("500.00"));

        receiver = new Account(
                2L, "LV02TEST", "Bob",
                new BigDecimal("200.00"));

        when(accountService.findById(1L))
                .thenReturn(Optional.of(sender));

        when(accountService.findById(2L))
                .thenReturn(Optional.of(receiver));
    }

    @Test
    void shouldTransferMoneySuccessfully() {
        BigDecimal amount = new BigDecimal("100.00");

        Transaction debit = new Transaction(
                1L, sender, TransactionType.DEBIT,
                amount, Instant.now(), "Rent");

        Transaction credit = new Transaction(
                2L, receiver, TransactionType.CREDIT,
                amount, Instant.now(), "Rent");

        when(transactionService.recordTransaction(
                sender, TransactionType.DEBIT, amount, "Rent"))
                .thenReturn(debit);

        when(transactionService.recordTransaction(
                receiver, TransactionType.CREDIT, amount, "Rent"))
                .thenReturn(credit);

        List<Transaction> result = transferService.transfer(
                1L, 2L, amount, "Rent");

        assertEquals(
                new BigDecimal("400.00"), sender.getBalance());

        assertEquals(
                new BigDecimal("300.00"), receiver.getBalance());

        assertEquals(2, result.size());
        assertEquals(TransactionType.DEBIT, result.get(0).getType());
        assertEquals(TransactionType.CREDIT, result.get(1).getType());

        verify(transactionService, times(2))
                .recordTransaction(any(), any(), any(), any());
    }

    @Test
    void shouldRejectInsufficientBalance() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> transferService.transfer(
                        1L, 2L,
                        new BigDecimal("600.00"), "Payment"));

        assertEquals(HttpStatus.BAD_REQUEST,
                exception.getStatusCode());

        assertEquals(
                new BigDecimal("500.00"), sender.getBalance());

        verifyNoInteractions(transactionService);
    }

    @Test
    void shouldRejectNegativeAmount() {
        assertThrows(ResponseStatusException.class,
                () -> transferService.transfer(
                        1L, 2L,
                        new BigDecimal("-50.00"), "Payment"));

        verifyNoInteractions(transactionService);
    }

    @Test
    void shouldRejectZeroAmount() {
        assertThrows(ResponseStatusException.class,
                () -> transferService.transfer(
                        1L, 2L,
                        BigDecimal.ZERO, "Payment"));

        verifyNoInteractions(transactionService);
    }

    @Test
    void shouldRejectSameAccountTransfer() {
        assertThrows(ResponseStatusException.class,
                () -> transferService.transfer(
                        1L, 1L,
                        new BigDecimal("100.00"), "Payment"));

        verifyNoInteractions(transactionService);
    }

    @Test
    void shouldRejectMissingAccount() {
        when(accountService.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> transferService.transfer(
                        1L, 99L,
                        new BigDecimal("100.00"), "Payment"));

        assertEquals(HttpStatus.NOT_FOUND,
                exception.getStatusCode());

        verifyNoInteractions(transactionService);
    }
}
