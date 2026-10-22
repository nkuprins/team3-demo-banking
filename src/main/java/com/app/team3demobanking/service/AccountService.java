package com.app.team3demobanking.service;

import com.app.team3demobanking.exception.NotFoundException;
import com.app.team3demobanking.model.Account;
import com.app.team3demobanking.model.Transaction;
import com.app.team3demobanking.model.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class AccountService {

    private static final String DEMO_BANK_IDENTIFIER = "TEST";
    private static final long MAX_ACCOUNT_NUMBER = 9_999_999_999_999L;
    private static final BigInteger IBAN_CHECK_MODULUS = BigInteger.valueOf(97);


    private final Map<Long, Account> accounts = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);
    private final TransactionService transactionService;

    public Account createAccount(String ownerName, BigDecimal initialBalance) {
        Long id = nextId.getAndIncrement();
        Account account = new Account(id, generateDemoIban(id), ownerName);
        if (initialBalance != null && initialBalance.signum() > 0) {
            deposit(account, initialBalance, "Initial balance");
        }
        accounts.put(id, account);
        return account;
    }

    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public Account getById(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Account not found"));
    }

    public List<Transaction> getTransactions(Long accountId) {
        return transactionService.findByAccountId(getById(accountId).getId());
    }

    // Balance changes and their transaction records always happen together
    Transaction deposit(Account account, BigDecimal amount, String note) {
        BigDecimal money = amount.setScale(2, RoundingMode.UNNECESSARY);
        account.deposit(money);
        return transactionService.recordTransaction(account, TransactionType.CREDIT, money, note);
    }

    Transaction withdraw(Account account, BigDecimal amount, String note) {
        BigDecimal money = amount.setScale(2, RoundingMode.UNNECESSARY);
        account.withdraw(money);
        return transactionService.recordTransaction(account, TransactionType.DEBIT, money, note);
    }

    private String generateDemoIban(long accountId) {
        if (accountId < 1 || accountId > MAX_ACCOUNT_NUMBER) {
            throw new IllegalStateException("Demo IBAN account-number capacity exceeded");
        }

        String accountNumber = String.format(Locale.ROOT, "%013d", accountId);
        String basicBankAccountNumber = DEMO_BANK_IDENTIFIER + accountNumber;
        String rearrangedIban = basicBankAccountNumber + "LV00";
        BigInteger remainder = new BigInteger(convertLettersToNumbers(rearrangedIban))
                .mod(IBAN_CHECK_MODULUS);
        String checkDigits = String.format(Locale.ROOT, "%02d", 98 - remainder.intValue());

        return "LV" + checkDigits + basicBankAccountNumber;
    }

    private String convertLettersToNumbers(String value) {
        StringBuilder numericValue = new StringBuilder();
        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            if (character >= 'A' && character <= 'Z') {
                numericValue.append(character - 'A' + 10);
            } else {
                numericValue.append(character);
            }
        }
        return numericValue.toString();
    }
}
