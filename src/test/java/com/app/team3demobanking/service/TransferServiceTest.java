
package com.app.team3demobanking.service;

import com.app.team3demobanking.exception.InsufficientFundsException;
import com.app.team3demobanking.exception.InvalidTransferException;
import com.app.team3demobanking.exception.NotFoundException;
import com.app.team3demobanking.model.Account;
import com.app.team3demobanking.model.Transaction;
import com.app.team3demobanking.model.TransactionType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransferServiceTest {

    private TransactionService transactionService;
    private TransferService transferService;

    private Account sender;
    private Account receiver;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService();
        AccountService accountService = new AccountService(transactionService);

        transferService = new TransferService(accountService);

        sender = accountService.createAccount(
                "Alice", new BigDecimal("500.00"));

        receiver = accountService.createAccount(
                "Bob", new BigDecimal("200.00"));
    }

    @Test
    void shouldTransferMoneySuccessfully() {
        List<Transaction> result = transferService.transfer(
                sender.getId(), receiver.getId(),
                new BigDecimal("100"), "Rent");

        assertEquals(
                new BigDecimal("400.00"), sender.getBalance());

        assertEquals(
                new BigDecimal("300.00"), receiver.getBalance());

        assertEquals(2, result.size());
        assertEquals(TransactionType.DEBIT, result.get(0).getType());
        assertEquals(TransactionType.CREDIT, result.get(1).getType());

        // Amounts are stored with 2 decimals, like balances
        assertEquals(new BigDecimal("100.00"), result.get(0).getAmount());

        // Initial balance plus one transfer leg on each side
        assertEquals(2, transactionService
                .findByAccountId(sender.getId()).size());

        assertEquals(2, transactionService
                .findByAccountId(receiver.getId()).size());
    }

    @Test
    void shouldRejectInsufficientBalance() {
        InsufficientFundsException exception = assertThrows(
                InsufficientFundsException.class,
                () -> transferService.transfer(
                        sender.getId(), receiver.getId(),
                        new BigDecimal("600.00"), "Payment"));

        assertEquals("Insufficient balance",
                exception.getMessage());

        assertEquals(
                new BigDecimal("500.00"), sender.getBalance());

        assertEquals(
                new BigDecimal("200.00"), receiver.getBalance());

        assertNoTransferRecorded();
    }

    @Test
    void shouldRejectNegativeAmount() {
        InvalidTransferException exception = assertThrows(
                InvalidTransferException.class,
                () -> transferService.transfer(
                        sender.getId(), receiver.getId(),
                        new BigDecimal("-50.00"), "Payment"));

        assertEquals("Invalid transfer details",
                exception.getMessage());

        assertNoTransferRecorded();
    }

    @Test
    void shouldRejectZeroAmount() {
        InvalidTransferException exception = assertThrows(
                InvalidTransferException.class,
                () -> transferService.transfer(
                        sender.getId(), receiver.getId(),
                        BigDecimal.ZERO, "Payment"));

        assertEquals("Invalid transfer details",
                exception.getMessage());

        assertNoTransferRecorded();
    }

    @Test
    void shouldRejectSameAccountTransfer() {
        InvalidTransferException exception = assertThrows(
                InvalidTransferException.class,
                () -> transferService.transfer(
                        sender.getId(), sender.getId(),
                        new BigDecimal("100.00"), "Payment"));

        assertEquals("Cannot transfer to the same account",
                exception.getMessage());

        assertNoTransferRecorded();
    }

    @Test
    void shouldRejectMissingAccount() {
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> transferService.transfer(
                        sender.getId(), 99L,
                        new BigDecimal("100.00"), "Payment"));

        assertEquals("Receiver account not found",
                exception.getMessage());

        assertNoTransferRecorded();
    }

    // Only the initial balances from setUp exist
    private void assertNoTransferRecorded() {
        assertEquals(1, transactionService
                .findByAccountId(sender.getId()).size());

        assertEquals(1, transactionService
                .findByAccountId(receiver.getId()).size());
    }
}
