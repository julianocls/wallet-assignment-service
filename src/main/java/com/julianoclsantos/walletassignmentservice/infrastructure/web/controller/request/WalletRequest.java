package com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class WalletRequest {

    private String userName;
    private String name;

}
