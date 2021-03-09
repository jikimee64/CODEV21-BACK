package com.j2kb.codev21.domains.user.service;

import com.j2kb.codev21.domains.user.domain.SocialLoginType;
import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.domains.user.dto.AuthDto;
import com.j2kb.codev21.domains.user.dto.AuthDto.GithubIdRes;
import com.j2kb.codev21.domains.user.dto.AuthDto.GithubInfoOrgs;
import com.j2kb.codev21.domains.user.dto.AuthDto.GithubProfileRes;
import com.j2kb.codev21.domains.user.exception.GithubDuplicationException;
import com.j2kb.codev21.domains.user.repository.UserRepository;
import com.j2kb.codev21.domains.user.service.social.SocialOauth;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final UserRepository userRepository;

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

    public String requestGithubId(SocialLoginType socialLoginType, String code) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        AuthDto.TokenRes tokenRes = socialOauth.requestAccessToken(code);

        GithubProfileRes githubProfileRes = socialOauth.userInfoGithub(tokenRes);
        log.info("깃허브 이메일 : " + githubProfileRes.getEmail());
        String githubId = githubProfileRes.getEmail();

        //깃허브 조직계정(URL)으로 다시한번 API 호출 한뒤 조직 유무 평가
        GithubInfoOrgs[] githubInfoOrgs = socialOauth
            .githubOrgsCheck(githubProfileRes.getOrganizations_url());

        if ("J2KB".equals(githubInfoOrgs[0].getLogin())) {
            Optional<User> user = userRepository.findByGithubId(githubId);
            user.ifPresent( v -> {
                throw new GithubDuplicationException();
            });
            return githubId;
        }else{
            throw new GithubDuplicationException();
        }
    }

    private SocialOauth findSocialOauthByType(SocialLoginType socialLoginType) {
        return socialOauthList.stream()
            .filter(x -> x.type() == socialLoginType)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialType 입니다."));
    }

}
