package com.julianoclsantos.walletassignmentservice.infrastructure.web.exception;

import com.julianoclsantos.walletassignmentservice.domain.exception.BusinessException;
import com.julianoclsantos.walletassignmentservice.infrastructure.exception.InternalErrorException;
import com.julianoclsantos.walletassignmentservice.shared.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.julianoclsantos.walletassignmentservice.domain.enums.MessageEnum.GENERIC_ERROR;
import static java.time.LocalDateTime.now;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        var status = ex.getStatus();
        var errorResponse = new ErrorResponse(status.value(), status.name(), ex.getCode(), ex.getMessage(), now());
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalException(InternalErrorException ex) {
        log.error(ex.toString());

        var status = ex.getStatus();
        var errorResponse = new ErrorResponse(status.value(), status.name(), ex.getCode(), ex.getMessage(), now());
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error(ex.toString());

        var status = GENERIC_ERROR.getStatus();
        var errorResponse = new ErrorResponse(status.value(), status.name(), GENERIC_ERROR.getCode(), GENERIC_ERROR.getMessage(), now());
        return new ResponseEntity<>(errorResponse, GENERIC_ERROR.getStatus());
    }
}

