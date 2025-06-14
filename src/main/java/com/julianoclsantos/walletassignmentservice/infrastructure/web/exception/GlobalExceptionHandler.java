package com.julianoclsantos.walletassignmentservice.infrastructure.web.exception;

import com.julianoclsantos.walletassignmentservice.domain.exception.BusinessException;
import com.julianoclsantos.walletassignmentservice.infrastructure.exception.InternalErrorException;
import com.julianoclsantos.walletassignmentservice.shared.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.julianoclsantos.walletassignmentservice.domain.enums.MessageEnum.GENERIC_ERROR;
import static com.julianoclsantos.walletassignmentservice.shared.ErrorUtils.toFlatStackTrace;
import static java.time.LocalDateTime.now;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        var status = ex.getStatus();
        var response = buildErrorResponse(status.value(), status.name(), ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalException(InternalErrorException ex) {
        log.error("Internal error {}", toFlatStackTrace(ex, 3));
        var status = ex.getStatus();
        var response = buildErrorResponse(status.value(), status.name(), ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        var status = HttpStatus.BAD_REQUEST;
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .reduce((m1, m2) -> m1 + ", " + m2)
                .orElse("Validation error");

        var response = buildErrorResponse(status.value(), status.name(), "VALIDATION_ERROR", errorMessage);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        log.error("Unhandled exception, error={}", toFlatStackTrace(ex, 3));
        var status = GENERIC_ERROR.getStatus();
        var response = buildErrorResponse(status.value(), status.name(), GENERIC_ERROR.getCode(), GENERIC_ERROR.getMessage());
        return new ResponseEntity<>(response, status);
    }

    private ErrorResponse buildErrorResponse(int statusCode, String statusName, String code, String message) {
        return new ErrorResponse(statusCode, statusName, code, message, now());
    }

}
