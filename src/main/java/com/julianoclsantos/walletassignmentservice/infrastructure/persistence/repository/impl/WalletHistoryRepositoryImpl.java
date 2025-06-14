package com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository.impl;

import com.julianoclsantos.walletassignmentservice.application.port.out.WalletHistoryRepository;
import com.julianoclsantos.walletassignmentservice.domain.enums.CacheNames;
import com.julianoclsantos.walletassignmentservice.domain.enums.OperationStatusEnum;
import com.julianoclsantos.walletassignmentservice.domain.model.WalletHistory;
import com.julianoclsantos.walletassignmentservice.infrastructure.exception.InternalErrorException;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.WalletHistoryMapper;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletHistoryEntity;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository.WalletHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.julianoclsantos.walletassignmentservice.domain.enums.MessageEnum.GENERIC_ERROR;
import static com.julianoclsantos.walletassignmentservice.shared.ErrorUtils.toFlatStackTrace;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletHistoryRepositoryImpl implements WalletHistoryRepository {

    private final WalletHistoryJpaRepository jpaRepository;
    private final WalletHistoryMapper mapper;

    @Override
    public void save(WalletHistoryEntity entity) {
        try {
            jpaRepository.save(entity);
        } catch (Exception e) {
            log.error(
                    "Failed to save walletHistory. ID={}, Name: {}, User Name = {}, Code: {}, error={}",
                    entity.getId(),
                    entity.getWallet().getName(), entity.getWallet().getUserName(),
                    entity.getWallet().getCode(), toFlatStackTrace(e, 3)
            );
            throw new InternalErrorException(GENERIC_ERROR);
        }
    }

    @CachePut(value = CacheNames.WALLET_TRANSACTION, key = "#transactionCode")
    @Override
    public Optional<WalletHistory> updateOperationStatus(String transactionCode) {
        try {
            var entity = jpaRepository.findByTransactionCode(transactionCode).orElseThrow();
            log.info("Updating wallet TransactionCode={}", transactionCode);

            entity.applyOperationStatus(OperationStatusEnum.FINISHED);
            return Optional.ofNullable(mapper.toDomain(entity));
        } catch (Exception e) {
            log.error("Failed to update wallet history. Transaction Code: {}, error={}", transactionCode, toFlatStackTrace(e, 3));
            throw new InternalErrorException(GENERIC_ERROR);
        }
    }

}
