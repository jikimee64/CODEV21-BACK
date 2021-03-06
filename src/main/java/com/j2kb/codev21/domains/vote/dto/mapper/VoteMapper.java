package com.j2kb.codev21.domains.vote.dto.mapper;

import org.mapstruct.Mapper;

import com.j2kb.codev21.domains.vote.domain.Vote;
import com.j2kb.codev21.domains.vote.dto.VoteDto;

@Mapper(componentModel = "spring")
public interface VoteMapper {
	
	Vote reqToVote(VoteDto.Req req);
}
