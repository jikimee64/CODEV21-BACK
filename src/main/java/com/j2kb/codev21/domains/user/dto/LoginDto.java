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
        @Size(min = 3, max = 50)
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

