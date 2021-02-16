package com.j2kb.codev21.domains.user.service;

import com.j2kb.codev21.domains.user.dto.JwtTokenDto;
import com.j2kb.codev21.domains.user.dto.UserDto;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    public JwtTokenDto.TokenInRes login(UserDto.LoginReq loginDto) {
        return new JwtTokenDto.TokenInRes();

    }
}
