package com.app.team3demobanking.controller;

import com.app.team3demobanking.exception.NotFoundException;
import com.app.team3demobanking.model.Account;
import com.app.team3demobanking.service.AccountService;
import com.app.team3demobanking.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

    @MockitoBean
    private TransactionService transactionService;

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
}
