package com.app.team3demobanking.controller;

import com.app.team3demobanking.dto.AccountResponse;
import com.app.team3demobanking.dto.CreateAccountRequest;
import com.app.team3demobanking.dto.TransactionResponse;
import com.app.team3demobanking.model.Account;
import com.app.team3demobanking.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Account account = accountService.createAccount(request.ownerName(), request.initialBalance());
        return ResponseEntity.created(URI.create("/account/" + account.getId()))
                .body(AccountResponse.from(account));
    }

    @GetMapping("/{id}")
    public AccountResponse getAccountById(@PathVariable Long id) {
        return AccountResponse.from(accountService.getById(id));
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionResponse> getAccountTransactions(@PathVariable Long id) {
        return accountService.getTransactions(id).stream()
                .map(TransactionResponse::from)
                .toList();
    }
}
