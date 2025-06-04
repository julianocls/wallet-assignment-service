package com.julianoclsantos.walletassignmentservice.infrastructure.web.controller;

import com.julianoclsantos.walletassignmentservice.application.dto.WalletBalanceDTO;
import com.julianoclsantos.walletassignmentservice.application.port.in.WalletService;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{walletCode}")
    @ResponseStatus(HttpStatus.OK)
    public WalletBalanceDTO getBalance(@PathVariable String walletCode) {
        return service.getBalance(walletCode);
    }

}
