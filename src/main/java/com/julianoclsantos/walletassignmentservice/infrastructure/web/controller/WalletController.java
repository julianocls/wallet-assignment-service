package com.julianoclsantos.walletassignmentservice.infrastructure.web.controller;

import com.julianoclsantos.walletassignmentservice.application.dto.WalletBalanceDTO;
import com.julianoclsantos.walletassignmentservice.application.dto.WalletBalanceHistoryDTO;
import com.julianoclsantos.walletassignmentservice.application.port.in.WalletService;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletDepositRequest;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletRequest;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/balanceHistory")
    @ResponseStatus(HttpStatus.OK)
    public WalletBalanceHistoryDTO balanceHistory(
            @RequestParam String userName,
            @RequestParam LocalDate createAtStart,
            @RequestParam LocalDate createAtEnd) {
        return service.balanceHistory(userName, createAtStart, createAtEnd);
    }

    @GetMapping("/{walletCode}")
    @ResponseStatus(HttpStatus.OK)
    public WalletBalanceDTO getBalance(@PathVariable String walletCode) {
        return service.getBalance(walletCode);
    }

}
