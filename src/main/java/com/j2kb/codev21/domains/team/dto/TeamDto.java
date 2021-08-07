package com.j2kb.codev21.domains.team.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.j2kb.codev21.domains.board.dto.BoardDto.GisuInfo;
import com.j2kb.codev21.domains.gisucategory.dto.GisuCategoryDto;

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
        private GisuInfo gisuInfo;
        private String teamName;
        private List<TeamMemberList> teamMemberLists;
        
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Req{

        @NotBlank(message = "기수ID는 필수 입력 값입니다.")
        private Long gisuId;

        @NotBlank(message = "팀이름은 필수 입력 값입니다.")
        @Size(max = 30, message = "팀이름은 30자 이하로 입력해주세요.")
        private String teamName;

        private List<TeamMemberList> teamMemberLists;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class TeamMemberList{
    	
        @NotBlank(message = "멤버명은 필수 입력 값입니다.")
        @Size(max = 12, message = "멤버명은 12자 이하로 입력해주세요.")
        private Long userId;
        
        private String userName;
        
        @NotBlank(message = "리더 여부는 필수 입력 값입니다.(true, false)")
        private Boolean isLeader;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class TeamIdRes {
        private Long id;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class DeleteTeamCheckRes {
        private Boolean checkFlag;
    }


}
