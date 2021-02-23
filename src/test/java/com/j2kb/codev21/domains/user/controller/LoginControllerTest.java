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
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j2kb.codev21.domains.user.dto.LoginDto;
import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.dto.UserDto.LoginReq;
import com.j2kb.codev21.domains.user.dto.UserDto.selectUserRes;
import com.j2kb.codev21.domains.user.service.LoginService;
import com.j2kb.codev21.domains.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
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

    @DisplayName("회원 가입")
    @Order(1)
    @Test
    void join() throws Exception {

        String content = objectMapper.writeValueAsString(
            getStubUser()
        );

        when(userService.joinUser(getStubUser())).thenReturn(
            UserDto.userIdRes.builder()
                .id(1L)
                .build());

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/users/")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @DisplayName("로그인")
    @Order(2)
    @Test
    void login() throws Exception {
        String content = objectMapper.writeValueAsString(
            getStubLogin()
        );

        when(loginService.login(eq(getStubLogin())))
            .thenReturn(LoginDto.LoginRes.builder()
                .userId(1L)
                .accessToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqMmtiQGoya2IuY29tIiwiYXV0aCI6IiIsImV4cCI6MTYxNDAxNTk2Nn0.Yp31VtAyFvfyuZh72Qj_pSF3vYsVr4ZfRrM5Kbk4KAAMJDxIWb0SbYXfY9X1rwTkdTwt5lWn_cRkjldfqZFTrQ")
                .build());

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/login")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("LoginController/login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("code").description("code(200,400...)"),
                    fieldWithPath("message").description("message(success...)"),
                    fieldWithPath("data.accessToken").description("액세스 토큰"),
                    fieldWithPath("data.userId").description("유저 id(고유번호)")
                ),
                responseHeaders(headerWithName("Authorization").description("액세스 토큰값"))
            ));
    }

    UserDto.joinReq getStubUser() {
        return UserDto.joinReq.builder()
            .email("jikimee64@gmail.com")
            .password("password")
            .name("김우철")
            .joinGisu("2기")
            .githubId("jikimee64@gmail.com")
            .isOauth(true)
            .build();
    }

    LoginDto.LoginReq getStubLogin() {
        return LoginDto.LoginReq.builder()
            .email("j2kb@j2kb.com")
            .password("j2kb")
            .build();
    }



}