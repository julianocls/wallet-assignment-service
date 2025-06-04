package com.julianoclsantos.walletassignmentservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.julianoclsantos.walletassignmentservice.domain.enums.OperationStatusEnum;
import com.julianoclsantos.walletassignmentservice.domain.enums.OperationTypeEnum;
import com.julianoclsantos.walletassignmentservice.domain.enums.TransactionTypeEnum;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletDepositRequest;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class WalletHistory {

    private Long id;

    private BigDecimal amount;

    private TransactionTypeEnum transactionType;

    private OperationTypeEnum operationType;

    private OperationStatusEnum operationStatus;

    private Wallet wallet;

    private Long sourceWalletId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static WalletHistory toDeposit(WalletDepositRequest request, Wallet wallet) {
        return WalletHistory.builder()
                .amount(request.getAmount())
                .wallet(wallet)
                .transactionType(TransactionTypeEnum.CREDIT)
                .operationType(OperationTypeEnum.DEPOSIT)
                .operationStatus(OperationStatusEnum.CREATED)
                .build();
    }

}
