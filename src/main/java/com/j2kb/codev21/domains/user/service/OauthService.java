package com.j2kb.codev21.domains.user.service;

import com.j2kb.codev21.domains.user.domain.SocialLoginType;
import com.j2kb.codev21.domains.user.dto.AuthDto;
import com.j2kb.codev21.domains.user.dto.AuthDto.GithubIdRes;
import com.j2kb.codev21.domains.user.dto.AuthDto.GithubInfoOrgs;
import com.j2kb.codev21.domains.user.dto.AuthDto.GithubProfileRes;
import com.j2kb.codev21.domains.user.service.social.SocialOauth;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {

    private final HttpServletResponse response;
    private final List<SocialOauth> socialOauthList;

    public void request(SocialLoginType socialLoginType) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        String redirectURL = socialOauth.getOauthRedirectURL();
        log.info("URL : " + redirectURL);
        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            log.info("request error : " + redirectURL);
            e.printStackTrace();
        }
    }

    public String requestAccessToken(SocialLoginType socialLoginType, String code) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        AuthDto.TokenRes tokenRes = socialOauth.requestAccessToken(code);

        GithubProfileRes githubProfileRes = socialOauth.userInfoGithub(tokenRes);
        log.info("깃허브 이메일 : " + githubProfileRes.getEmail());
        String githubId = githubProfileRes.getEmail();


        //깃허브 조직계정으로 다시한번 통신 한뒤 조직 유무 평가
        GithubInfoOrgs[] githubInfoOrgs = socialOauth
            .githubOrgsCheck(githubProfileRes.getOrganizations_url());

        if ("J2KB".equals(githubInfoOrgs[0].getLogin())) {
            return githubId;
        }

        //기존 DB에 존재하는 githuId인지 로직 추가필요
        return githubId;
    }

    private SocialOauth findSocialOauthByType(SocialLoginType socialLoginType) {
        return socialOauthList.stream()
            .filter(x -> x.type() == socialLoginType)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }

}
