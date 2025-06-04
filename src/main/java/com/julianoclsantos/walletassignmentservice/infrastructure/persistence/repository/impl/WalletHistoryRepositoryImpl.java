package com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository.impl;

import com.julianoclsantos.walletassignmentservice.application.port.out.WalletHistoryRepository;
import com.julianoclsantos.walletassignmentservice.infrastructure.exception.InternalErrorException;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletHistoryEntity;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository.WalletHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.julianoclsantos.walletassignmentservice.domain.enums.MessageEnum.GENERIC_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletHistoryRepositoryImpl implements WalletHistoryRepository {

    private final WalletHistoryJpaRepository jpaRepository;

    @Override
    public void save(WalletHistoryEntity entity) {
        try {
            jpaRepository.save(entity);
        } catch (Exception e) {
            log.error(
                    "Failed to save walletHistory. ID={}, Name: {}, User Name = {}, Code: {}",
                    entity.getId(),
                    entity.getWallet().getName(), entity.getWallet().getUserName(),
                    entity.getWallet().getCode(), e
            );
            throw new InternalErrorException(GENERIC_ERROR);
        }
    }

}
