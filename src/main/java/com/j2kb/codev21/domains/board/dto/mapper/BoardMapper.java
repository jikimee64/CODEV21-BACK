package com.j2kb.codev21.domains.board.dto.mapper;

import java.util.stream.Collectors;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;

import com.j2kb.codev21.domains.board.domain.Board;
import com.j2kb.codev21.domains.board.dto.BoardDto;
import com.j2kb.codev21.domains.board.dto.BoardDto.GisuInfo;
import com.j2kb.codev21.domains.board.dto.BoardDto.TeamInfo;
import com.j2kb.codev21.domains.board.dto.BoardDto.Writer;

@Mapper(componentModel = "spring")
public interface BoardMapper {

	BoardDto.Res boardToRes(Board board);
	
	@Mapping(target = "image", ignore = true)
	@Mapping(target = "writer", ignore = true)
	@Mapping(target = "user", ignore = true)
	Board reqToBoard(BoardDto.Req req);
}
