package com.j2kb.codev21.domains.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDto {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class JoinReq{
        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        @Email(message = "아이디 형식에 맞지 않습니다.")
        @Size(max = 30, message = "아이디는 30자 이하로 입력해주세요.")
        private String email;

        @NotBlank(message = "패스워드는 필수 입력 값입니다.")
        @Size(max = 15, message = "비밀번호는 15자 이하로 입력해주세요.")
        private String password;

        @NotBlank(message = "휴대폰번호는 필수 입력 값입니다.")
        @Size(max = 12, message = "휴대폰번호는 11자로 입력해주세요.")
        private String name;

        @NotBlank(message = "가입기수는 필수 입력 값입니다.")
        @Size(max = 12, message = "가입기수는 11자로 입력해주세요.")
        private String joinGisu;

        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        @Size(min = 2, message = "닉네임은 최소 2자 이상으로 입력해주세요.")
        private String githubId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class UpdateUserReq {

        @NotBlank(message = "패스워드는 필수 입력 값입니다.")
        @Size(min = 3, max = 50)
        private String password;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class UpdateUserByAdminReq {
        @NotBlank(message = "유저상태는 필수 입력 값입니다.")
        private String status;
        @NotBlank(message = "필드는 필수 입력 값입니다.")
        private String field;
        @NotBlank(message = "가입기수는 필수 입력 값입니다.")
        private String joinGisu;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class SelectUserRes {
        private Long id;
        private String email;
        private String name;
        private String joinGisu;
        private String status;
        private String field;
        private String githubId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class UserIdRes {
        private Long id;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class DeleteUserCheckRes {
        private Boolean checkFlag;
    }

}
