package com.j2kb.codev21.domains.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.j2kb.codev21.domains.user.domain.Field;
import com.j2kb.codev21.domains.user.domain.Status;
import com.j2kb.codev21.domains.user.domain.Token;
import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.domains.user.dto.LoginDto.LoginReq;
import com.j2kb.codev21.domains.user.dto.LoginDto.LogoutReq;
import com.j2kb.codev21.domains.user.dto.LoginDto.RefreshReq;
import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.dto.UserDto.JoinReq;
import com.j2kb.codev21.domains.user.dto.UserDto.SelectUserRes;
import com.j2kb.codev21.domains.user.dto.UserDto.UpdateUserReq;
import com.j2kb.codev21.domains.user.dto.UserDto.UserIdRes;
import com.j2kb.codev21.domains.user.dto.mapper.UserMapper;
import com.j2kb.codev21.domains.user.exception.MemberNotFoundException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
//@Transactional
class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Autowired
    UserMapper userMapper;

    @BeforeEach
    public void before() {

    }

    @Test
    @Order(1)
    @DisplayName("회원가입")
    void shouldSortedInOrderOfGrade() {
        /* given */
        JoinReq dto = JoinReq.builder()
            .email("j2kb@j2kb.com")
            .password("1q2w3e4r1!")
            .name("이름")
            .joinGisu("1기")
            .githubId("jikimee64@gmail.com")
            .build();

        /* when */
        userService.joinUser(userMapper.joinDtoToEntity(dto));
    }

    @Test
    @Order(2)
    @DisplayName("로그인")
    void login() {
        /* given */
        LoginReq dto = LoginReq.builder()
            .email("j2kb@j2kb.com")
            .password("1q2w3e4r1!")
            .build();

        /* when */
        Map<String, Object> login = loginService.login(dto);

        /* then */
        assertAll(
            () -> assertNotNull(login.get(Token.ACCESS_TOKEN.getName())),
            () -> assertNotNull(login.get(Token.REFRESH_TOKEN.getName())),
            () -> assertNotNull(login.get("id"))
        );
    }

    @Test
    @Order(4)
    @DisplayName("로그아웃")
    void logout() {
        /* given */
        LogoutReq dto = LogoutReq.builder()
            .accessToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqMmtiQGoya2IuY29tIiwiYXV0aCI6IiIsImV4cCI6MTYxNDAxNTk2Nn0.Yp31VtAyFvfyuZh72Qj_pSF3vYsVr4ZfRrM5Kbk4KAAMJDxIWb0SbYXfY9X1rwTkdTwt5lWn_cRkjldfqZFTrQ")
            .build();

        /* when */
        String logout = loginService.logout(dto);

        /* then */
        assertNotNull(logout);
    }

}