package com.j2kb.codev21.domains.team.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.j2kb.codev21.domains.gisucategory.dto.mapper.GisuCategoryMapper;
import com.j2kb.codev21.domains.team.domain.Team;
import com.j2kb.codev21.domains.team.domain.UserTeam;
import com.j2kb.codev21.domains.team.dto.TeamDto;

@Mapper(componentModel = "spring", uses = { GisuCategoryMapper.class })
public interface TeamMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "boards", ignore = true)
	@Mapping(target = "userTeams", ignore = true)
	@Mapping(target = "gisuCategory", ignore = true)
	Team reqToTeam(TeamDto.Req req);
	
	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "userName", source = "user.name")
	@Mapping(target = "isLeader", source = "leader")
	TeamDto.TeamMemberList userTeamToTeamMemeber(UserTeam userTeam);
	
	@Mapping(target = "gisuInfo", source = "gisuCategory")
	@Mapping(target = "teamMemberLists", source = "userTeams")
	TeamDto.SelectTeamRes teamToRes(Team team);
}
