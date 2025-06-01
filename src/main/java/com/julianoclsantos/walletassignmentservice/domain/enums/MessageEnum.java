package com.julianoclsantos.walletassignmentservice.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum MessageEnum {

    // 5XX
    GENERIC_ERROR("USS0000", INTERNAL_SERVER_ERROR, "Um erro inesperado aconteceu."),

    // 4XX
    WALLET_SERVICE_USER_CODE_NOT_FOUND("WAS0001", NOT_FOUND, "Usuário não encontrado."),
    WALLET_SERVICE_CODE_NOT_FOUND("WAS0002", NOT_FOUND, "Código da Wallet não encontrado."),
    WALLET_SERVICE_SYSTEM_CODE_ALREADY_EXISTS("WAS0004", CONFLICT, "Código da Wallet já existe."),

    ;

    private final String code;
    private final HttpStatus status;
    private final String message;

}
