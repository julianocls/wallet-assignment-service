package com.julianoclsantos.walletassignmentservice.application.port.in;

import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletRequest;

public interface WalletService {

    void create(WalletRequest request);

}
