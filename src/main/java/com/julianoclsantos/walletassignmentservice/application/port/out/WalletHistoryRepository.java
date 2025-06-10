package com.julianoclsantos.walletassignmentservice.application.port.out;

import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletHistoryEntity;

public interface WalletHistoryRepository {

    void save(WalletHistoryEntity walletHistoryEntity);

    void updateOperationStatus(String transactionCode, String walletCode);

}
