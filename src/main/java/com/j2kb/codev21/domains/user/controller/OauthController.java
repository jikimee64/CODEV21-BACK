package com.j2kb.codev21.domains.user.controller;

import com.j2kb.codev21.domains.user.domain.SocialLoginType;
import com.j2kb.codev21.domains.user.dto.AuthDto.GithubCheckRes;
import com.j2kb.codev21.domains.user.dto.UserDto.selectUserRes;
import com.j2kb.codev21.domains.user.service.OauthService;
import com.j2kb.codev21.global.common.CommonResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
public class OauthController {

    private final OauthService oauthService;

    /**
     * 사용자로부터 SNS 로그인 요청을 Social Login Type 을 받아 처리
     */
    @GetMapping(value = "/{socialLoginType}")
    public CommonResponse<GithubCheckRes> socialConfirm(
        @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {
        log.info("========== 깃허브 인증 요청 ==========");
        oauthService.request(socialLoginType);

        //return 안해도 되지만 Rest Doc문서출력용
        return CommonResponse.<GithubCheckRes>builder()
            .code("200")
            .message("ok")
            .data(
                GithubCheckRes.builder()
                    .isOauth(true)
                    .build())
            .build();
    }

    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     */
    @GetMapping(value = "/{socialLoginType}/callback")
    public CommonResponse<GithubCheckRes> callback(
        @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
        @RequestParam(name = "code") String code
    ) {
        log.info("========== 깃허브 CallBack 요청 ==========");

        return CommonResponse.<GithubCheckRes>builder()
            .code("200")
            .message("ok")
            .data(
                GithubCheckRes.builder()
                    .isOauth(oauthService
                        .requestAccessToken(socialLoginType, code))
                    .build()
            ).build();
    }

}
