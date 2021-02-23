package com.j2kb.codev21.domains.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LoginDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class LoginReq {

        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        @Email
        public String email;

        @NotBlank(message = "패스워드는 필수 입력 값입니다.")
        @Size(min = 3, max = 50, message = "세글자 이상 50자 이하로 입력해주세요.")
        public String password;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class LoginRes{
        private String accessToken;
        private Long userId;

    }

}

