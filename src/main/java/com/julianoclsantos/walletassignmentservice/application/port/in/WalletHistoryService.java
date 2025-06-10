package com.julianoclsantos.walletassignmentservice.application.port.in;

import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletHistoryEntity;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletDepositEvent;

public interface WalletHistoryService {

    void create(WalletHistoryEntity entity);

    void updateOperationStatus(WalletDepositEvent event);
}
