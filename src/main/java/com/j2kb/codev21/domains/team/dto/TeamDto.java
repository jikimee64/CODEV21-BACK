package com.j2kb.codev21.domains.team.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TeamDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class SelectTeamRes {
        private Long id;
        private String gisu;
        private String teamName;
        private String isLeader;
        private List<TeamMemberList> teamMemberLists;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class JoinTeam{

        @NotBlank(message = "기수는 필수 입력 값입니다.")
        @Size(max = 12, message = "기수는 12자 이하로 입력해주세요.")
        private String gisu;

        @NotBlank(message = "팀이름은 필수 입력 값입니다.")
        @Size(max = 30, message = "아이디는 30자 이하로 입력해주세요.")
        private String teamName;

        @NotBlank(message = "리더 필수 입력 값입니다.")
        @Size(max = 15, message = "비밀번호는 15자 이하로 입력해주세요.")
        private String isLeader;

        private List<TeamMemberList> teamMemberLists = new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class TeamMemberList{
        @NotBlank(message = "멤버명은 필수 입력 값입니다.")
        @Size(max = 12, message = "멤버명은 12자 이하로 입력해주세요.")
        private String name;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class updateTeamByAdminReq {

        @NotBlank(message = "기수는 필수 입력 값입니다.")
        @Size(max = 12, message = "기수는 12자 이하로 입력해주세요.")
        private String gisu;

        @NotBlank(message = "팀이름은 필수 입력 값입니다.")
        @Size(max = 30, message = "아이디는 30자 이하로 입력해주세요.")
        private String teamName;

        @NotBlank(message = "리더 필수 입력 값입니다.")
        @Size(max = 15, message = "비밀번호는 15자 이하로 입력해주세요.")
        private String isLeader;

        private List<TeamMemberList> teamMemberLists = new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class teamIdRes {
        private Long id;
    }


}
