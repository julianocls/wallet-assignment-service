package com.julianoclsantos.walletassignmentservice.application.port.out;

import brave.internal.collect.UnsafeArrayMap;
import com.julianoclsantos.walletassignmentservice.application.dto.WalletBalanceHistoryDTO;
import com.julianoclsantos.walletassignmentservice.domain.model.Wallet;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface WalletRepository {

    void save(WalletEntity walletEntity);

    Optional<Wallet> findByUserNameAndWalletName(String code, String walletName);

    Optional<Wallet> findByCode(String code);

    Page<Wallet> searchAll(String name, LocalDate start, LocalDate end, Pageable pageable);

    Optional<Wallet> balanceHistory(String userName, LocalDate createAtStart, LocalDate createAtEnd);
}
