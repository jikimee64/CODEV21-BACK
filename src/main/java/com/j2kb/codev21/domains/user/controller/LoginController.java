package com.j2kb.codev21.domains.user.controller;

import com.j2kb.codev21.domains.user.dto.JwtTokenDto;
import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.dto.UserDto.selectUserRes;
import com.j2kb.codev21.domains.user.service.LoginService;
import com.j2kb.codev21.global.common.CommonResponse;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public CommonResponse<JwtTokenDto.TokenInRes> login(
        @Valid @RequestBody UserDto.LoginReq loginDto) {
        return CommonResponse.<JwtTokenDto.TokenInRes>builder()
            .code("200")
            .message("ok")
            .data(loginService.login(loginDto)).build();
    }
}