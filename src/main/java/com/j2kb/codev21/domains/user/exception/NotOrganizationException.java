package com.j2kb.codev21.domains.user.exception;

import com.j2kb.codev21.global.error.ErrorCode;

public class NotOrganizationException extends RuntimeException{

    public NotOrganizationException() {
        super(ErrorCode.NOT_ORGINAZATION.getMessage());
    }

    public NotOrganizationException(Exception ex) {
        super(ex);
    }
}

