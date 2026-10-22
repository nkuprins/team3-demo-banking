package com.app.team3demobanking.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {
    @EqualsAndHashCode.Include
    private final Long id;
    @ToString.Exclude
    private final String iban;
    @ToString.Exclude
    private final String ownerName;
    private BigDecimal balance = new BigDecimal("0.00");
}
