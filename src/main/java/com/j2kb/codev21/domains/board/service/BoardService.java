package com.j2kb.codev21.domains.board.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.j2kb.codev21.domains.board.dto.BoardDto;
import com.j2kb.codev21.domains.board.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;

	public List<BoardDto.Res> getBoardList(String gisu) {
		return new ArrayList<>();
		
	}

	public BoardDto.Res getBoard(Long id) {
		return new BoardDto.Res();
		
	}

	public BoardDto.Res insertBoard(BoardDto.Req req) {
		return new BoardDto.Res();
		
	}

	public BoardDto.Res updateBoard(long id, BoardDto.Req req) {
		return new BoardDto.Res();
	}

	public boolean deleteBoard(Long id) {
		return true;
	}
}
