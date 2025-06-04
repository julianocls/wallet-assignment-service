package com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository;

import brave.internal.collect.UnsafeArrayMap;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WalletJpaRepository extends JpaRepository<WalletEntity, Long> {

    @Query("""
                SELECT w FROM WalletEntity w
                JOIN w.histories wh
                WHERE LOWER(w.name) = LOWER(:name)
                  AND (:start IS NULL OR :end IS NULL OR (w.createdAt BETWEEN :start AND :end))
            """)
    Page<WalletEntity> searchAll(String name, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Optional<WalletEntity> findByUserNameAndName(String userName, String walletName);

    Optional<WalletEntity> findByCode(String walletCode);
}