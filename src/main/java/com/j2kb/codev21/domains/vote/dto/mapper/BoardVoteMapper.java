package com.j2kb.codev21.domains.vote.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.j2kb.codev21.domains.vote.domain.BoardVote;
import com.j2kb.codev21.domains.vote.dto.BoardVoteDto;

@Mapper(componentModel = "spring")
public interface BoardVoteMapper {
	
	@Mapping(target = "boardId", source = "board.id")
	@Mapping(target = "boardVoteId", source = "id")
	@Mapping(target = "title", source = "board.title")
	BoardVoteDto.Res boardVoteToRes(BoardVote boardVote);
}
