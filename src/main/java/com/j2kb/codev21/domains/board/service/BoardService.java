package com.j2kb.codev21.domains.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.j2kb.codev21.domains.board.domain.Board;
import com.j2kb.codev21.domains.board.dto.BoardDto;
import com.j2kb.codev21.domains.board.dto.BoardSearchCondition;
import com.j2kb.codev21.domains.board.dto.mapper.BoardMapper;
import com.j2kb.codev21.domains.board.repository.BoardRepository;
import com.j2kb.codev21.domains.gisucategory.domain.GisuCategory;
import com.j2kb.codev21.domains.gisucategory.repository.GisuCategoryRepository;
import com.j2kb.codev21.domains.team.domain.Team;
import com.j2kb.codev21.domains.team.repository.TeamRepository;
import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.domains.user.repository.UserRepository;
import com.j2kb.codev21.global.error.ErrorCode;
import com.j2kb.codev21.global.error.exception.InvalidValueException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

	private final BoardMapper boardMapper;
	
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final GisuCategoryRepository gisuCategoryRepository;

	public List<BoardDto.Res> getBoardList(BoardSearchCondition condition) {
		List<Board> results = boardRepository.search(condition);
		

		return results.stream()
				.map(boardMapper::boardToRes)
				.collect(Collectors.toList());
		
	}

	public BoardDto.Res getBoard(Long id) {
		Board board = boardRepository.findById(id)
				.orElseThrow(() -> new InvalidValueException(ErrorCode.NO_BOARD_FOUND));
		return boardMapper.boardToRes(board);
		
	}

	public BoardDto.Res insertBoard(Board boardParam, long teamId, long gisuId, long userId) {
		User user = userRepository.findById(userId).orElseThrow();
		Board board = boardRepository.save(boardParam);
		Team team = teamRepository.findById(teamId).orElseThrow();
		GisuCategory gisuCategory = gisuCategoryRepository.findById(gisuId).orElseThrow();
		
		board.setUser(user);
		board.setTeam(team);
		board.setGisuCategory(gisuCategory);
		
		boardRepository.flush();
		
		return boardMapper.boardToRes(board);
		
	}

	public BoardDto.Res updateBoard(long boardId, Board boardParam, Long teamId, Long gisuId, long userId) {
		Team team = null;
		GisuCategory gisuCategory = null;
		
		User user = userRepository.findById(userId).orElseThrow();
		Board board = boardRepository.findById(boardId).orElseThrow();
		if(teamId != null)
			team = teamRepository.findById(teamId).orElseThrow();
		if(gisuId != null)
			gisuCategory = gisuCategoryRepository.findById(gisuId).orElseThrow();
		
		board.update(boardParam);
		
		if(team != null)
			board.setTeam(team);
		
		if(gisuCategory != null)
			board.setGisuCategory(gisuCategory);
		
		boardRepository.flush();
		return boardMapper.boardToRes(board);
	}

	public boolean deleteBoard(long boardId) {
		boardRepository.deleteById(boardId);
		return true;
	}
}
