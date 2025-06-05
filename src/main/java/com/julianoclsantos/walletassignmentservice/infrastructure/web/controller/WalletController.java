package com.julianoclsantos.walletassignmentservice.infrastructure.web.controller;

import com.julianoclsantos.walletassignmentservice.application.dto.WalletBalanceDTO;
import com.julianoclsantos.walletassignmentservice.application.dto.WalletBalanceHistoryDTO;
import com.julianoclsantos.walletassignmentservice.application.dto.WalletDTO;
import com.julianoclsantos.walletassignmentservice.application.port.in.WalletService;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletDepositRequest;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletRequest;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletWithdrawRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody WalletRequest request) {
        service.create(request);
    }

    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.CREATED)
    public void deposit(@RequestBody WalletDepositRequest request) {
        service.deposit(request);
    }

    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.CREATED)
    public void withdraw(@RequestBody WalletWithdrawRequest request) {
        service.withdraw(request);
    }

    @GetMapping("/balanceHistory")
    @ResponseStatus(HttpStatus.OK)
    public WalletBalanceHistoryDTO balanceHistory(
            @RequestParam String walletCode,
            @RequestParam(required = false) LocalDate createAtStart,
            @RequestParam(required = false) LocalDate createAtEnd) {
        return service.balanceHistory(walletCode, createAtStart, createAtEnd);
    }

    @GetMapping("/{walletCode}")
    @ResponseStatus(HttpStatus.OK)
    public WalletBalanceDTO getBalance(@PathVariable String walletCode) {
        return service.getBalance(walletCode);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<WalletDTO> list(
            @RequestParam String userName,
            @RequestParam(required = false) LocalDate createAtStart,
            @RequestParam(required = false) LocalDate createAtEnd,
            @PageableDefault(size = 15, sort = "userName", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.list(userName, createAtStart, createAtEnd, pageable);
    }

}
