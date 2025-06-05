package com.julianoclsantos.walletassignmentservice.application.port.in;

import com.julianoclsantos.walletassignmentservice.application.dto.WalletBalanceDTO;
import com.julianoclsantos.walletassignmentservice.application.dto.WalletBalanceHistoryDTO;
import com.julianoclsantos.walletassignmentservice.application.dto.WalletDTO;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletDepositRequest;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletRequest;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletWithdrawRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface WalletService {

    void create(WalletRequest request);

    WalletBalanceDTO getBalance(String walletCode);

    Page<WalletDTO> list(String userName, LocalDate createAtStart, LocalDate createAtEnd, Pageable pageable);

    WalletBalanceHistoryDTO balanceHistory(String userName, LocalDate createAtStart, LocalDate createAtEnd);

    void deposit(WalletDepositRequest request);

    void withdraw(WalletWithdrawRequest request);
}
