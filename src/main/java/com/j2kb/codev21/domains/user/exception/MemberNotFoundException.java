package com.j2kb.codev21.domains.user.exception;

import com.j2kb.codev21.global.error.ErrorCode;

public class MemberNotFoundException extends RuntimeException{

    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND.getMessage());
    }

    public MemberNotFoundException(Exception ex) {
        super(ex);
    }
}

