package com.j2kb.codev21.domains.team.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j2kb.codev21.domains.team.dto.TeamDto;
import com.j2kb.codev21.domains.team.dto.TeamDto.JoinTeam;
import com.j2kb.codev21.domains.team.dto.TeamDto.SelectTeamRes;
import com.j2kb.codev21.domains.team.dto.TeamDto.TeamMemberList;
import com.j2kb.codev21.domains.team.service.TeamService;
import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.dto.UserDto.selectUserRes;
import com.j2kb.codev21.domains.user.dto.UserDto.updateUserReq;
import com.j2kb.codev21.domains.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
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
class TeamControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private TeamService teamService;

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

    @DisplayName("팀 전체 조회")
    @Test
    void find_all_teamList() throws Exception {

        List<SelectTeamRes> teamList = new ArrayList<>();
        teamList.add(
            TeamDto.SelectTeamRes.builder()
                .id(1L)
                .gisu("1기")
                .teamName("팀이름")
                .isLeader("김우철")
                .teamMemberLists(getStubMemberList())
                .build()
        );

        when(teamService.getTeamList())
            .thenReturn(teamList);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/admin/teams/")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("TeamController/selectAllTeam",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("code").description("code(200,400...)"),
                    fieldWithPath("message").description("message(success...)"),
                    fieldWithPath("data[0].id").description("팀 고유번호"),
                    fieldWithPath("data[0].gisu").description("기수명(1기, 2기..)"),
                    fieldWithPath("data[0].teamName").description("팀이름"),
                    fieldWithPath("data[0].isLeader").description("The leader's name"),
                    fieldWithPath("data[0].teamMemberLists[0].name").description("팀 멤버")
                )
            ));
    }


    @DisplayName("팀 단건 조회")
    @Test
    void selectUser() throws Exception {

        //given
        SelectTeamRes team =
            TeamDto.SelectTeamRes.builder()
                .id(1L)
                .gisu("1기")
                .teamName("팀이름")
                .isLeader("김우철")
                .teamMemberLists(getStubMemberList())
                .build();

        when(teamService.getTeam(anyLong()))
            .thenReturn(
                team);

        this.mockMvc
            .perform(RestDocumentationRequestBuilders.get("/api/v1/teams/{teamId}", 1L)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("TeamController/selectTeam",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("code").description("code(200,400...)"),
                    fieldWithPath("message").description("message(success...)"),
                    fieldWithPath("data.id").description("팀 고유번호"),
                    fieldWithPath("data.gisu").description("기수명(1기, 2기..)"),
                    fieldWithPath("data.teamName").description("팀이름"),
                    fieldWithPath("data.isLeader").description("The leader's name"),
                    fieldWithPath("data.teamMemberLists[0].name").description("팀 멤버")
                ),
                pathParameters(parameterWithName("teamId").description("조회할 팀번호"))));
    }


    @DisplayName("팀 등록")
    @Test
    void join() throws Exception {

        TeamDto.JoinTeam team =
            TeamDto.JoinTeam.builder()
                .gisu("1기")
                .teamName("팀이름")
                .isLeader("김우철")
                .teamMemberLists(getStubMemberList())
                .build();

        String content = objectMapper.writeValueAsString(
            team
        );

        when(teamService.joinTeam(team)).thenReturn(
            TeamDto.teamIdRes.builder()
                .id(1L)
                .build());

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/teams/")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("TeamController/joinTeam",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("code").description("code(200,400...)"),
                    fieldWithPath("message").description("message(success...)"),
                    fieldWithPath("data.id").description("The team's id(고유번호)")
                )
            ));
    }

    @DisplayName("회원 수정(관리자 권한)")
    @Test
    void updateUserByAdmin() throws Exception {

        TeamDto.updateTeamByAdminReq dto = TeamDto.updateTeamByAdminReq.builder()
            .gisu("2기(수정)")
            .teamName("팀이름(수정)")
            .isLeader("유경호(수정)")
            .teamMemberLists(getStubMemberList())
            .build();

        String content = objectMapper.writeValueAsString(
            dto
        );

        when(teamService.updateTeamByAdmin(anyLong(), eq(dto)))
            .thenReturn(TeamDto.SelectTeamRes.builder()
                .id(1L)
                .gisu("2기(수정)")
                .teamName("팀이름(수정)")
                .isLeader("유경호(수정)")
                .teamMemberLists(getStubMemberList())
                .build());

        this.mockMvc
            .perform(RestDocumentationRequestBuilders.patch("/api/v1/admin/teams/{teamId}", 1L)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("TeamController/updateTeamByAdmin",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("code").description("code(200,400...)"),
                    fieldWithPath("message").description("message(success...)"),
                    fieldWithPath("data.id").description("팀 고유번호"),
                    fieldWithPath("data.gisu").description("기수명(1기, 2기..)"),
                    fieldWithPath("data.teamName").description("팀이름"),
                    fieldWithPath("data.isLeader").description("The leader's name"),
                    fieldWithPath("data.teamMemberLists[0].name").description("팀 멤버")
                ),
                pathParameters(parameterWithName("teamId").description("수정할 팀번호"))));
    }

    @DisplayName("회원 삭제")
    @Test
    void deleteUser() throws Exception {
        //given
        when(teamService.deleteTeam(anyLong()))
            .thenReturn(true);

        //when
        //then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/admin/teams/{teamId}", 1L)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("TeamController/deleteTeam",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("code").description("code(200,400...)"),
                    fieldWithPath("message").description("message(success...)"),
                    fieldWithPath("data.result").description("삭제유무(true, false)")
                ),
                pathParameters(parameterWithName("teamId").description("삭제할 팀 번호"))));
    }

    List<TeamMemberList> getStubMemberList() {
        List<TeamMemberList> memberList = new ArrayList<>();
        memberList.add(
            TeamMemberList.builder()
                .name("김우철")
                .build()
        );
        memberList.add(
            TeamMemberList.builder()
                .name("고재영")
                .build()
        );
        memberList.add(
            TeamMemberList.builder()
                .name("유경호")
                .build()
        );
        return memberList;
    }


}