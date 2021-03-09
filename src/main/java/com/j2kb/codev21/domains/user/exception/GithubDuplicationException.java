package com.j2kb.codev21.domains.user.exception;

import com.j2kb.codev21.global.error.ErrorCode;

public class GithubDuplicationException extends RuntimeException{

    public GithubDuplicationException() {
        super(ErrorCode.GITHUB_DUPLICATION.getMessage());
    }

    public GithubDuplicationException(Exception ex) {
        super(ex);
    }
}



