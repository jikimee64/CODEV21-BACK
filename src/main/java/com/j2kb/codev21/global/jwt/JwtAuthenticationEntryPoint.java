package com.j2kb.codev21.global.jwt;

import com.j2kb.codev21.global.error.ErrorCode;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException) throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401

        String exception = (String)request.getAttribute("exception");
        ErrorCode errorCode;

        log.debug("log: exception: {} ", exception);

        /**
         * 토큰 없는 경우
         */
        if(exception == null) {
            log.info("JwtAuthenticationEntryPoint() : 토큰 없는 경우");
            errorCode = ErrorCode.INVALID_JWT_TOKEN;
            setResponse(response, errorCode);
            return;
        }

        /**
         * 토큰 만료된 경우
         */
        if(exception.equals(ErrorCode.EXPIRED_JWT_TOKE.getCode())) {
            log.info("JwtAuthenticationEntryPoint() : 토큰 만료된 경우");
            errorCode = ErrorCode.EXPIRED_JWT_TOKE;
            setResponse(response, errorCode);
            return;
        }

        /**
         * 토큰 시그니처가 다른 경우
         */
        if(exception.equals(ErrorCode.INVALID_JWT_TOKEN.getCode())) {
            log.info("JwtAuthenticationEntryPoint() : 토큰 시그니처가 다른 경우");
            errorCode = ErrorCode.INVALID_JWT_TOKEN;
            setResponse(response, errorCode);
        }

        log.info("JwtAuthenticationEntryPoint");

    }

    /**
     * 한글 출력을 위해 getWriter() 사용
     */
    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{ \"code\" : \"" +  errorCode.getCode()
            + "\", \"message\" : \"" +  errorCode.getMessage()
            + "\" }");
    }

//        response.getWriter().println("{ \"message\" : \"" + errorCode.getMessage()
//            + "\", \"code\" : \"" +  errorCode.getCode()
//                + "\", \"status\" : " + errorCode.getStatus()
//                + ", \"errors\" : [ ] }");

}