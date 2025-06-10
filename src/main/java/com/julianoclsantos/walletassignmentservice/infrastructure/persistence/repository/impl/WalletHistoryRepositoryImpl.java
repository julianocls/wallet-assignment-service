package com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository.impl;

import com.julianoclsantos.walletassignmentservice.application.port.out.WalletHistoryRepository;
import com.julianoclsantos.walletassignmentservice.domain.enums.OperationStatusEnum;
import com.julianoclsantos.walletassignmentservice.infrastructure.exception.InternalErrorException;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletHistoryEntity;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository.WalletHistoryJpaRepository;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletDepositEvent;
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

    @Override
    public void updateOperationStatus(WalletDepositEvent event) {
        try {
            var entity = jpaRepository.findByTransactionCode(event.getTransactionCode()).orElseThrow();
            log.info("Updating wallet history={} TransactionCode={}", event.getWalletCode(), event.getTransactionCode());

            entity.applyOperationStatus(OperationStatusEnum.FINISHED);
        } catch (Exception e) {
            log.error("Failed to update wallet history. Wallet Code: {}, Transaction Code: {}", event.getWalletCode(), event.getTransactionCode(), e);
            throw new InternalErrorException(GENERIC_ERROR);
        }
    }

}
