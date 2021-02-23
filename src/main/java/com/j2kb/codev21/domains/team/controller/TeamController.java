package com.j2kb.codev21.domains.team.controller;


import com.j2kb.codev21.domains.team.dto.TeamDto;
import com.j2kb.codev21.domains.team.dto.TeamDto.SelectTeamRes;
import com.j2kb.codev21.domains.team.dto.TeamDto.teamIdRes;
import com.j2kb.codev21.domains.team.service.TeamService;
import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.dto.UserDto.selectUserRes;
import com.j2kb.codev21.domains.user.service.UserService;
import com.j2kb.codev21.global.common.CommonResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TeamController {

    private final TeamService teamService;

    //팀 전체 조회(기수, 유저 다)
    //@PreAuthorize("isAuthenticated() and ( hasRole('ROLE_ADMIN'))" )
    @GetMapping("/admin/teams")
    public CommonResponse<List<SelectTeamRes>> selectAllTeam() {
        return CommonResponse.<List<SelectTeamRes>>builder()
            .code("200")
            .message("ok")
            .data(teamService.getTeamList()).build();
    }

    //팀 단건 조회
    @GetMapping(value = {"/teams/{teamId}", "/admin/teams/{userId}"})
    public CommonResponse<SelectTeamRes> selectTeam(
        @PathVariable("teamId") Long teamId) {

        return CommonResponse.<SelectTeamRes>builder()
            .code("200")
            .message("ok")
            .data(teamService.getTeam(teamId)).build();
    }

    //팀 등록
    @PostMapping("/teams")
    public CommonResponse<teamIdRes> joinTeam(
        @RequestBody @Valid TeamDto.JoinTeam dto) {

        return CommonResponse.<teamIdRes>builder()
            .code("200")
            .message("ok")
            .data(teamService.joinTeam(dto)).build();
    }

    //팀 수정
    //@PreAuthorize("isAuthenticated() and ( hasRole('ROLE_ADMIN'))" )
    @PatchMapping(value = "/admin/teams/{teamId}")
    public CommonResponse<SelectTeamRes> updateTeamByAdmin(
        @PathVariable("teamId") final Long teamId,
        @RequestBody @Valid TeamDto.updateTeamByAdminReq dto) {

        return CommonResponse.<SelectTeamRes>builder()
            .code("200")
            .message("ok")
            .data(teamService.updateTeamByAdmin(teamId, dto)).build();
    }

    //팀 삭제
    //@PreAuthorize("isAuthenticated() and ( hasRole('ROLE_ADMIN'))" )
    @DeleteMapping(value = { "/admin/teams/{teamId}"})
    public CommonResponse<Map> deleteTeam(
        @PathVariable("teamId") final Long teamId) {

        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("result", teamService.deleteTeam(teamId));

        return CommonResponse.<Map>builder()
            .code("200")
            .message("ok")
            .data(map)
            .build();
    }

}
