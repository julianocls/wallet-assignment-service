package com.julianoclsantos.walletassignmentservice.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private int statusCode;
    private String statusName;
    private String code;
    private String message;
    private LocalDateTime timestamp;

}
