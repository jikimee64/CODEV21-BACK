package com.j2kb.codev21.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j2kb.codev21.domains.user.exception.NotHaveAccessTokenException;
import com.j2kb.codev21.global.jwt.JwtTokenProvider;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
@Component
public class MemberEmailDecodeResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isTokenMemberEmail = parameter
            .getParameterAnnotation(TokenMemberEmail.class) != null;

        boolean isString = String.class.equals(parameter.getParameterType());

        return isTokenMemberEmail && isString;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
        throws Exception {
        String authorizationHeader = webRequest.getHeader("Authorization");
        log.info("Authorization Header ::: " + authorizationHeader);

        if (authorizationHeader == null) {
            throw new NotHaveAccessTokenException();
        }

        String jwtToken = authorizationHeader.substring(7);

        Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);

        String email = authentication.getName();

        return email;
    }
}
