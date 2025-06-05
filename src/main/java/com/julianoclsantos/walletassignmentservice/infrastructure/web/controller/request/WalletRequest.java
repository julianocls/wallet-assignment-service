package com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WalletRequest {

    private String userName;
    private String name;

}
