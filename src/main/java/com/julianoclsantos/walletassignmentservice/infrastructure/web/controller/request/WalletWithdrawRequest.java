package com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
public class WalletWithdrawRequest {

    private BigDecimal amount;
    private String walletCode;

}
