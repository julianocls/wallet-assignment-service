package com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity;

import com.julianoclsantos.walletassignmentservice.domain.enums.OperationStatusEnum;
import com.julianoclsantos.walletassignmentservice.domain.enums.OperationTypeEnum;
import com.julianoclsantos.walletassignmentservice.domain.enums.TransactionTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "WALLETS_HISTORY", schema = "WALLET_ASSIGNMENT_ADM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallets_history_seq_gen")
    @SequenceGenerator(name = "wallets_history_seq_gen", sequenceName = "WALLETS_HISTORY_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "AMOUNT", nullable = false, precision = 12, scale = 4)
    private BigDecimal amount;

    @Column(name = "TRANSACTION_TYPE", nullable = false, length = 1)
    private TransactionTypeEnum transactionType;

    @Column(name = "OPERATION_TYPE", nullable = false, length = 20)
    private OperationTypeEnum operationType;

    @Column(name = "OPERATION_STATUS", nullable = false, length = 20)
    private OperationStatusEnum operationStatusEnum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WALLET_ID", nullable = false)
    private WalletEntity wallet;

    @Column(name = "SOURCE_WALLET_ID")
    private Long sourceWalletId;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        var localDateTime = LocalDateTime.now();
        this.createdAt = localDateTime;
        this.updatedAt = localDateTime;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
