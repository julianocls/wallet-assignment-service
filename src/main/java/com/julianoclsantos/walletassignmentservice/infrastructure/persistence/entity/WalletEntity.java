package com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "WALLETS", schema = "WALLET_ASSIGNMENT_ADM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallets_seq_gen")
    @SequenceGenerator(name = "wallets_seq_gen", sequenceName = "WALLETS_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "CODE", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "TOTAL_AMOUNT", nullable = false, precision = 12, scale = 4)
    private BigDecimal totalAmount;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive = true;

    @Column(name = "USER_NAME", nullable = false, length = 100)
    private String userName;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "wallet")
    private List<WalletHistoryEntity> histories;
}
