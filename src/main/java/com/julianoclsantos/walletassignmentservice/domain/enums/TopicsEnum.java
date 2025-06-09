package com.julianoclsantos.walletassignmentservice.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TopicsEnum {

    WALLET_ASSIGNMENT_WALLET_DEPOSIT,
    WALLET_ASSIGNMENT_WALLET_DEPOSIT_DLQ,
    WALLET_ASSIGNMENT_WALLET_TRANSFER,
    WALLET_ASSIGNMENT_WALLET_TRANSFER_DLQ,
    WALLET_ASSIGNMENT_WALLET_WITHDRAW,
    WALLET_ASSIGNMENT_WALLET_WITHDRAW_DLQ;

}



