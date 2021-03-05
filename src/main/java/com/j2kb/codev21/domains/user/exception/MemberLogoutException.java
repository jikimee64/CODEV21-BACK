package com.j2kb.codev21.domains.user.exception;

import com.j2kb.codev21.global.error.ErrorCode;

public class MemberLogoutException extends RuntimeException{

    public MemberLogoutException() {
        super(ErrorCode.MEMBER_LOGOUT.getMessage());
    }

    public MemberLogoutException(Exception ex) {
        super(ex);
    }
}

