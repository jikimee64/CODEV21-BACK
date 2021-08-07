package com.j2kb.codev21.domains.user.exception;

import com.j2kb.codev21.global.error.ErrorCode;

public class NotHaveAccessTokenException extends RuntimeException{

    public NotHaveAccessTokenException() {
        super(ErrorCode.NOT_HAVE_TOKEN.getMessage());
    }

    public NotHaveAccessTokenException(Exception ex) {
        super(ex);
    }
}

