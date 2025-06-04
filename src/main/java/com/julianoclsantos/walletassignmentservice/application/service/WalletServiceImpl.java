package com.julianoclsantos.walletassignmentservice.application.service;

import com.julianoclsantos.walletassignmentservice.application.port.in.WalletService;
import com.julianoclsantos.walletassignmentservice.application.port.out.WalletRepository;
import com.julianoclsantos.walletassignmentservice.domain.exception.BusinessException;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.WalletMapper;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.julianoclsantos.walletassignmentservice.domain.enums.MessageEnum.WALLET_SERVICE_WALLET_NAME_ALREADY_EXISTS;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository repository;
    private final WalletMapper mapper;

    @Override
    public void create(WalletRequest request) {

        log.info("Create wallet={}", request.getName());

        var user = repository.findByUserNameAndWalletName(request.getUserName(), request.getName());
        if (user.isPresent()) {
            log.error("Error creating, wallet {} already exist. UserName={}", request.getName(), request.getUserName());
            throw new BusinessException(WALLET_SERVICE_WALLET_NAME_ALREADY_EXISTS);
        }

        var entity = mapper.toEntity(request);

        repository.save(entity);

    }

}
