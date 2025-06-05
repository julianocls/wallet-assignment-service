package com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WalletRequest {

    @NotNull(message = "Please provide the User name")
    private String userName;

    @NotNull(message = "Please provide the Name")
    private String name;

}
