
package com.app.team3demobanking.service;

import com.app.team3demobanking.model.Account;
import com.app.team3demobanking.model.Transaction;
import com.app.team3demobanking.model.TransactionType;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public TransferService(AccountService accountService,
                           TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public synchronized List<Transaction> transfer(
            Long fromAccountId,
            Long toAccountId,
            BigDecimal amount,
            String note) {

        // Validate transfer details
        if (fromAccountId == null || toAccountId == null
                || amount == null || amount.signum() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid transfer details");
        }

        // Prevent transferring to the same account
        if (fromAccountId.equals(toAccountId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot transfer to the same account");
        }

        // Find sender and receiver accounts
        Account sender = accountService.findById(fromAccountId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sender account not found"));

        Account receiver = accountService.findById(toAccountId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Receiver account not found"));

        // Check available balance
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Insufficient balance");
        }

        // Update balances
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        // Record both sides of the transfer
        Transaction debit = transactionService.recordTransaction(
                sender, TransactionType.DEBIT, amount, note);

        Transaction credit = transactionService.recordTransaction(
                receiver, TransactionType.CREDIT, amount, note);

        return List.of(debit, credit);
    }
}
