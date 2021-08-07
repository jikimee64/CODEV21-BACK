package com.j2kb.codev21.domains.user.dto;

import com.j2kb.codev21.domains.user.domain.Field;
import com.j2kb.codev21.domains.user.domain.Status;
import com.j2kb.codev21.global.util.Enum;
import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
        @Email(message = "아이디는 이메일 형식에 맞게 작성해주세요.")
        @Size(max = 30, message = "아이디는 30자 이하로 입력해주세요.")
        private String email;

        @NotBlank(message = "패스워드는 필수 입력 값입니다.")
        @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,15}",
            message = "비밀번호는 영문 소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 6자 ~ 15자의 비밀번호여야 합니다.")
        private String password;

        @NotBlank(message = "이름은 필수 입력 값입니다.")
        @Size(min = 2, message = "이름은 최소 2자 이상으로 입력해주세요.")
        private String name;

        @NotBlank(message = "가입기수는 필수 입력 값입니다.")
        @Size(min = 2, max = 2, message = "가입기수는 2자로 입력해주세요. ex)2기")
        private String joinGisu;

        @NotBlank(message = "깃허브 아이디는 필수 입력 값입니다.")
        @Email(message = "깃허브 아이디는 이메일 형식에 맞게 작성해주세요.")
        @Size(max = 30, message = "깃허브 아이디는 30자 이하로 입력해주세요.")
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
        //필드가 Enum에 존재하지 않으면 MethodArgumentNotValidException 발생
        @Enum(enumClass = Status.class, ignoreCase = true)
        private String status;
        @Enum(enumClass = Field.class, ignoreCase = true)
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
        public LocalDateTime createdAt;
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
