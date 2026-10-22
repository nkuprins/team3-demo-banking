package com.app.team3demobanking.service;

import com.app.team3demobanking.model.Account;
import com.app.team3demobanking.model.Transaction;
import com.app.team3demobanking.model.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TransactionService {
    private final ConcurrentHashMap<Long, Transaction> transactions = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public Transaction recordTransaction(Account account, TransactionType type, BigDecimal amount, String note) {
        Objects.requireNonNull(account, "account must not be null");
        Objects.requireNonNull(account.getId(), "account id must not be null");
        Objects.requireNonNull(type, "transaction type must not be null");
        Objects.requireNonNull(amount, "transaction amount must not be null");

        Transaction transaction = new Transaction(
                nextId.getAndIncrement(),
                account,
                type,
                amount,
                note
        );
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    public List<Transaction> findByAccountId(Long accountId) {
        Objects.requireNonNull(accountId, "account id must not be null");

        List<Transaction> accountTransactions = new ArrayList<>();
        for (Transaction transaction : transactions.values()) {
            if (accountId.equals(transaction.getAccount().getId())) {
                accountTransactions.add(transaction);
            }
        }
        accountTransactions.sort(Comparator.comparing(Transaction::getCreatedAt)
                .thenComparing(Transaction::getId)
                .reversed());
        return accountTransactions;
    }
}
