package com.julianoclsantos.walletassignmentservice.application.port.out;

import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletHistoryEntity;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletDepositEvent;

public interface WalletHistoryRepository {

    void save(WalletHistoryEntity walletHistoryEntity);

    void updateOperationStatus(WalletDepositEvent event);

}
