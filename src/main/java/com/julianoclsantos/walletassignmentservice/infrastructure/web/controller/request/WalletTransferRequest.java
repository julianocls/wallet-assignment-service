package com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class WalletTransferRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Please provide the origin Wallet code")
    private String originWalletCode;

    @NotNull(message = "Please provide the destination Wallet code")
    private String destinationWalletCode;

}
