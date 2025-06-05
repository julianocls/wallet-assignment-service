package com.julianoclsantos.walletassignmentservice.application.service;

import com.julianoclsantos.walletassignmentservice.application.dto.WalletBalanceDTO;
import com.julianoclsantos.walletassignmentservice.application.dto.WalletBalanceHistoryDTO;
import com.julianoclsantos.walletassignmentservice.application.dto.WalletDTO;
import com.julianoclsantos.walletassignmentservice.application.port.in.WalletHistoryService;
import com.julianoclsantos.walletassignmentservice.application.port.in.WalletService;
import com.julianoclsantos.walletassignmentservice.application.port.out.WalletRepository;
import com.julianoclsantos.walletassignmentservice.domain.exception.BusinessException;
import com.julianoclsantos.walletassignmentservice.domain.model.WalletHistory;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.CycleAvoidingMappingContext;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.WalletHistoryMapper;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.WalletMapper;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletDepositRequest;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletRequest;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletTransferRequest;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletWithdrawRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.julianoclsantos.walletassignmentservice.domain.enums.MessageEnum.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository repository;
    private final WalletMapper mapper;
    private final WalletHistoryMapper walletHistoryMapper;
    private final CycleAvoidingMappingContext context;
    private final WalletHistoryService walletHistoryService;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void create(WalletRequest request) {

        log.info("Create wallet={}", request.getName());

        var wallet = repository.findByUserNameAndWalletName(request.getUserName(), request.getName());
        if (wallet.isPresent()) {
            log.info("Error creating, wallet {} already exist. UserName={}", request.getName(), request.getUserName());
            throw new BusinessException(WALLET_SERVICE_WALLET_NAME_ALREADY_EXISTS);
        }

        var entity = mapper.toEntity(request);

        repository.save(entity);

    }

    @Override
    public WalletBalanceDTO getBalance(String walletCode) {
        log.info("Get wallet balance by walletCode={}", walletCode);

        var walletOpt = repository.findByCode(walletCode);
        if (walletOpt.isEmpty()) {
            log.info("Error getting wallet balance, wallet {} not exist.", walletCode);
            throw new BusinessException(WALLET_SERVICE_CODE_NOT_FOUND);
        }

        var wallet = repository.balanceHistory(walletCode, null, null);
        return WalletBalanceDTO.builder()
                .totalAmount(wallet)
                .userName(walletOpt.get().getUserName())
                .walletName(walletOpt.get().getName())
                .build();
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

    @Override
    public WalletBalanceHistoryDTO balanceHistory(String walletCode, LocalDate createAtStart, LocalDate createAtEnd) {
        log.info(
                "msg=Getting wallet balance history when walletCode={}, createAtStart={}, createAtEnd={}",
                walletCode, createAtStart, createAtEnd
        );

        var walletOpt = repository.findByCode(walletCode);
        if (walletOpt.isEmpty()) {
            log.info("Error getting wallet history, wallet {} not exist.", walletCode);
            throw new BusinessException(WALLET_SERVICE_CODE_NOT_FOUND);
        }

        var wallet = repository.balanceHistory(walletCode, createAtStart, createAtEnd);
        return WalletBalanceHistoryDTO.builder()
                .totalAmount(wallet)
                .userName(walletOpt.get().getUserName())
                .walletName(walletOpt.get().getName())
                .build();
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deposit(WalletDepositRequest request) {

        log.info("Deposit wallet={}", request.getWalletCode());

        var walletOpt = repository.findByCode(request.getWalletCode());
        if (walletOpt.isEmpty()) {
            log.info("Error depositing, wallet {} not exist.", request.getWalletCode());
            throw new BusinessException(WALLET_SERVICE_CODE_NOT_FOUND);
        }

        var wallet = walletOpt.get();
        var walletHistory = WalletHistory.toDeposit(request, wallet);
        var walletHistoryEntity = walletHistoryMapper.toEntity(walletHistory, context);

        walletHistoryService.create(walletHistoryEntity);

    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void withdraw(WalletWithdrawRequest request) {

        log.info("Withdraw wallet={}", request.getWalletCode());

        var walletOpt = repository.findByCode(request.getWalletCode());
        if (walletOpt.isEmpty()) {
            log.info("Error withdrawing, wallet {} not exist.", request.getWalletCode());
            throw new BusinessException(WALLET_SERVICE_CODE_NOT_FOUND);
        }

        var balance = repository.balanceHistory(request.getWalletCode(), null, null);
        if (request.getAmount().compareTo(balance) > 0) {
            log.info("Error withdrawing, wallet {} no balance.", request.getWalletCode());
            throw new BusinessException(WALLET_SERVICE_NO_BALANCE_WITHDRAW);
        }

        var wallet = walletOpt.get();
        var walletHistory = WalletHistory.toWithdraw(request, wallet);
        var walletHistoryEntity = walletHistoryMapper.toEntity(walletHistory, context);

        walletHistoryService.create(walletHistoryEntity);

    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void transfer(WalletTransferRequest request) {

        log.info(
                "Transfer walletCode={} for walletCode={}",
                request.getOriginWalletCode(), request.getDestinationWalletCode()
        );

        var walletOriginOpt = repository.findByCode(request.getOriginWalletCode());
        if (walletOriginOpt.isEmpty()) {
            log.info("Error transfer, wallet origin {} not exist.", request.getOriginWalletCode());
            throw new BusinessException(WALLET_SERVICE_CODE_NOT_FOUND);
        }

        var walletDestinationOpt = repository.findByCode(request.getDestinationWalletCode());
        if (walletDestinationOpt.isEmpty()) {
            log.info("Error transfer, wallet destination {} not exist.", request.getDestinationWalletCode());
            throw new BusinessException(WALLET_SERVICE_CODE_NOT_FOUND);
        }

        var balance = repository.balanceHistory(request.getOriginWalletCode(), null, null);
        if (request.getAmount().compareTo(balance) > 0) {
            log.info("Error transfer, wallet {} no balance.", request.getOriginWalletCode());
            throw new BusinessException(WALLET_SERVICE_NO_BALANCE_WITHDRAW);
        }

        var walletOrigin = walletOriginOpt.get();
        var requestOrigin = WalletWithdrawRequest.builder()
                .walletCode(request.getOriginWalletCode())
                .amount(request.getAmount())
                .build();

        var walletHistoryOrigin = WalletHistory.toOriginTransfer(requestOrigin, walletOrigin);
        var walletHistoryEntityOrigin = walletHistoryMapper.toEntity(walletHistoryOrigin, context);
        walletHistoryService.create(walletHistoryEntityOrigin);

        var walletDestination = walletDestinationOpt.get();
        var requestDestination = WalletDepositRequest.builder()
                .walletCode(request.getDestinationWalletCode())
                .amount(request.getAmount())
                .build();

        var walletHistoryDestination = WalletHistory.toDestinationTransfer(requestDestination, walletDestination, walletOrigin.getId());
        var walletHistoryEntityDestination = walletHistoryMapper.toEntity(walletHistoryDestination, context);
        walletHistoryService.create(walletHistoryEntityDestination);

    }

}
