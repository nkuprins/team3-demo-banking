
package com.app.team3demobanking.controller;

import com.app.team3demobanking.dto.TransferRequest;
import com.app.team3demobanking.dto.TransactionResponse;
import com.app.team3demobanking.model.Transaction;
import com.app.team3demobanking.service.TransferService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<List<TransactionResponse>> transfer(
            @Valid @RequestBody TransferRequest request) {

        List<Transaction> transactions = transferService.transfer(
                request.fromAccountId(),
                request.toAccountId(),
                request.amount(),
                request.note()
        );

        List<TransactionResponse> response = transactions.stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getId(),
                        transaction.getAccount().getId(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getCreatedAt(),
                        transaction.getNote()
                ))
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
