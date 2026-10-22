
package com.app.team3demobanking.service;

import com.app.team3demobanking.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransferApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Test
    void shouldTransferMoneyThroughApi() throws Exception {

        // Create two test accounts, Alice with a starting balance
        Account alice = accountService.createAccount("Alice", new BigDecimal("500.00"));
        Account bob = accountService.createAccount("Bob", null);

        // Prepare transfer request
        String request = """
                {
                  "fromAccountId": %d,
                  "toAccountId": %d,
                  "amount": 100.00,
                  "note": "Test payment"
                }
                """.formatted(alice.getId(), bob.getId());

        // Send request to our controller
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].type").value("DEBIT"))
                .andExpect(jsonPath("$[1].type").value("CREDIT"));

        // Verify balances
        assertEquals(0, alice.getBalance()
                .compareTo(new BigDecimal("400.00")));

        assertEquals(0, bob.getBalance()
                .compareTo(new BigDecimal("100.00")));

        // Verify transaction history
        assertEquals(2, transactionService
                .findByAccountId(alice.getId()).size());

        assertEquals(1, transactionService
                .findByAccountId(bob.getId()).size());
    }
}
