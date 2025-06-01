package com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository.impl;

import com.julianoclsantos.walletassignmentservice.application.port.out.WalletRepository;
import com.julianoclsantos.walletassignmentservice.domain.model.Wallet;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletRepositoryImpl implements WalletRepository {
    @Override
    public void save(WalletEntity walletEntity) {

    }

    @Override
    public Optional<Wallet> findByCode(String code) {
        return Optional.empty();
    }

    @Override
    public Page<Wallet> searchAll(String name, LocalDate start, LocalDate end, Pageable pageable) {
        return null;
    }
}
