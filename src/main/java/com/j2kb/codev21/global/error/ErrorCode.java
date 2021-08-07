package com.j2kb.codev21.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Member
    EMAIL_DUPLICATION(400, "M001", "중복된 아이디입니다."),
    MEMBER_IN_ACTIVE(400, "M002", "회원이 정지된 상태입니다."),
    MEMBER_NOT_FOUND(404, "M003", "회원을 찾을 수 없습니다."),
    MEMBER_LOGOUT(400, "M004", "로그아웃된 상태입니다."),
    GITHUB_DUPLICATION(400, "M005", "기존 인증을 받은 깃허브아이디입니다."),
    NOT_ORGINAZATION(400, "M006", "J2KB 조직에 소속되지 않은 깃허브 계정입니다."),

    // Common
    BAD_REQUEST(400, "COMMON_001", "입력값이 잘못되었습니다."),
    MEMBER_DUPLICATION(400, "COMMON_001", "중복된 회원입니다."),
    VALIDATION(400, "COMMON_003", ""),
    METHOD_NOT_ALLOWED(405, "COMMON_004", " Invalid HTTP Method"),
    INTERNAL_SERVER_ERROR(500, "COMMON_005", "Server Error"),
    
    ////InvalidValue
    //Common
    INVALID_INPUT_VALUE(400, "INVALID_C001", " Invalid Input Value"),
    //USER
    NO_USER_FOUND(400, "INVALID_M001", "회원을 찾을 수 없습니다."),
    //BOARD
    NO_BOARD_FOUND(400, "INVALID_B001", "게시글을 찾을 수 없습니다."),
    //Vote
    NO_VOTE_FOUND(400, "INVALID_V001", "투표를 찾을 수 없습니다."),
    VOTE_DUPLICATION(400, "INVALID_V002", "중복된 날짜의 투표가 존재합니다."),
    //BoardVote
    NO_BOARDVOTE_FOUND(400, "INVALID_BV001", "게시글을 찾을 수 없습니다."),
    BOARDVOTE_DUPLICATION(400, "INVALID_BV002", "투표에 중복된 게시글이 존재합니다."),
    //UserBoardVote
    NO_USERBOARDVOTE_FOUND(400, "INVALID_UBV001", "회원의 투표를 찾을 수 없습니다."),
    USERBOARDVOTE_DUPLICATION(400, "INVALID_UBV001", "중복된 회원의 투표입니다."),
    
    // Auth
    AUTHENTICATION_FAILED(401, "AUTH_001", "인증에 실패하였습니다."),
    LOGIN_FAILED(401, "AUTH_001", "아이디 혹은 비밀번호가 틀립니다."),
    INVALID_JWT_TOKEN(401, "AUTH003", "유효하지 않은 토큰입니다."),
    EXPIRED_JWT_TOKE(401,"AUTH004","만료된 토큰입니다."),
    ACCESS_DENIED(401, "AUTH005","접근 권한이 없습니다."),
    NOT_HAVE_TOKEN(400, "AUTH006", "토큰이 존재하지 않습니다.");

    private int status;
    private final String code;

    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
