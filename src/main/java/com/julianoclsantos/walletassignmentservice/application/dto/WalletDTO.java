package com.julianoclsantos.walletassignmentservice.application.dto;

import com.julianoclsantos.walletassignmentservice.domain.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDTO {

    private String userName;
    private String walletName;
    private String walletCode;

    public static WalletDTO toWalletDTO(Wallet wallet) {
        return WalletDTO.builder()
                .userName(wallet.getUserName())
                .walletName(wallet.getName())
                .walletCode(wallet.getCode())
                .build();
    }
}