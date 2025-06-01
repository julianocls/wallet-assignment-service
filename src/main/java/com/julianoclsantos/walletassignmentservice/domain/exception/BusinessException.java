package com.julianoclsantos.walletassignmentservice.domain.exception;

import com.julianoclsantos.walletassignmentservice.domain.enums.MessageEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BusinessException extends RuntimeException {

    private final String code;
    private final HttpStatus status;
    private final String message;

    public BusinessException(MessageEnum definitionEnum) {
        this.code = definitionEnum.getCode();
        this.status = definitionEnum.getStatus();
        this.message = definitionEnum.getMessage();
    }

}
