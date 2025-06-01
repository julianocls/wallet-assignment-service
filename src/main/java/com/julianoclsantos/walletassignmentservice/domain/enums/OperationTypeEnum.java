package com.julianoclsantos.walletassignmentservice.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationTypeEnum {

    DEPOSIT("DEPOSIT", "Deposito"),
    TRANSFER("TRANSFER", "Transferencia"),
    WITHDRAWAL("WITHDRAWAL", "Saque"),
    REFUND("REFUND", "Estorno")

    ;

    private final String key;
    private final String description;

}
