package com.j2kb.codev21.domains.vote.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.domains.vote.domain.BoardVote;
import com.j2kb.codev21.domains.vote.domain.UserBoardVote;
import com.j2kb.codev21.domains.vote.domain.Vote;

public interface UserBoardVoteRepository extends JpaRepository<UserBoardVote, Long> {
	
	public Optional<UserBoardVote> findByUserAndVote(User user, Vote vote);
	
	public Optional<UserBoardVote> findByUserAndBoardVote(User user, BoardVote boardVote);
	
	public void deleteByUserAndBoardVote(User user, BoardVote boardVote);
}
