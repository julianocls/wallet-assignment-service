package com.julianoclsantos.walletassignmentservice.infrastructure.mapper;

import com.julianoclsantos.walletassignmentservice.domain.model.WalletHistory;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletHistoryEntity;
import org.springframework.stereotype.Component;

@Component
public class WalletHistoryMapper {

    public WalletHistory toDomain(WalletHistoryEntity entity, CycleAvoidingMappingContext context) {

        if (entity == null) return null;
        var existing = context.getMappedInstance(entity, WalletHistory.class);
        if (existing != null) return existing;

        var domain = new WalletHistory();
        context.storeMappedInstance(entity, domain);

        domain.setId(entity.getId());
        domain.setAmount(entity.getAmount());
        domain.setTransactionType(entity.getTransactionType());
        domain.setOperationType(entity.getOperationType());
        domain.setSourceWalletId(entity.getSourceWalletId());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());

        return null;
    }

}
