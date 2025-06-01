package com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository;

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

//    @Query("""
//             SELECT w FROM WalletEntity w
//             join WalletHistoryEntity wh
//             WHERE LOWER(u.name) = LOWER(:name)
//                   AND (:start IS NULL OR :end IS NULL OR (u.createdAt BETWEEN :start AND :end))
//            """)
//    Page<WalletEntity> searchAll(String name, String email, LocalDateTime start, LocalDateTime end, Pageable pageable);
//
//    Optional<WalletEntity> findByCode(String code);

}