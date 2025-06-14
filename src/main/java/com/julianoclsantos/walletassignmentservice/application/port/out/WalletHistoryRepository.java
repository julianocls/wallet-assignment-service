package com.julianoclsantos.walletassignmentservice.application.port.out;

import com.julianoclsantos.walletassignmentservice.domain.model.WalletHistory;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletHistoryEntity;

import java.util.Optional;

public interface WalletHistoryRepository {

    void save(WalletHistoryEntity walletHistoryEntity);

    Optional<WalletHistory> updateOperationStatus(String transactionCode);

}
