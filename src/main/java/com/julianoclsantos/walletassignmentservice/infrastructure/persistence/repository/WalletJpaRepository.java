package com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository;

import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WalletJpaRepository extends JpaRepository<WalletEntity, Long> {

    @Query("""
                SELECT w FROM WalletEntity w
                LEFT JOIN w.histories wh
                WHERE LOWER(w.userName) = LOWER(:userName)
                  AND (:start IS NULL OR :end IS NULL OR (w.createdAt BETWEEN :start AND :end))
            """)
    Page<WalletEntity> searchAll(String userName, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Optional<WalletEntity> findByUserNameAndName(String userName, String walletName);

    Optional<WalletEntity> findByCode(String walletCode);

    @Query("""
                SELECT COALESCE(SUM(CASE WHEN wh.transactionType = 'CREDIT' THEN wh.amount ELSE -wh.amount END), 0)
                FROM WalletEntity w
                LEFT JOIN w.histories wh
                WHERE LOWER(w.code) = LOWER(:walletCode)
                  AND (:start IS NULL OR :end IS NULL OR (wh.createdAt >= :start AND wh.createdAt < :end))
            """)
    BigDecimal balanceHistory(String walletCode, LocalDateTime start, LocalDateTime end);

}