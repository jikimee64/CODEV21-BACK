package com.j2kb.codev21.global.jwt;

import com.j2kb.codev21.global.error.ErrorCode;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        //토큰은 존재하지만 필요한 권한이 없이 접근하려 할때 403
        log.info("=========== JwtAccessDeniedHandler ===========");

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().println("{ \"message\" : \"" + ErrorCode.ACCESS_DENIED.getMessage()
            + "\", \"code\" : \"" +  ErrorCode.ACCESS_DENIED.getCode()
            + "\", \"status\" : " + ErrorCode.ACCESS_DENIED.getStatus()
            + ", \"errors\" : [ ] }");

    }
}