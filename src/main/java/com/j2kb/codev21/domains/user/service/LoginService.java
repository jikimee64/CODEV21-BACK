package com.j2kb.codev21.domains.user.service;

import com.j2kb.codev21.domains.user.domain.RedisToken;
import com.j2kb.codev21.domains.user.domain.Token;
import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.domains.user.dto.JwtTokenDto;
import com.j2kb.codev21.domains.user.dto.LoginDto;
import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.exception.MemberLogoutException;
import com.j2kb.codev21.domains.user.repository.UserRepository;
import com.j2kb.codev21.global.common.CommonResponse;
import com.j2kb.codev21.global.jwt.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final RedisTemplate<String, Object> redisTemplate;
    private final CustomUserDetailsService customUserDetailsService;

    public Map<String, Object> login(LoginDto.LoginReq loginDto) {

        Map<String, Object> map = new HashMap<>();
        //아이디와 패스워드를 조합해서 인스턴스 생성
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        // authenticate() 실행시 loadUserByUsername 실행
        // 사용자 비밀번호 체크, 패스워드 일치하지 않는다면 Exception 발생 및 이후 로직 실행 안됨
        Authentication authentication = authenticationManagerBuilder.getObject()
            .authenticate(authenticationToken);

        //로그인 성공하면 인증 객체 생성 및 스프링 시큐리티 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);

        map.put(Token.ACCESS_TOKEN.getName(), jwtTokenProvider.doGenerateToken(authentication));
        map.put(Token.REFRESH_TOKEN.getName(),
            jwtTokenProvider.doGenerateRefreshToken(loginDto.getEmail()));

        Optional<User> byAccount = userRepository.findByEmail(loginDto.getEmail());
        byAccount.ifPresent( v -> {
            map.put("id", v.getId());
        });

        return map;
    }

    public String logout(LoginDto.LogoutReq dto){
        String username = null;
        String accessToken = dto.getAccessToken();
        try {
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            username = authentication.getName();
        } catch (IllegalArgumentException e) {
        } catch (ExpiredJwtException e) { //expire됐을 때
            username = e.getClaims().getSubject();
            log.info("username from expired access token: " + username);
        }
        return username;
    }

    public String provideNewAccessToken(LoginDto.RefreshReq dto) {
        String accessToken = null;
        String refreshToken = null;
        String refreshTokenFromDb = null;
        String username = null;
        String responseAccessToken = null;
        Map<String, Object> map = new HashMap<>();
        try {
            accessToken = dto.getAccessToken();
            refreshToken = dto.getRefreshToken();
            log.info("accessToken: " + accessToken);
            log.info("refreshToken: " + refreshToken);
            try {
                //accessToken값으로 유저정보 GET
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                username = authentication.getName();
                log.info("success username: " + username);
            } catch (IllegalArgumentException e) {

            } catch (ExpiredJwtException e) { //expire됐을 때
                username = e.getClaims().getSubject();
                log.info("username from expired access token: " + username);
            }

            if (refreshToken != null) { //refresh를 같이 보냈으면.
                try {
                    ValueOperations<String, Object> vop = redisTemplate.opsForValue();
                    RedisToken result = (RedisToken) vop.get(username);
                    if(result == null){ //로그아웃된 상태
                        throw new MemberLogoutException();
                    }
                    refreshTokenFromDb = result.getRefreshToken();
                    log.info("rtfrom db: " + refreshTokenFromDb);
                    log.info("refreshToken : " + refreshToken);
                } catch (IllegalArgumentException e) {
                    log.warn("illegal argument!!");
                }
                //둘이 일치하고 refresh token 만료도 안됐으면 재발급 해주기.
                if (refreshToken.equals(refreshTokenFromDb) && jwtTokenProvider
                    .validateExceptionToken(refreshToken)) {
                    final UserDetails userDetails = customUserDetailsService
                        .loadUserByUsername(username);

                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                    responseAccessToken = jwtTokenProvider.doGenerateToken(authentication);
                } else {
                    log.info("refresh token is expired.");
                }
            } else { //refresh token이 없으면
                log.info("your refresh token does not exist.");
            }

        } catch (Exception e) {
            throw e;
        }

        return responseAccessToken;

    }

}
