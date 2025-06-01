package com.julianoclsantos.walletassignmentservice.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionTypeEnum {

    CREDIT("C", "Crédito."),
    DEBIT("D", "Débito."),

    ;

    private final String key;
    private final String description;

}
