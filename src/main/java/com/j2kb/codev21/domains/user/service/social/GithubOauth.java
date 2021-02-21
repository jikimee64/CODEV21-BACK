package com.j2kb.codev21.domains.user.service.social;

import com.j2kb.codev21.domains.user.dto.AuthDto;
import com.j2kb.codev21.domains.user.dto.AuthDto.GithubInfoOrgs;
import com.j2kb.codev21.domains.user.dto.AuthDto.TokenRes;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubOauth implements SocialOauth {

    @Value("${sns.github.url}")
    private String GITHUB_SNS_BASE_URL;

    @Value("${sns.github.client.id}")
    private String GITHUB_SNS_CLIENT_ID;

    @Value("${sns.github.callback.url}")
    private String GITHUB_SNS_CALLBACK_URL;

    @Value("${sns.github.client.secret}")
    private String GITHUB_SNS_CLIENT_SECRET;

    @Value("${sns.github.token.url}")
    private String GITHUB_SNS_TOKEN_BASE_URL;

    @Value("${sns.github.userinfo.url}")
    private String GITHUB_SNS_USERINFO_URL;

    private final RestTemplate restTemplate;

    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", GITHUB_SNS_CLIENT_ID);
        params.put("redirect_uri", GITHUB_SNS_CALLBACK_URL);

        String parameterString = params.entrySet().stream()
            .map(x -> x.getKey() + "=" + x.getValue())
            .collect(Collectors.joining("&"));

        return GITHUB_SNS_BASE_URL + "?" + parameterString;
    }

    @Override
    public AuthDto.TokenRes requestAccessToken(String code) {

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GITHUB_SNS_CLIENT_ID);
        params.put("client_secret", GITHUB_SNS_CLIENT_SECRET);
        try {
            ResponseEntity<TokenRes> responseEntity =
                restTemplate
                    .postForEntity(GITHUB_SNS_TOKEN_BASE_URL, params, AuthDto.TokenRes.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            }
        } catch (HttpClientErrorException e) {
            log.info("========== 깃허브 API 인증 실패 에러 ==========");
            e.printStackTrace();
            //throw new NaverApiUnauthorizedException();
        } catch (Exception e) {
            log.info("========== requestAccessToken : 깃허브 API 통신 알수 없는 에러 ==========");
            e.printStackTrace();
            //throw new NavereApiErrorException();
        }
        return null;
    }

    @Override
    public AuthDto.GithubProfileRes userInfoGithub(TokenRes tokenRes) {
//        Map<String, Object> params = new HashMap<>();
//        System.out.println("access_token : " + tokenRes.getAccess_token());
//        params.put("access_token", tokenRes.getAccess_token());
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + tokenRes.getAccess_token());

            ResponseEntity<AuthDto.GithubProfileRes> responseEntity =
                restTemplate
                    .exchange(GITHUB_SNS_USERINFO_URL, HttpMethod.GET, new HttpEntity<>(headers),
                        AuthDto.GithubProfileRes.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            }
        } catch (HttpClientErrorException e) {
            log.info("========== 깃허브 API 인증 실패 에러 ==========");
            e.printStackTrace();
            //throw new NaverApiUnauthorizedException();
        } catch (Exception e) {
            log.info("========== userInfoGithub : 깃허브 API 통신 알수 없는 에러 ==========");
            e.printStackTrace();
            //throw new NavereApiErrorException();
        }
        return null;
    }

    @Override
    public GithubInfoOrgs[] githubOrgsCheck(String orgsUrl) {
        try {

            log.info("orgsUrl" + orgsUrl);
            ResponseEntity<GithubInfoOrgs[]> responseEntity =
                restTemplate
                    .getForEntity(orgsUrl, GithubInfoOrgs[].class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("깃허브 조직 정보" + responseEntity.getBody());
                return responseEntity.getBody();
            }
        } catch (HttpClientErrorException e) {
            log.info("========== 깃허브 API 인증 실패 에러 ==========");
            e.printStackTrace();
            //throw new NaverApiUnauthorizedException();
        } catch (Exception e) {
            log.info("========== githubOrgsCheck : 깃허브 API 통신 알수 없는 에러 ==========");
            e.printStackTrace();
            //throw new NavereApiErrorException();
        }
        return null;
    }

}