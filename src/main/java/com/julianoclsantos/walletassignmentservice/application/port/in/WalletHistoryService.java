package com.julianoclsantos.walletassignmentservice.application.port.in;

import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletHistoryEntity;

public interface WalletHistoryService {

    void create(WalletHistoryEntity entity);

    void updateOperationStatus(String transactionCode, String walletCode);

}
