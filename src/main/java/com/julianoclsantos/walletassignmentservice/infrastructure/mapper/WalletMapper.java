package com.julianoclsantos.walletassignmentservice.infrastructure.mapper;

import com.julianoclsantos.walletassignmentservice.domain.model.Wallet;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletEntity;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletRequest;
import org.mapstruct.Context;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class WalletMapper {

    protected final WalletHistoryMapper walletHistoryMapper;

    public WalletMapper(@Lazy WalletHistoryMapper walletHistoryMapper) {
        this.walletHistoryMapper = walletHistoryMapper;
    }

    public Wallet toDomain(WalletEntity entity, @Context CycleAvoidingMappingContext context) {
        if (entity == null) return null;
        var existing = context.getMappedInstance(entity, Wallet.class);
        if (existing != null) return existing;

        var domain = new Wallet();
        context.storeMappedInstance(entity, domain);

        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setCode(entity.getCode());
        domain.setTotalAmount(entity.getTotalAmount());
        domain.setIsActive(entity.getIsActive());
        domain.setUserName(entity.getUserName());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getHistories() != null) {
            domain.setHistories(
                    entity.getHistories().stream()
                            .map(wh -> walletHistoryMapper.toDomain(wh, context))
                            .toList()
            );
        }


        return domain;
    }

    public WalletEntity toEntity(Wallet domain, @Context CycleAvoidingMappingContext context) {
        return null;
    }

    public WalletEntity toEntity(WalletRequest request) {
        return isNull(request) ? null : WalletEntity.builder()
                .name(request.getName())
                .userName(request.getUserName())
                .build();
    }
}
