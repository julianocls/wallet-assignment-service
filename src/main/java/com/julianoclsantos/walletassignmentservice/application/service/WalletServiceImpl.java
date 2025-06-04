package com.julianoclsantos.walletassignmentservice.application.service;

import com.julianoclsantos.walletassignmentservice.application.dto.WalletBalanceDTO;
import com.julianoclsantos.walletassignmentservice.application.dto.WalletDTO;
import com.julianoclsantos.walletassignmentservice.application.port.in.WalletService;
import com.julianoclsantos.walletassignmentservice.application.port.out.WalletRepository;
import com.julianoclsantos.walletassignmentservice.domain.exception.BusinessException;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.WalletMapper;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.julianoclsantos.walletassignmentservice.domain.enums.MessageEnum.WALLET_SERVICE_WALLET_NAME_ALREADY_EXISTS;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository repository;
    private final WalletMapper mapper;

    @Transactional(rollbackOn = Exception.class)
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

    @Override
    public WalletBalanceDTO getBalance(String walletCode) {
        log.info("Get wallet balance by walletCode={}", walletCode);

        return repository.findByCode(walletCode)
                .map(WalletBalanceDTO::toWalletBalanceDTO)
                .orElse(null);
    }

    @Override
    public Page<WalletDTO> list(String userName, LocalDate createAtStart, LocalDate createAtEnd, Pageable pageable) {
        log.info(
                "msg=Listing all user wallet when userName={}, createAtStart={}, createAtEnd={}",
                userName, createAtStart, createAtEnd
        );

        return repository.searchAll(userName, createAtStart, createAtEnd, pageable)
                .map(WalletDTO::toWalletDTO);
    }


}
