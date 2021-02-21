package com.j2kb.codev21.domains.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.j2kb.codev21.domains.vote.domain.BoardVote;

public interface BoardVoteRepository extends JpaRepository<BoardVote, Long>{

}
