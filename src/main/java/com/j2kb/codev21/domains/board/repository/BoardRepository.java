package com.j2kb.codev21.domains.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.j2kb.codev21.domains.board.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{
	
}
