
package com.app.team3demobanking.service;

import com.app.team3demobanking.exception.InvalidTransferException;
import com.app.team3demobanking.exception.NotFoundException;
import com.app.team3demobanking.model.Account;
import com.app.team3demobanking.model.Transaction;
import com.app.team3demobanking.model.TransactionType;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public synchronized List<Transaction> transfer(
            Long fromAccountId,
            Long toAccountId,
            BigDecimal amount,
            String note) {

        // Defensive validation for direct service calls
        if (fromAccountId == null || toAccountId == null
                || amount == null || amount.signum() <= 0) {
            throw new InvalidTransferException(
                    "Invalid transfer details");
        }

        // Prevent transfers to the same account
        if (fromAccountId.equals(toAccountId)) {
            throw new InvalidTransferException(
                    "Cannot transfer to the same account");
        }

        // Find sender and receiver
        Account sender = accountService.findById(fromAccountId)
                .orElseThrow(() -> new NotFoundException(
                        "Sender account not found"));

        Account receiver = accountService.findById(toAccountId)
                .orElseThrow(() -> new NotFoundException(
                        "Receiver account not found"));

        // Check available balance
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InvalidTransferException(
                    "Insufficient balance");
        }

        // Update account balances
        sender.setBalance(
                sender.getBalance().subtract(amount));

        receiver.setBalance(
                receiver.getBalance().add(amount));

        // Record outgoing transaction
        Transaction debit = transactionService.recordTransaction(
                sender, TransactionType.DEBIT, amount, note);

        // Record incoming transaction
        Transaction credit = transactionService.recordTransaction(
                receiver, TransactionType.CREDIT, amount, note);

        return List.of(debit, credit);
    }
}
