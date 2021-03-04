package com.j2kb.codev21.domains.user.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j2kb.codev21.domains.board.controller.BoardController;
import com.j2kb.codev21.domains.board.dto.BoardDto;
import com.j2kb.codev21.domains.board.service.BoardService;
import com.j2kb.codev21.domains.user.domain.Field;
import com.j2kb.codev21.domains.user.domain.Status;
import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.dto.UserDto.SelectUserRes;
import com.j2kb.codev21.domains.user.dto.UserDto.UpdateUserReq;
import com.j2kb.codev21.domains.user.repository.UserRepository;
import com.j2kb.codev21.domains.user.service.UserService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class, SpringExtension.class})
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    @DisplayName("회원 전체 조회")
    @Test
    void find_all_userList() throws Exception {

        //given
        List<SelectUserRes> list = new ArrayList<>();
        list.add(
            UserDto.SelectUserRes.builder()
                .id(1L)
                .email("jikimee64@gmail.com")
                .name("김우철")
                .joinGisu("2기")
                .status(Status.ACTIVE.getStatus())
                .field(Field.NONE.getField())
                .githubId("jikimee64@gmail.com")
                .createdAt(
                    LocalDateTime.parse(LocalDateTime.now().format(formatter),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                )
                .build()
        );

        when(userService.getUserList())
            .thenReturn(list);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/admin/users")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bear {token값}"))
            .andExpect(status().isOk())
            .andDo(document("UserController/selectAllUser",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(headerWithName("Authorization").description("Bear {token값}")),
                responseFields(
                    fieldWithPath("code").description("code(200,400...)"),
                    fieldWithPath("message").description("message(success...)"),
                    fieldWithPath("data[0].id").description("The user's id(고유번호)"),
                    fieldWithPath("data[0].email").description("The user's email(아이디)"),
                    fieldWithPath("data[0].name").description("The user's name"),
                    fieldWithPath("data[0].joinGisu").description("The user's joinGisu(가입기수)"),
                    fieldWithPath("data[0].status")
                        .description("The user's status(활동중(ACTIVE),활동중지)"),
                    fieldWithPath("data[0].field")
                        .description("The user's field(backend,frontend...)"),
                    fieldWithPath("data[0].githubId").description("The user's githubId"),
                    fieldWithPath("data[0].createdAt").description("The user's 가입일자")
                )
            ));
    }

    @DisplayName("회원가입")
    @Test
    void join() throws Exception {

        String content = objectMapper.writeValueAsString(
            getStubUser()
        );

        when(userService.joinUser(getStubUser())).thenReturn(
            UserDto.UserIdRes.builder()
                .id(1L)
                .build());

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/users")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("UserController/joinUser",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("password").description("패스워드"),
                    fieldWithPath("name").description("이름"),
                    fieldWithPath("joinGisu").description("가입기수"),
                    fieldWithPath("githubId").description("J2KB 조직 체크버튼 요청 후 건네받는 깃허브ID")
                ),
                responseFields(
                    fieldWithPath("code").description("code(200,400...)"),
                    fieldWithPath("message").description("message(success...)"),
                    fieldWithPath("data.id").description("The user's id(고유번호)")
                )
            ));
    }

    @DisplayName("회원 단건 조회")
    @Test
    void selectUser() throws Exception {

        when(userService.getUser(anyLong()))
            .thenReturn(UserDto.SelectUserRes.builder()
                .id(1L)
                .email("jikimee64@gmail.com")
                .name("김우철")
                .joinGisu("2기")
                .status(Status.ACTIVE.getStatus())
                .field(Field.NONE.getField())
                .githubId("jikimee64@gmail.com")
                .createdAt(
                    LocalDateTime.parse(LocalDateTime.now().format(formatter),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                )
                .build());

        this.mockMvc
            .perform(RestDocumentationRequestBuilders.get("/api/v1/users/{userId}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bear {token값}"))
            .andExpect(status().isOk())
            .andDo(document("UserController/selectUser",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(headerWithName("Authorization").description("Bear {token값}")),
                responseFields(
                    fieldWithPath("code").description("code(200,400...)"),
                    fieldWithPath("message").description("message(success...)"),
                    fieldWithPath("data.id").description("The user's id(고유번호)"),
                    fieldWithPath("data.email").description("The user's email(아이디)"),
                    fieldWithPath("data.name").description("The user's name"),
                    fieldWithPath("data.joinGisu").description("The user's joinGisu(가입기수)"),
                    fieldWithPath("data.status").description("The user's status(활동중(ACTIVE),활동중지)"),
                    fieldWithPath("data.field")
                        .description("The user's field(backend,frontend...)"),
                    fieldWithPath("data.githubId").description("The user's githubId"),
                    fieldWithPath("data.createdAt").description("The user's 가입일자")
                ),
                pathParameters(parameterWithName("userId").description("조회할 회원번호"))));
    }

    @DisplayName("회원 수정(유저 권한)")
    @Test
    void updateUser() throws Exception {

        UpdateUserReq dto = UpdateUserReq.builder()
            .password("update password")
            .build();

        String content = objectMapper.writeValueAsString(
            dto
        );

        when(userService.updateUser(anyLong(), eq(dto)))
            .thenReturn(UserDto.SelectUserRes.builder()
                .id(1L)
                .email("jikimee64@gmail.com")
                .name("김우철")
                .joinGisu("2기")
                .status(Status.ACTIVE.getStatus())
                .field(Field.NONE.getField())
                .githubId("jikimee64@gmail.com")
                .createdAt(
                    LocalDateTime.parse(LocalDateTime.now().format(formatter),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                )
                .build());

        this.mockMvc
            .perform(RestDocumentationRequestBuilders.patch("/api/v1/users/{userId}", 1L)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bear {token값}"))
            .andExpect(status().isOk())
            .andDo(document("UserController/updateUser",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(headerWithName("Authorization").description("Bear {token값}")),
                requestFields(
                    fieldWithPath("password").description("수정할 패스워드")
                ),
                responseFields(
                    fieldWithPath("code").description("code(200,400...)"),
                    fieldWithPath("message").description("message(success...)"),
                    fieldWithPath("data.id").description("The user's id(고유번호)"),
                    fieldWithPath("data.email").description("The user's email(아이디)"),
                    fieldWithPath("data.name").description("The user's name"),
                    fieldWithPath("data.joinGisu").description("The user's joinGisu(가입기수)"),
                    fieldWithPath("data.status").description("The user's status(활동중(ACTIVE),활동중지)"),
                    fieldWithPath("data.field")
                        .description("The user's field(backend,frontend...)"),
                    fieldWithPath("data.githubId").description("The user's githubId"),
                    fieldWithPath("data.createdAt").description("The user's 가입일자")
                ),
                pathParameters(parameterWithName("userId").description("수정할 회원번호"))));
    }


    @DisplayName("회원 수정(관리자 권한)")
    @Test
    void updateUserByAdmin() throws Exception {

        UserDto.UpdateUserByAdminReq dto = UserDto.UpdateUserByAdminReq.builder()
            .status("INACTIVE")
            .field("BACK_END")
            .joinGisu("2기")
            .build();

        String content = objectMapper.writeValueAsString(
            dto
        );

        when(userService.updateUserByAdmin(anyLong(), eq(dto)))
            .thenReturn(UserDto.SelectUserRes.builder()
                .id(1L)
                .email("jikimee64@gmail.com")
                .name("김우철")
                .joinGisu("2기")
                .status(Status.ACTIVE.getStatus())
                .field(Field.NONE.getField())
                .githubId("jikimee64@gmail.com")
                .createdAt(
                    LocalDateTime.parse(LocalDateTime.now().format(formatter),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                )
                .build());

        this.mockMvc
            .perform(RestDocumentationRequestBuilders.patch("/api/v1/admin/users/{userId}", 1L)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bear {token값}"))
            .andExpect(status().isOk())
            .andDo(document("UserController/updateUserByAdmin",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(headerWithName("Authorization").description("Bear {token값}")),
                requestFields(
                    fieldWithPath("status").description("회원활동 상태(값 : ACTIVE, INACTIVE, ARMY)"),
                    fieldWithPath("field").description("소속 분야(값 : APP, FRONT_END, BACK_END, DATA_SCIENCE, NONE)"),
                    fieldWithPath("joinGisu").description("가입기수")
                ),
                responseFields(
                    fieldWithPath("code").description("code(200,400...)"),
                    fieldWithPath("message").description("message(success...)"),
                    fieldWithPath("data.id").description("The user's id(고유번호)"),
                    fieldWithPath("data.email").description("The user's email(아이디)"),
                    fieldWithPath("data.name").description("The user's name"),
                    fieldWithPath("data.joinGisu").description("The user's joinGisu(가입기수)"),
                    fieldWithPath("data.status").description("The user's status(활동중(ACTIVE),활동중지)"),
                    fieldWithPath("data.field")
                        .description("The user's field(backend,frontend...)"),
                    fieldWithPath("data.githubId").description("The user's githubId"),
                    fieldWithPath("data.createdAt").description("The user's 가입일자")
                ),
                pathParameters(parameterWithName("userId").description("수정할 회원번호"))))
                .andDo(print());;
    }

    @DisplayName("회원 삭제")
    @Test
    void deleteUser() throws Exception {
        //given
        when(userService.deleteUser(anyLong()))
            .thenReturn(
                UserDto.DeleteUserCheckRes.builder()
                    .checkFlag(true)
                    .build()
            );

        //when
        //then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/users/{userId}", 1L)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bear {token값}"))
            .andExpect(status().isOk())
            .andDo(document("UserController/deleteUser",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(headerWithName("Authorization").description("Bear {token값}")),
                responseFields(
                    fieldWithPath("code").description("code(200,400...)"),
                    fieldWithPath("message").description("message(success...)"),
                    fieldWithPath("data.checkFlag").description("삭제유무(true, false)")
                ),
                pathParameters(parameterWithName("userId").description("삭제할 회원 번호"))));
    }

    UserDto.JoinReq getStubUser() {
        return UserDto.JoinReq.builder()
            .email("jikimee64@gmail.com")
            .password("1q2w3e4r1!")
            .name("김우철")
            .joinGisu("2기")
            .githubId("jikimee64@gmail.com")
            .build();
    }

}