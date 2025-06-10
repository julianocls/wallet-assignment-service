package com.julianoclsantos.walletassignmentservice.application.service;

import com.julianoclsantos.walletassignmentservice.application.dto.WalletBalanceDTO;
import com.julianoclsantos.walletassignmentservice.application.dto.WalletBalanceHistoryDTO;
import com.julianoclsantos.walletassignmentservice.application.dto.WalletDTO;
import com.julianoclsantos.walletassignmentservice.application.port.in.WalletHistoryService;
import com.julianoclsantos.walletassignmentservice.application.port.in.WalletService;
import com.julianoclsantos.walletassignmentservice.application.port.out.WalletRepository;
import com.julianoclsantos.walletassignmentservice.domain.exception.BusinessException;
import com.julianoclsantos.walletassignmentservice.domain.model.Wallet;
import com.julianoclsantos.walletassignmentservice.domain.model.WalletHistory;
import com.julianoclsantos.walletassignmentservice.infrastructure.adapter.kafka.producer.WalletEventProducer;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.WalletHistoryMapper;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.WalletMapper;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.julianoclsantos.walletassignmentservice.domain.enums.MessageEnum.*;
import static com.julianoclsantos.walletassignmentservice.domain.enums.TopicsEnum.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository repository;
    private final WalletMapper mapper;
    private final WalletHistoryMapper walletHistoryMapper;
    private final WalletHistoryService walletHistoryService;
    private final WalletEventProducer walletEventProducer;

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

        var wallet = getWallet(walletCode);

        var balance = repository.balanceHistory(walletCode, null, null);
        return WalletBalanceDTO.builder()
                .totalAmount(balance)
                .userName(wallet.getUserName())
                .walletName(wallet.getName())
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

        var wallet = getWallet(walletCode);

        var balance = repository.balanceHistory(walletCode, createAtStart, createAtEnd);
        return WalletBalanceHistoryDTO.builder()
                .totalAmount(balance)
                .userName(wallet.getUserName())
                .walletName(wallet.getName())
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
        var walletHistoryEntity = walletHistoryMapper.toEntity(walletHistory);

        walletHistoryService.create(walletHistoryEntity);

        var walletDepositEvent = WalletDepositEvent.newBuilder()
                .setWalletCode(request.getWalletCode())
                .setTransactionCode(walletHistoryEntity.getTransactionCode())
                .setAmount(request.getAmount()).build();

        walletEventProducer.send(WALLET_ASSIGNMENT_WALLET_DEPOSIT, walletHistoryEntity.getTransactionCode(), walletDepositEvent);

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

        verifyBalance(request.getWalletCode(), request.getAmount());

        var wallet = walletOpt.get();
        var walletHistory = WalletHistory.toWithdraw(request, wallet);
        var walletHistoryEntity = walletHistoryMapper.toEntity(walletHistory);

        walletHistoryService.create(walletHistoryEntity);

        var walletWithdrawEvent = WalletWithdrawEvent.newBuilder()
                .setWalletCode(request.getWalletCode())
                .setTransactionCode(walletHistoryEntity.getTransactionCode())
                .setAmount(request.getAmount())
                .build();

        walletEventProducer.send(WALLET_ASSIGNMENT_WALLET_WITHDRAW, walletHistoryEntity.getTransactionCode(), walletWithdrawEvent);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void transfer(WalletTransferRequest request) {

        log.info(
                "Transfer walletCode={} for walletCode={}",
                request.getOriginWalletCode(), request.getDestinationWalletCode()
        );

        var walletOrigin = getWallet(request.getOriginWalletCode());
        var walletDestination = getWallet(request.getDestinationWalletCode());
        verifyBalance(request.getOriginWalletCode(), request.getAmount());

        var requestOrigin = WalletWithdrawRequest.toRequest(request.getOriginWalletCode(), request.getAmount());
        var walletHistoryOrigin = WalletHistory.toOriginTransfer(requestOrigin, walletOrigin);
        var walletHistoryEntityOrigin = walletHistoryMapper.toEntity(walletHistoryOrigin);
        walletHistoryService.create(walletHistoryEntityOrigin);

        var requestDestination = WalletDepositRequest.toRequest(request.getDestinationWalletCode(), request.getAmount());
        var walletHistoryDestination = WalletHistory.toDestinationTransfer(requestDestination, walletDestination, walletOrigin.getId());
        var walletHistoryEntityDestination = walletHistoryMapper.toEntity(walletHistoryDestination);
        walletHistoryService.create(walletHistoryEntityDestination);

        var walletTransferEvent = WalletTransferEvent.newBuilder()
                .setOriginWalletCode(request.getOriginWalletCode())
                .setOriginTransactionCode(walletHistoryEntityOrigin.getTransactionCode())
                .setDestinationWalletCode(request.getDestinationWalletCode())
                .setDestinationTransactionCode(walletHistoryEntityDestination.getTransactionCode())
                .setAmount(request.getAmount())
                .build();

        walletEventProducer.send(WALLET_ASSIGNMENT_WALLET_TRANSFER, walletHistoryEntityOrigin.getTransactionCode(), walletTransferEvent);
    }

    private Wallet getWallet(String walletCode) {
        var walletOpt = repository.findByCode(walletCode);
        if (walletOpt.isEmpty()) {
            log.info("Error to getWallet, wallet {} not exist.", walletCode);
            throw new BusinessException(WALLET_SERVICE_CODE_NOT_FOUND);
        }
        return walletOpt.get();
    }

    private void verifyBalance(String walletCode, BigDecimal amount) {
        var balance = repository.balanceHistory(walletCode, null, null);
        if (amount.compareTo(balance) > 0) {
            log.info("Error verifyBalance, wallet {} no balance.", walletCode);
            throw new BusinessException(WALLET_SERVICE_NO_BALANCE_WITHDRAW);
        }
    }

}
