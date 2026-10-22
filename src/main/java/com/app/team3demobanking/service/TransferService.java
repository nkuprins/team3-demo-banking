
package com.app.team3demobanking.service;

import com.app.team3demobanking.exception.InvalidTransferException;
import com.app.team3demobanking.exception.NotFoundException;
import com.app.team3demobanking.model.Account;
import com.app.team3demobanking.model.Transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountService accountService;

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

        // Withdraw first: it fails before anything changes if the balance is too low
        Transaction debit = accountService.withdraw(
                sender, amount, note);

        Transaction credit = accountService.deposit(
                receiver, amount, note);

        return List.of(debit, credit);
    }
}
