package com.julianoclsantos.walletassignmentservice.application.port.in;

import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletHistoryEntity;

public interface WalletHistoryService {

    void create(WalletHistoryEntity entity);

}
