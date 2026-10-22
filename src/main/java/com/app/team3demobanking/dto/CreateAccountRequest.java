package com.app.team3demobanking.dto;

public record CreateAccountRequest(
    String iban, 
    String ownerName
    ) {
}
