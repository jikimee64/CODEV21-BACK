package com.j2kb.codev21.domains.vote.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.j2kb.codev21.domains.board.domain.Board;
import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.domains.vote.domain.BoardVote;
import com.j2kb.codev21.domains.vote.domain.Vote;

public interface BoardVoteRepository extends JpaRepository<BoardVote, Long>{
	
	public List<BoardVote> findByVote(Vote vote);
	
	public Optional<BoardVote> findByVoteAndBoard(Vote vote, Board boards);
	
	public List<BoardVote> findByVoteAndBoardIn(Vote vote, List<Board> boards);
	
	public void deleteByVoteAndBoard(Vote vote, Board board);
}
