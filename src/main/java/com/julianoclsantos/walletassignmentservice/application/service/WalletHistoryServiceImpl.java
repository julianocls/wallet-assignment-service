package com.julianoclsantos.walletassignmentservice.application.service;

import com.julianoclsantos.walletassignmentservice.application.port.in.WalletHistoryService;
import com.julianoclsantos.walletassignmentservice.application.port.out.WalletHistoryRepository;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.CycleAvoidingMappingContext;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.WalletHistoryMapper;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.WalletMapper;
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
    private final WalletMapper mapper;
    private final WalletHistoryMapper walletHistoryMapper;
    private final CycleAvoidingMappingContext context;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void create(WalletHistoryEntity entity) {

        log.info("Create walletHistoryId={}", entity.getWallet().getId());

        repository.save(entity);

    }

}
