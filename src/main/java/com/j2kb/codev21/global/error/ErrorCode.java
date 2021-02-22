package com.j2kb.codev21.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Member
    EMAIL_DUPLICATION(400, "M001", "중복된 아이디입니다."),
    MEMBER_IN_ACTIVE(400, "M002", "회원이 정지된 상태입니다."),
    MEMBER_NOT_FOUND(400, "M003", "회원을 찾을 수 없습니다."),

    // Common
    BAD_REQUEST(400, "ERROR_001", "입력값이 잘못되었습니다."),
    MEMBER_DUPLICATION(400, "ERROR_001", "중복된 회원입니다."),

    // Auth
    AUTHENTICATION_FAILED(401, "AUTH_001", "인증에 실패하였습니다."),
    LOGIN_FAILED(401, "AUTH_001", "로그인에 실패하였습니다."),
    INVALID_JWT_TOKEN(401, "AUTH003", "유효하지 않은 토큰입니다.!!"),
    EXPIRED_JWT_TOKE(401,"AUTH004","만료된 토큰입니다."),
    ACCESS_DENIED(401, "AUTH005","접근 권한이 없습니다.");

    private int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
