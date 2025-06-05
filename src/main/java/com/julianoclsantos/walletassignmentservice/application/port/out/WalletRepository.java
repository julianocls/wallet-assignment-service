package com.julianoclsantos.walletassignmentservice.application.port.out;

import com.julianoclsantos.walletassignmentservice.domain.model.Wallet;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface WalletRepository {

    void save(WalletEntity walletEntity);

    Optional<Wallet> findByUserNameAndWalletName(String code, String walletName);

    Optional<Wallet> findByCode(String code);

    Page<Wallet> searchAll(String name, LocalDate start, LocalDate end, Pageable pageable);

    BigDecimal balanceHistory(String walletCode, LocalDate start, LocalDate end);
}
