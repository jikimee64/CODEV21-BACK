package com.j2kb.codev21.domains.board.dto.mapper;

import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.j2kb.codev21.domains.board.domain.Board;
import com.j2kb.codev21.domains.board.dto.BoardDto;
import com.j2kb.codev21.domains.board.dto.BoardDto.GisuInfo;
import com.j2kb.codev21.domains.board.dto.BoardDto.Res;
import com.j2kb.codev21.domains.board.dto.BoardDto.Res.ResBuilder;
import com.j2kb.codev21.domains.board.dto.BoardDto.TeamInfo;
import com.j2kb.codev21.domains.board.dto.BoardDto.Writer;

@Mapper(componentModel = "spring")
public interface BoardMapper {
	
	default BoardDto.Res boardToRes(Board board) {
        if ( board == null ) {
            return null;
        }

        ResBuilder res = Res.builder();

        res.board( board );
        res.content( board.getContent() );
        res.createdAt( board.getCreatedAt() );
        if ( board.getId() != null ) {
            res.id( board.getId() );
        }
        res.image( board.getImage() );
        res.summary( board.getSummary() );
        res.title( board.getTitle() );
        res.updatedAt( board.getUpdatedAt() );
        if(board.getGisuCategory() != null)
        	res.gisuInfo( new GisuInfo(board.getGisuCategory().getId(), board.getGisuCategory().getGisu()) );
        if(board.getUser() != null)
        	res.writerInfo( new Writer(board.getUser().getId(), board.getUser().getName()) );
        if(board.getTeam() != null)
        	res.teamInfo( new TeamInfo(board.getTeam().getId(), board.getTeam().getTeamName(), board.getTeam().getUserTeams().stream().map(userTeam -> userTeam.getUser().getName()).collect(Collectors.toList())) );

        return res.build();
	}
	
	@Mapping(target = "image", ignore = true)
	@Mapping(target = "writer", ignore = true)
	@Mapping(target = "user", ignore = true)
	Board reqToBoard(BoardDto.Req req);
}
