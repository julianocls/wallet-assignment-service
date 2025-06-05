package com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class WalletWithdrawRequest {

    private BigDecimal amount;
    private String walletCode;

}
