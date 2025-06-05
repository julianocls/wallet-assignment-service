package com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
public class WalletDepositRequest {

    private BigDecimal amount;
    private String walletCode;

    public static WalletDepositRequest toRequest(String walletCode, BigDecimal amount) {
        return builder().walletCode(walletCode).amount(amount).build();
    }

}
