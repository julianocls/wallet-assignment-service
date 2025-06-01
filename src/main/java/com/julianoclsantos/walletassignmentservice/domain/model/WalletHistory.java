package com.julianoclsantos.walletassignmentservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.julianoclsantos.walletassignmentservice.domain.enums.TransactionTypeEnum;
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

    private String operationType;

    private Wallet wallet;

    private Long sourceWalletId;

    private LocalDateTime createdAt;

}
