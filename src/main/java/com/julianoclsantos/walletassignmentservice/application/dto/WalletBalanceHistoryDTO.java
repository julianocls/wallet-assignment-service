package com.julianoclsantos.walletassignmentservice.application.dto;

import com.julianoclsantos.walletassignmentservice.domain.enums.TransactionTypeEnum;
import com.julianoclsantos.walletassignmentservice.domain.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletBalanceHistoryDTO {

    private String userName;
    private String walletName;
    private BigDecimal totalAmount;

    public static WalletBalanceHistoryDTO toTotalize(Wallet wallet) {
        BigDecimal totalAmount = wallet.getHistories().stream()
                .map(
                        i -> i.getTransactionType() == TransactionTypeEnum.CREDIT
                                ? i.getAmount()
                                : i.getAmount().negate()
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return WalletBalanceHistoryDTO.builder()
                .userName(wallet.getUserName())
                .walletName(wallet.getName())
                .totalAmount(totalAmount)
                .build();
    }

}
