package com.j2kb.codev21.global.error.exception;

import com.j2kb.codev21.global.error.ErrorCode;

public class InvalidValueException extends IllegalArgumentException {
	
	private static final long serialVersionUID = 3461484217381311129L;
	
	private ErrorCode errorCode;

    public InvalidValueException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
