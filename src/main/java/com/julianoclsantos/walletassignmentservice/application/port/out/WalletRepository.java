package com.julianoclsantos.walletassignmentservice.application.port.out;

import com.julianoclsantos.walletassignmentservice.domain.model.Wallet;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface WalletRepository {

    void save(WalletEntity walletEntity);

    Optional<Wallet> findByUserNameAndWalletName(String code, String walletName);

    Page<Wallet> searchAll(String name, LocalDate start, LocalDate end, Pageable pageable);

}
