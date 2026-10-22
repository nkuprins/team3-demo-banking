package com.app.team3demobanking.service;

import com.app.team3demobanking.model.Account;
import com.app.team3demobanking.model.Transaction;
import com.app.team3demobanking.model.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TransactionService {
    private final ConcurrentHashMap<Long, List<Transaction>> transactionsByAccount = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    Transaction recordTransaction(Account account, TransactionType type, BigDecimal amount, String note) {
        Transaction transaction = new Transaction(
                nextId.getAndIncrement(),
                account,
                type,
                amount,
                note
        );
        transactionsByAccount.computeIfAbsent(account.getId(), id -> new CopyOnWriteArrayList<>())
                .add(transaction);
        return transaction;
    }

    public List<Transaction> findByAccountId(Long accountId) {
        List<Transaction> accountTransactions = new ArrayList<>(
                transactionsByAccount.getOrDefault(accountId, List.of()));
        accountTransactions.sort(Comparator.comparing(Transaction::getCreatedAt)
                .thenComparing(Transaction::getId)
                .reversed());
        return accountTransactions;
    }
}
