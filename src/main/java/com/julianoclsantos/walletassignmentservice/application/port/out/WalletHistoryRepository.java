package com.julianoclsantos.walletassignmentservice.application.port.out;

import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletEntity;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletHistoryEntity;

public interface WalletHistoryRepository {

    void save(WalletHistoryEntity walletHistoryEntity);

}
