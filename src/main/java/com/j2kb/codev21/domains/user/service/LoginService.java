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
        //???????????? ??????????????? ???????????? ???????????? ??????
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        // authenticate() ????????? loadUserByUsername ??????
        // ????????? ???????????? ??????, ???????????? ???????????? ???????????? Exception ?????? ??? ?????? ?????? ?????? ??????
        Authentication authentication = authenticationManagerBuilder.getObject()
            .authenticate(authenticationToken);

        //????????? ???????????? ?????? ?????? ?????? ??? ????????? ???????????? ??????
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
        } catch (ExpiredJwtException e) { //expire?????? ???
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
                //accessToken????????? ???????????? GET
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                username = authentication.getName();
                log.info("success username: " + username);
            } catch (IllegalArgumentException e) {

            } catch (ExpiredJwtException e) { //expire?????? ???
                username = e.getClaims().getSubject();
                log.info("username from expired access token: " + username);
            }

            if (refreshToken != null) { //refresh??? ?????? ????????????.
                try {
                    ValueOperations<String, Object> vop = redisTemplate.opsForValue();
                    RedisToken result = (RedisToken) vop.get(username);
                    if(result == null){ //??????????????? ??????
                        throw new MemberLogoutException();
                    }
                    refreshTokenFromDb = result.getRefreshToken();
                    log.info("rtfrom db: " + refreshTokenFromDb);
                    log.info("refreshToken : " + refreshToken);
                } catch (IllegalArgumentException e) {
                    log.warn("illegal argument!!");
                }
                //?????? ???????????? refresh token ????????? ???????????? ????????? ?????????.
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
            } else { //refresh token??? ?????????
                log.info("your refresh token does not exist.");
            }

        } catch (Exception e) {
            throw e;
        }

        return responseAccessToken;

    }

}
