package com.j2kb.codev21.domains.user.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j2kb.codev21.domains.user.domain.Token;
import com.j2kb.codev21.domains.user.dto.LoginDto;
import com.j2kb.codev21.domains.user.dto.LoginDto.LoginReq;
import com.j2kb.codev21.domains.user.dto.LoginDto.LogoutReq;
import com.j2kb.codev21.domains.user.dto.LoginDto.RefreshReq;
import com.j2kb.codev21.domains.user.dto.LoginDto.RefreshRes;
import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.service.LoginService;
import com.j2kb.codev21.domains.user.service.UserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class, SpringExtension.class})
class LoginControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(documentationConfiguration(restDocumentation)
                .uris()
                .withScheme("http")
                .withHost("localhost.com")
                .withPort(8080))
            .build();
    }

    @DisplayName("로그인")
    @Test
    void login() throws Exception {

        LoginReq logitDto = LoginDto.LoginReq.builder()
            .email("j2kb@j2kb.com")
            .password("j2kb")
            .build();

        String content = objectMapper.writeValueAsString(
            logitDto
        );

        Map<String, Object> map = new HashMap<>();
        map.put(Token.ACCESS_TOKEN.getName(), "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqMmtiQGoya2IuY29tIiwiYXV0aCI6IiIsImV4cCI6MTYxNDAxNTk2Nn0.Yp31VtAyFvfyuZh72Qj_pSF3vYsVr4ZfRrM5Kbk4KAAMJDxIWb0SbYXfY9X1rwTkdTwt5lWn_cRkjldfqZFTrQ");
        map.put(Token.REFRESH_TOKEN.getName(), "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJub3NlbGxAbm9zZWxsLmNvbSIsImV4cCI6MTYxNTUyNzU3Nn0.cj4lnaldqwrenfJtMdm7jjhOzto6ZuiOKKiIQPy5p4yg5k3HC1QNd47SfZcDpscSqq_Tcy2tK5rHtx0QtigE-A");

        when(loginService.login(eq(logitDto)))
            .thenReturn(map);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/login")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("LoginController/login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").description("아이디"),
                    fieldWithPath("password").description("패스워드")
                ),
                responseFields(
                    fieldWithPath("code").description("code(200,400...)"),
                    fieldWithPath("message").description("message(success...)"),
                    fieldWithPath("data.accessToken").description("액세스 토큰"),
                    fieldWithPath("data.userId").description("유저 id(고유번호)")
                ),
                responseHeaders(headerWithName("Set-Cookie").description("httpOnly 적용한 상태로 쿠키에 담아서 refreshToken 전송"))
            ));
    }

    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {

        LogoutReq logoutReq = LogoutReq.builder()
            .accessToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqMmtiQGoya2IuY29tIiwiYXV0aCI6IiIsImV4cCI6MTYxNDAxNTk2Nn0.Yp31VtAyFvfyuZh72Qj_pSF3vYsVr4ZfRrM5Kbk4KAAMJDxIWb0SbYXfY9X1rwTkdTwt5lWn_cRkjldfqZFTrQ")
            .build();

        String content = objectMapper.writeValueAsString(
            logoutReq
        );

        when(loginService.logout(eq(logoutReq)))
            .thenReturn("username(email)");

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/logout")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("LoginController/logout",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("accessToken").description("액세스 토큰")
                ),
                responseFields(
                    fieldWithPath("code").description("code(200,400...)"),
                    fieldWithPath("message").description("message(success...)"),
                    fieldWithPath("data.resultLogout").description("로그아웃 결과")
                )
            ));
    }

    @DisplayName("new access token 요청")
    @Test
    void refresh() throws Exception {

        RefreshReq refreshRes = RefreshReq.builder()
            .accessToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqMmtiQGoya2IuY29tIiwiYXV0aCI6IiIsImV4cCI6MTYxNDAxNTk2Nn0.Yp31VtAyFvfyuZh72Qj_pSF3vYsVr4ZfRrM5Kbk4KAAMJDxIWb0SbYXfY9X1rwTkdTwt5lWn_cRkjldfqZFTrQ")
            .refreshToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJub3NlbGxAbm9zZWxsLmNvbSIsImV4cCI6MTYxNTUyNzU3Nn0.cj4lnaldqwrenfJtMdm7jjhOzto6ZuiOKKiIQPy5p4yg5k3HC1QNd47SfZcDpscSqq_Tcy2tK5rHtx0QtigE-A")
            .build();

        String content = objectMapper.writeValueAsString(
            refreshRes
        );

        when(loginService.provideNewAccessToken(eq(refreshRes)))
            .thenReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqMmtiQGoya2IuY29tIiwiYXV0aCI6IiIsImV4cCI6MTYxNDAxNTk2Nn0.Yp31VtAyFvfyuZh72Qj_pSF3vYsVr4ZfRrM5Kbk4KAAMJDxIWb0SbYXfY9X1rwTkdTwt5lWn_cRkjldfqZFTrQKWC");

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/refresh")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("LoginController/refresh",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("accessToken").description("액세스 토큰"),
                    fieldWithPath("refreshToken").description("리프레쉬 토큰")
                ),
                responseFields(
                    fieldWithPath("code").description("code(200,400...)"),
                    fieldWithPath("message").description("message(success...)"),
                    fieldWithPath("data.accessToken").description("새로운 토큰값")
                )
            ));
    }

}