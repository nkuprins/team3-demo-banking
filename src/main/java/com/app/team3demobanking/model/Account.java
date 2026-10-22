package com.app.team3demobanking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Long id;
    private String iban;
    private String ownerName;
    private BigDecimal balance;
}
