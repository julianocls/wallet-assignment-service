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
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.CycleAvoidingMappingContext;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.WalletHistoryMapper;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.WalletMapper;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletDepositRequest;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.julianoclsantos.walletassignmentservice.domain.enums.MessageEnum.WALLET_SERVICE_CODE_NOT_FOUND;
import static com.julianoclsantos.walletassignmentservice.domain.enums.MessageEnum.WALLET_SERVICE_WALLET_NAME_ALREADY_EXISTS;

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

    @Override
    public WalletBalanceHistoryDTO balanceHistory(String userName, LocalDate createAtStart, LocalDate createAtEnd) {
        log.info(
                "msg=Getting wallet balance history when userName={}, createAtStart={}, createAtEnd={}",
                userName, createAtStart, createAtEnd
        );

        var wallet = repository.balanceHistory(userName, createAtStart, createAtEnd)
                .orElse(new Wallet());

        return WalletBalanceHistoryDTO.toTotalize(wallet);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deposit(WalletDepositRequest request) {

        log.info("Deposit wallet={}", request.getWalletCode());

        var walletOpt = repository.findByCode(request.getWalletCode());
        if (walletOpt.isEmpty()) {
            log.error("Error depositing, wallet {} not exist.", request.getWalletCode());
            throw new BusinessException(WALLET_SERVICE_CODE_NOT_FOUND);
        }

        var wallet = walletOpt.get();
        var walletHistory = WalletHistory.toDeposit(request, wallet);
        var walletHistoryEntity = walletHistoryMapper.toEntity(walletHistory, context);

        walletHistoryService.create(walletHistoryEntity);

    }

}
