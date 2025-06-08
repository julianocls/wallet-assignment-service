package com.julianoclsantos.walletassignmentservice.infrastructure.mapper;

import com.julianoclsantos.walletassignmentservice.domain.model.Wallet;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletEntity;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class WalletMapper {

    protected final WalletHistoryMapper walletHistoryMapper;

    public WalletMapper(@Lazy WalletHistoryMapper walletHistoryMapper) {
        this.walletHistoryMapper = walletHistoryMapper;
    }

    public Wallet toDomain(WalletEntity entity) {
        var domain = new Wallet();

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
                            .map(walletHistoryMapper::toDomain)
                            .toList()
            );
        }


        return domain;
    }

    public WalletEntity toEntity(Wallet domain) {
        var entity = new WalletEntity();

        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setCode(domain.getCode());
        entity.setTotalAmount(domain.getTotalAmount());
        entity.setIsActive(domain.getIsActive());
        entity.setUserName(domain.getUserName());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        if (domain.getHistories() != null) {
            entity.setHistories(domain.getHistories().stream()
                    .map(walletHistoryMapper::toEntity)
                    .toList());
        }

        return entity;
    }

    public WalletEntity toEntity(WalletRequest request) {
        return isNull(request) ? null : WalletEntity.builder()
                .name(request.getName())
                .userName(request.getUserName())
                .build();
    }
}
