package com.j2kb.codev21.domains.user.service.social;

import com.j2kb.codev21.domains.user.domain.SocialLoginType;
import com.j2kb.codev21.domains.user.dto.AuthDto;
import com.j2kb.codev21.domains.user.dto.AuthDto.GithubInfoOrgs;
import com.j2kb.codev21.domains.user.dto.AuthDto.TokenRes;

public interface SocialOauth {
    /**
     * 각 Social Login 페이지로 Redirect 처리할 URL Build 사용자로부터 로그인 요청을 받아 Social Login Server 인증용 code 요청
     */
    String getOauthRedirectURL();

    /**
     * API Server로부터 받은 code를 활용하여 사용자 인증 정보 요청
     *
     * @param code API Server 에서 받아온 code
     * @return API 서버로 부터 응답받은 Json 형태의 결과를 string으로 반환
     */
    AuthDto.TokenRes requestAccessToken(String code);

    //String requestUserInfo(GoogleAuthDto.TokenRes tokenRes);

    default AuthDto.GithubProfileRes userInfoGithub(TokenRes tokenRes) {
        return null;
    }

    default GithubInfoOrgs[] githubOrgsCheck(String orgsUrl){ return null; }

    default SocialLoginType type() {
        if (this instanceof GithubOauth) {
            return SocialLoginType.GITHUB;
        } else {
            return null;
        }
    }

}
