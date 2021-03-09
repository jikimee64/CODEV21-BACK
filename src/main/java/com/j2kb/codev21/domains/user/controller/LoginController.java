package com.j2kb.codev21.domains.user.controller;

import com.j2kb.codev21.domains.user.domain.RedisToken;
import com.j2kb.codev21.domains.user.domain.Token;
import com.j2kb.codev21.domains.user.dto.JwtTokenDto;
import com.j2kb.codev21.domains.user.dto.LoginDto;
import com.j2kb.codev21.domains.user.dto.LoginDto.LoginRes;
import com.j2kb.codev21.domains.user.dto.LoginDto.LogoutCheck;
import com.j2kb.codev21.domains.user.dto.LoginDto.RefreshRes;
import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.dto.UserDto.SelectUserRes;
import com.j2kb.codev21.domains.user.service.LoginService;
import com.j2kb.codev21.global.common.CommonResponse;
import com.j2kb.codev21.global.jwt.JwtFilter;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private final RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/login")
    public CommonResponse<LoginRes> login(
        @Valid @RequestBody LoginDto.LoginReq loginDto,
        HttpServletResponse response) {

        Map<String, Object> map = loginService.login(loginDto);

        RedisToken retok = new RedisToken();
        retok.setUsername(loginDto.getEmail());
        retok.setRefreshToken(String.valueOf(map.get(Token.REFRESH_TOKEN.getName())));

        //generate Token and save in redis
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        vop.set(loginDto.getEmail(), retok);

        Cookie cookie = new Cookie("refreshToken", (String) map.get(Token.REFRESH_TOKEN.getName()));
        cookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return CommonResponse.<LoginRes>builder()
            .code("200")
            .message("ok")
            .data(
                LoginRes.builder()
                .accessToken(String.valueOf(map.get(Token.ACCESS_TOKEN.getName())))
                .userId((Long) map.get("id"))
                .build()
            ).build();
    }

    @PostMapping("/logout")
    public CommonResponse<LogoutCheck>logout(
        @RequestBody LoginDto.LogoutReq logoutReq) {

        String username = loginService.logout(logoutReq);
        String accessToken = logoutReq.getAccessToken();

        try {
            if (redisTemplate.opsForValue().get(username) != null) {
                //delete refresh token
                redisTemplate.delete(username);
            }
        } catch (IllegalArgumentException e) {
            log.warn("user does not exist");
        }

        /**
         * cache logout token for 10 minutes!
         * Access token Blacklist에 30분동안(access token 만료 시간) 올라간다. 로그아웃한 access Token으로는 30분동안 사용 불가
         */
        //key, value 저장
        RedisToken redisToken = new RedisToken();
        redisToken.setAccessToken(accessToken);
        redisTemplate.opsForValue().set(accessToken, redisToken);
        redisTemplate.expire(accessToken, 30 * 6 * 1000, TimeUnit.MILLISECONDS);

        return CommonResponse.<LogoutCheck>builder()
            .code("200")
            .message("ok")
            .data(LogoutCheck.builder().resultLogout(true).build())
            .build();
    }


    @PostMapping("/refresh")
    public CommonResponse<RefreshRes> requestForNewAccessToken(
        @RequestBody LoginDto.RefreshReq dto) {

        String newAccessToken = loginService.provideNewAccessToken(dto);

        return CommonResponse.<RefreshRes>builder()
                .code("200")
                .message("ok")
                .data(RefreshRes.builder().accessToken(newAccessToken).build()).build();
    }
}