package com.j2kb.codev21.domains.user.controller;

import com.j2kb.codev21.domains.user.domain.Token;
import com.j2kb.codev21.domains.user.dto.JwtTokenDto;
import com.j2kb.codev21.domains.user.dto.LoginDto;
import com.j2kb.codev21.domains.user.dto.LoginDto.LoginRes;
import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.service.LoginService;
import com.j2kb.codev21.global.common.CommonResponse;
import com.j2kb.codev21.global.jwt.JwtFilter;
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
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
        @Valid @RequestBody LoginDto.LoginReq loginDto) {

        LoginRes login = loginService.login(loginDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER,
            "Bearer " + (login.getAccessToken()));

        return new ResponseEntity<>(
            CommonResponse.<LoginRes>builder()
                .code("200")
                .message("ok")
                .data(login).build(),
            httpHeaders,
            HttpStatus.OK);
    }
}