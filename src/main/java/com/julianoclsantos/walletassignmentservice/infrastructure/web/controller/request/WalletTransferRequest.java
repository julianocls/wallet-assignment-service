package com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class WalletTransferRequest {

    private BigDecimal amount;
    private String originWalletCode;
    private String destinationWalletCode;

}
