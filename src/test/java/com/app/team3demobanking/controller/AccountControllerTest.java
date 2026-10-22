package com.app.team3demobanking.controller;

import com.app.team3demobanking.exception.NotFoundException;
import com.app.team3demobanking.model.Account;
import com.app.team3demobanking.model.Transaction;
import com.app.team3demobanking.model.TransactionType;
import com.app.team3demobanking.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@DisplayName("AccountController")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @Test
    @DisplayName("POST /account creates an account and returns 201")
    void shouldCreateAccount() throws Exception {
        Account account = new Account(1L, "LV00TEST0000000000001", "Alice");
        when(accountService.createAccount("Alice", null)).thenReturn(account);

        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ownerName\": \"Alice\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ownerName").value("Alice"));
    }

    @Test
    @DisplayName("POST /account with blank ownerName returns 400")
    void shouldReturn400ForBlankOwnerName() throws Exception {
        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ownerName\": \"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /account/{id} returns 404 when account not found")
    void shouldReturn404WhenAccountNotFound() throws Exception {
        when(accountService.getById(999L)).thenThrow(new NotFoundException("Account not found"));

        mockMvc.perform(get("/account/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /account/{id}/transactions returns the account's transactions")
    void shouldReturnAccountTransactions() throws Exception {
        Account account = new Account(1L, "LV00TEST0000000000001", "Alice");
        Transaction transaction = new Transaction(
                1L, account, TransactionType.CREDIT, new BigDecimal("500.00"), "Salary");
        when(accountService.getTransactions(1L)).thenReturn(List.of(transaction));

        mockMvc.perform(get("/account/1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountId").value(1))
                .andExpect(jsonPath("$[0].type").value("CREDIT"));
    }
}
