package com.julianoclsantos.walletassignmentservice.application.service;

import com.julianoclsantos.walletassignmentservice.application.port.in.WalletHistoryService;
import com.julianoclsantos.walletassignmentservice.application.port.out.WalletHistoryRepository;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletHistoryEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalletHistoryServiceImpl implements WalletHistoryService {

    private final WalletHistoryRepository repository;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void create(WalletHistoryEntity entity) {

        log.info("Create walletHistoryId={}", entity.getWallet().getId());

        repository.save(entity);

    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void updateOperationStatus(String transactionCode, String walletCode) {

        log.info("update walletHistory TransactionCode={}", transactionCode);

        repository.updateOperationStatus(transactionCode, walletCode);

    }



}
