package com.j2kb.codev21.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long tokenValidityInMillisecondsAccess;
    //private final long tokenValidityInMillisecondsRefresh;

    private Key keyAccess;

    public JwtTokenProvider(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.token-validity-in-seconds-access}") long tokenValidityInSecondsAccess,
        @Value("${jwt.token-validity-in-seconds-access}") long tokenValidityInSecondsRefresh) {
        this.secret = secret;
        this.tokenValidityInMillisecondsAccess = tokenValidityInSecondsAccess * 1000;
        //this.tokenValidityInMillisecondsRefresh = tokenValidityInSecondsRefresh * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyAccessByte = Decoders.BASE64.decode(secret);
        this.keyAccess = Keys.hmacShaKeyFor(keyAccessByte);
    }

    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validityAccess = new Date(now + this.tokenValidityInMillisecondsAccess);
        //Date validityRefresh = new Date(now + this.tokenValidityInMillisecondsRefresh);

        String tokens = Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(keyAccess, SignatureAlgorithm.HS512)
            .setExpiration(validityAccess) //토큰만료시간
            .compact();

//        tokens.put(Token.REFRESH_TOKEN.getName(), Jwts.builder()
//            .setSubject(authentication.getName())
//            .claim(AUTHORITIES_KEY, authorities)
//            .signWith(keyAccess, SignatureAlgorithm.HS512)
//            .setExpiration(validityRefresh) //토큰만료시간
//            .compact());

        return tokens;
    }

    //토큰값을 통해 사용자 정보 GET
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
            .parserBuilder()
            .setSigningKey(keyAccess)
            .build()
            .parseClaimsJws(token)
            .getBody();

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(keyAccess).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    //토근 검사용
    public void validateExceptionToken(String token) throws ExpiredJwtException, io.jsonwebtoken.security.SecurityException,
        UnsupportedJwtException, IllegalArgumentException {
        Jwts.parserBuilder().setSigningKey(keyAccess).build().parseClaimsJws(token);
    }

}
