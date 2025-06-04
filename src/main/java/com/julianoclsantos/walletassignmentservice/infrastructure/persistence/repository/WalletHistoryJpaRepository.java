package com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository;

import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletHistoryJpaRepository extends JpaRepository<WalletHistoryEntity, Long> {

}