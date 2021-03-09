package com.j2kb.codev21.domains.user.exception;

import com.j2kb.codev21.global.error.ErrorCode;

public class MemberDuplicationException extends RuntimeException{

    public MemberDuplicationException() {
        super(ErrorCode.EMAIL_DUPLICATION.getMessage());
    }

    public MemberDuplicationException(Exception ex) {
        super(ex);
    }
}

