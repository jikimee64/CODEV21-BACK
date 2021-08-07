package com.j2kb.codev21.global.common;

import com.j2kb.codev21.global.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String code;
    private String message;
    private int status;

    private ErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
    }
    
    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }
}

