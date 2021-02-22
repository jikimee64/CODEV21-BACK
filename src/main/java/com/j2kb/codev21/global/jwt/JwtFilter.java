package com.j2kb.codev21.global.jwt;


import com.j2kb.codev21.global.error.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
public class JwtFilter extends GenericFilterBean {

   // private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private JwtTokenProvider tokenProvider;

    public JwtFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain)
        throws IOException, ServletException {
        log.info("========== doFilter() ==========");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            //인증에 성공하면 Spring이 관리하는 SecurityContext에 인증 객체를 설정
            //Authentication객체를 통해 사용자가 토큰을 통해 접근할 수 있는 리소스가 FIX
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(),
                requestURI);
        } else {
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        log.info("========== resolveToken() ==========");
        try {

            String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                tokenProvider.validateExceptionToken(bearerToken.substring(7)); //에외처리 검사용
                return bearerToken.substring(7);
            }

        } catch (ExpiredJwtException e) {
            log.info("========== ExpiredJwtException ==========");
            request.setAttribute("exception", ErrorCode.EXPIRED_JWT_TOKE.getCode());
        } catch (JwtException e) {
            log.info("========== JwtException ==========");
            request.setAttribute("exception", ErrorCode.INVALID_JWT_TOKEN.getCode());
        }
        return null;
    }
}