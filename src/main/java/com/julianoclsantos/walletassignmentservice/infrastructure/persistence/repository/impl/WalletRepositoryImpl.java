package com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository.impl;

import com.julianoclsantos.walletassignmentservice.application.port.out.WalletRepository;
import com.julianoclsantos.walletassignmentservice.domain.model.Wallet;
import com.julianoclsantos.walletassignmentservice.infrastructure.exception.InternalErrorException;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.CycleAvoidingMappingContext;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.WalletMapper;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletEntity;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository.WalletJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static com.julianoclsantos.walletassignmentservice.domain.enums.MessageEnum.GENERIC_ERROR;
import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletRepositoryImpl implements WalletRepository {

    private final WalletJpaRepository jpaRepository;
    private final WalletMapper mapper;
    private final CycleAvoidingMappingContext context;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void save(WalletEntity entity) {
        try {
            jpaRepository.save(entity);
        } catch (Exception e) {
            log.error(
                    "Failed to save wallet. Name: {}, User Name = {}, Code: {}",
                    entity.getName(), entity.getUserName(), entity.getCode(), e
            );
            throw new InternalErrorException(GENERIC_ERROR);
        }
    }

    @Override
    public Optional<Wallet> findByUserNameAndWalletName(String userName, String walletName) {
        try {
            return jpaRepository.findByUserNameAndName(userName,  walletName)
                    .map(i -> mapper.toDomain(i, context));
        } catch (Exception e) {
            log.error("Failed to find Wallet. UserName: {}", userName, e);
            throw new InternalErrorException(GENERIC_ERROR);
        }
    }

    @Override
    public Page<Wallet> searchAll(String userName, LocalDate start, LocalDate end, Pageable pageable) {
        try {
            var startDate = isNull(start) ? null : LocalDateTime.of(start, LocalTime.MIN);
            var endDate = isNull(end) ? null : LocalDateTime.of(end, LocalTime.MAX);

            log.info("msg=Getting all wallet when UserName={}, start={}, end={}", userName, startDate, endDate);

            return jpaRepository.searchAll(userName, startDate, endDate, pageable)
                    .map(i -> mapper.toDomain(i, context));
        } catch (Exception e) {
            log.error("Failed to search wallets. UserName: {}", userName, e);
            throw new InternalErrorException(GENERIC_ERROR);
        }
    }
}
