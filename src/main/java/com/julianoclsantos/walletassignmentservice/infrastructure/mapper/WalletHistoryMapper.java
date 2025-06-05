package com.julianoclsantos.walletassignmentservice.infrastructure.mapper;

import com.julianoclsantos.walletassignmentservice.domain.model.WalletHistory;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletEntity;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletHistoryEntity;
import org.springframework.stereotype.Component;

@Component
public class WalletHistoryMapper {

    public WalletHistory toDomain(WalletHistoryEntity entity) {
        var domain = new WalletHistory();

        domain.setId(entity.getId());
        domain.setAmount(entity.getAmount());
        domain.setTransactionType(entity.getTransactionType());
        domain.setOperationType(entity.getOperationType());
        domain.setSourceWalletId(entity.getSourceWalletId());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());

        return domain;
    }

    public WalletHistoryEntity toEntity(WalletHistory domain) {
        var entity = new WalletHistoryEntity();

        entity.setId(domain.getId());
        entity.setAmount(domain.getAmount());
        entity.setTransactionType(domain.getTransactionType());
        entity.setOperationType(domain.getOperationType());
        entity.setOperationStatusEnum(domain.getOperationStatus());
        entity.setSourceWalletId(domain.getSourceWalletId());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        var walletEntity = WalletEntity.builder()
                .id(domain.getWallet().getId())
                .name(domain.getWallet().getName())
                .code(domain.getWallet().getCode())
                .totalAmount(domain.getWallet().getTotalAmount())
                .isActive(domain.getWallet().getIsActive())
                .userName(domain.getWallet().getUserName())
                .createdAt(domain.getWallet().getCreatedAt())
                .updatedAt(domain.getWallet().getUpdatedAt())
                .build();

        entity.setWallet(walletEntity);

        return entity;
    }
}
