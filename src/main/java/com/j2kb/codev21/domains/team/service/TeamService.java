package com.j2kb.codev21.domains.team.service;

import com.j2kb.codev21.domains.team.dto.TeamDto;
import com.j2kb.codev21.domains.team.dto.TeamDto.SelectTeamRes;
import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.dto.mapper.UserMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TeamService {

    //팀 전체 조회
    public List<SelectTeamRes> getTeamList(){
        return new ArrayList<>();
    }

    //팀 단건 조회
    public TeamDto.SelectTeamRes getTeam(Long teamId){
        return TeamDto.SelectTeamRes.builder().build();
    }

    //팀 등록
    public TeamDto.SelectTeamRes joinTeam(TeamDto.Req dto){
        return TeamDto.SelectTeamRes.builder().build();
    }

    //팀 수정
    public TeamDto.SelectTeamRes updateTeamByAdmin(Long teamId, TeamDto.Req dto){
        return TeamDto.SelectTeamRes.builder().build();
    }

    //팀 삭제
    public TeamDto.DeleteTeamCheckRes deleteTeam(Long teamId){
        return TeamDto.DeleteTeamCheckRes.builder().build();
    }


}
