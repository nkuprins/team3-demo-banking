package com.app.team3demobanking.service;

import com.app.team3demobanking.model.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AccountService {
    private final Map<Long, Account> accounts = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public Account createAccount(String iban, String ownerName) {
        Long id = nextId.getAndIncrement();
        Account account = new Account(id, iban, ownerName, BigDecimal.ZERO);
        accounts.put(id, account);
        return account;
    }

    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(accounts.get(id));
    }
}
