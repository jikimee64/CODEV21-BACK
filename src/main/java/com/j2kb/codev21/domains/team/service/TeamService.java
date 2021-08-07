package com.j2kb.codev21.domains.team.service;

import com.j2kb.codev21.domains.gisucategory.domain.GisuCategory;
import com.j2kb.codev21.domains.gisucategory.repository.GisuCategoryRepository;
import com.j2kb.codev21.domains.team.domain.Team;
import com.j2kb.codev21.domains.team.domain.UserTeam;
import com.j2kb.codev21.domains.team.dto.TeamDto;
import com.j2kb.codev21.domains.team.dto.TeamDto.SelectTeamRes;
import com.j2kb.codev21.domains.team.dto.mapper.TeamMapper;
import com.j2kb.codev21.domains.team.repository.TeamRepository;
import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.dto.mapper.UserMapper;
import com.j2kb.codev21.domains.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TeamService {
	
	private final TeamMapper teamMapper;
	
	private final TeamRepository teamRepository;

	private final GisuCategoryRepository gisuCategoryRepository;
	private final UserRepository userRepository;
	
    //팀 전체 조회
    public List<SelectTeamRes> getTeamList(){
    	List<Team> result = teamRepository.findAll();
        return result.stream()
        			.map(teamMapper::teamToRes)
        			.collect(Collectors.toList());
    }

    //팀 단건 조회
    public TeamDto.SelectTeamRes getTeam(Long teamId){
    	Team result = teamRepository.findById(teamId)
    			.orElseThrow();
        return teamMapper.teamToRes(result);
    }

    //팀 등록
    @Transactional
    public TeamDto.SelectTeamRes insertTeam(Team teamParam, Long gisuId, List<TeamDto.TeamMemberList> teamMemberList){
    	Team team = teamRepository.save(teamParam);
    	GisuCategory gisuCatgegory = gisuCategoryRepository.findById(gisuId)
    			.orElseThrow();
    	
    	team.changeGisuCategory(gisuCatgegory);
    	
    	for (TeamDto.TeamMemberList teamMember : teamMemberList) {
			User user = userRepository.findById(teamMember.getUserId())
				.orElseThrow();
			UserTeam userTeam = UserTeam.builder().leader(teamMember.getIsLeader()).build();
			userTeam.setUser(user);
			team.addUserTeam(userTeam);
		}
    	
    	teamRepository.flush();

        return teamMapper.teamToRes(team);
    }

    //팀 수정
    @Transactional
    public TeamDto.SelectTeamRes updateTeamByAdmin(Long teamId, TeamDto.Req dto){
    	Team team = teamRepository.findById(teamId)
    			.orElseThrow();
    	GisuCategory gusyCategory = gisuCategoryRepository.findById(dto.getGisuId())
    				.orElseThrow();
    	team.changeGisuCategory(gusyCategory);
    	
    	team.cleanUserTeams();
    	
    	for (TeamDto.TeamMemberList teamMember : dto.getTeamMemberLists()) {
			User user = userRepository.findById(teamMember.getUserId())
				.orElseThrow();
			UserTeam userTeam = UserTeam.builder().leader(teamMember.getIsLeader()).build();
			userTeam.setUser(user);
			team.addUserTeam(userTeam);
		}
    	teamRepository.flush();
    	
        return teamMapper.teamToRes(team);
    }

    //팀 삭제
    @Transactional
    public TeamDto.DeleteTeamCheckRes deleteTeam(Long teamId){
    	teamRepository.deleteById(teamId);
        return TeamDto.DeleteTeamCheckRes.builder().checkFlag(true).build();
    }


}
