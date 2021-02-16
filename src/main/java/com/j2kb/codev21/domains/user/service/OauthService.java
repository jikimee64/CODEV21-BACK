package com.j2kb.codev21.domains.user.service;

import java.io.IOException;
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


    public void request(String socialLoginType) {
        try {
            response.sendRedirect("redirect URL");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean requestAccessToken(String socialLoginType) {
        return true;
    }

    private void userInfo(){

    }

}
