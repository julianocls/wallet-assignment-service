package com.julianoclsantos.walletassignmentservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Wallet {

    private Long id;

    private String name;

    private String code;

    private BigDecimal totalAmount;

    private Boolean isActive;

    private String userName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<WalletHistory> histories;

}
