package com.julianoclsantos.walletassignmentservice.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationStatusEnum {

    CREATED("CREATED", "Criado"),
    PROCESSING("PROCESSING", "Processando"),
    FINISHED("FINISHED", "Finalizado"),
    ERROR("ERROR", "Erro"),
    CANCELED("CANCELED", "Cancelado")

    ;

    private final String key;
    private final String description;

}
