package com.julianoclsantos.walletassignmentservice.application.dto;

import com.julianoclsantos.walletassignmentservice.domain.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletBalanceDTO {

    private String userName;
    private String walletName;
    private BigDecimal totalAmount;

    public static WalletBalanceDTO toWalletBalanceDTO(Wallet wallet) {
        return WalletBalanceDTO.builder()
                .userName(wallet.getUserName())
                .walletName(wallet.getName())
                .totalAmount(wallet.getTotalAmount())
                .build();
    }
}
