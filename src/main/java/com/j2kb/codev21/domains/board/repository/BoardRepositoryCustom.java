package com.j2kb.codev21.domains.board.repository;

import java.util.List;

import com.j2kb.codev21.domains.board.domain.Board;
import com.j2kb.codev21.domains.board.dto.BoardSearchCondition;
import com.j2kb.codev21.domains.vote.domain.Vote;
import com.j2kb.codev21.domains.vote.dto.VoteSearchCondition;

public interface BoardRepositoryCustom {
	
	List<Board> search(BoardSearchCondition condition);

}
