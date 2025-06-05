package com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository.impl;

import com.julianoclsantos.walletassignmentservice.application.port.out.WalletRepository;
import com.julianoclsantos.walletassignmentservice.domain.model.Wallet;
import com.julianoclsantos.walletassignmentservice.infrastructure.exception.InternalErrorException;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.WalletMapper;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletEntity;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository.WalletJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
            return jpaRepository.findByUserNameAndName(userName, walletName)
                    .map(i -> mapper.toDomain(i));
        } catch (Exception e) {
            log.error("Failed to find Wallet. UserName: {}", userName, e);
            throw new InternalErrorException(GENERIC_ERROR);
        }
    }

    @Override
    public Optional<Wallet> findByCode(String walletCode) {
        try {
            return jpaRepository.findByCode(walletCode)
                    .map(i -> mapper.toDomain(i));
        } catch (Exception e) {
            log.error("Failed to find wallet. Wallet Code: {}", walletCode, e);
            throw new InternalErrorException(GENERIC_ERROR);
        }
    }

    @Override
    public Page<Wallet> searchAll(String userName, LocalDate start, LocalDate end, Pageable pageable) {
        try {
            var startDate = isNull(start) ? null : LocalDateTime.of(start, LocalTime.of(0, 0, 0));
            var endDate = isNull(end) ? null : LocalDateTime.of(end, LocalTime.of(23, 59, 59));

            log.info("msg=Getting all wallet when UserName={}, start={}, end={}", userName, startDate, endDate);

            return jpaRepository.searchAll(userName, startDate, endDate, pageable)
                    .map(i -> mapper.toDomain(i));
        } catch (Exception e) {
            log.error("Failed to search wallets. UserName: {}", userName, e);
            throw new InternalErrorException(GENERIC_ERROR);
        }
    }

    @Override
    public BigDecimal balanceHistory(String walletCode, LocalDate start, LocalDate end) {
        try {
            var startDate = isNull(start) ? null : LocalDateTime.of(start, LocalTime.of(0, 0, 0));
            var endDate = isNull(end) ? null : LocalDateTime.of(end, LocalTime.of(23, 59, 59));

            log.info(
                    "msg=Getting wallet balance history when walletCode={}, start={}, end={}",
                    walletCode, startDate, endDate
            );

            return jpaRepository.balanceHistory(walletCode, startDate, endDate);
        } catch (Exception e) {
            log.error("Failed to get wallet balance history when walletCode={}, start={}, end={}",
                    walletCode, start, end, e);
            throw new InternalErrorException(GENERIC_ERROR);
        }
    }


}
