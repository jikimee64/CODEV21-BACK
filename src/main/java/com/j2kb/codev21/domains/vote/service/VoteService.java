package com.j2kb.codev21.domains.vote.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.j2kb.codev21.domains.board.domain.Board;
import com.j2kb.codev21.domains.board.repository.BoardRepository;
import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.domains.user.repository.UserRepository;
import com.j2kb.codev21.domains.vote.domain.BoardVote;
import com.j2kb.codev21.domains.vote.domain.UserBoardVote;
import com.j2kb.codev21.domains.vote.domain.Vote;
import com.j2kb.codev21.domains.vote.dto.BoardVoteDto;
import com.j2kb.codev21.domains.vote.dto.VoteDto;
import com.j2kb.codev21.domains.vote.dto.VoteSearchCondition;
import com.j2kb.codev21.domains.vote.dto.mapper.BoardVoteMapper;
import com.j2kb.codev21.domains.vote.dto.mapper.VoteMapper;
import com.j2kb.codev21.domains.vote.repository.BoardVoteRepository;
import com.j2kb.codev21.domains.vote.repository.UserBoardVoteRepository;
import com.j2kb.codev21.domains.vote.repository.VoteRepository;
import com.j2kb.codev21.global.error.ErrorCode;
import com.j2kb.codev21.global.error.exception.InvalidValueException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoteService {
	
	private final VoteMapper voteMapper;
	private final BoardVoteMapper boardVoteMapper;
	
	private final VoteRepository voteRepository;
	private final BoardRepository boardRepository;
	private final BoardVoteRepository boardVoteRepository;
	private final UserBoardVoteRepository userBoardVoteRepository;
	private final UserRepository userRepository;

	public List<VoteDto.Res> getVoteList(VoteSearchCondition condition) {
		return voteRepository.search(condition).stream()
					.map(voteMapper::voteToRes)
					.collect(Collectors.toList());
	}

	public VoteDto.Res getVote(long id) {
		Optional<Vote> vote = voteRepository.findById(id);
		
		if(vote.isEmpty()) {
			throw new InvalidValueException(ErrorCode.NO_VOTE_FOUND);
		} else {
			return voteMapper.voteToRes(vote.get());
		}
	}

	@Transactional
	public Boolean insertVoteOfUser(long userId, long boardVoteId) {
		// TODO: 유저 데이터 접근 권한 검증 로직 추가
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new InvalidValueException(ErrorCode.NO_USER_FOUND));
		BoardVote boardVote = boardVoteRepository.findById(boardVoteId)
				.orElseThrow(() -> new InvalidValueException(ErrorCode.NO_BOARDVOTE_FOUND));
		
		// 유저 투표 중복 데이터 검증
		Optional<UserBoardVote> userBoardVote = userBoardVoteRepository.findByUserAndVote(user, boardVote.getVote());
		if(userBoardVote.isPresent()) {
			throw new InvalidValueException(ErrorCode.USERBOARDVOTE_DUPLICATION);
		}

		boardVote.addUserBoardVote(UserBoardVote.builder()
											.user(user)
											.vote(boardVote.getVote())
											.boardVote(boardVote)
											.build());
		boardVote.increaseCount();
		
		return true;
	}
	
	@Transactional
	public Boolean cancleVoteOfUser(long userId, long boardVoteId) {
		// TODO: 유저 데이터 접근 권한 검증 로직 추가
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new InvalidValueException(ErrorCode.NO_USER_FOUND));
		BoardVote boardVote = boardVoteRepository.findById(boardVoteId)
				.orElseThrow(() -> new InvalidValueException(ErrorCode.NO_BOARDVOTE_FOUND));
		
		Optional<UserBoardVote> userBoardVote = userBoardVoteRepository.findByUserAndBoardVote(user, boardVote);
		if(userBoardVote.isPresent()) {
			userBoardVoteRepository.delete(userBoardVote.get());
			boardVote.decreaseCount();
		} else {
			throw new InvalidValueException(ErrorCode.NO_USERBOARDVOTE_FOUND);
		}
		
		
		return true;
	}

	@Transactional
	public VoteDto.Res insertVote(Vote vote, List<Long> boardIds) {
		// 투표 시작 날짜와 투표 종료 날짜가 동일한 row가 중복될 경우 예외 처리
		if(voteRepository.findByStartDateAndEndDate(vote.getStartDate(), vote.getEndDate())
				.isPresent()) {
			throw new InvalidValueException(ErrorCode.VOTE_DUPLICATION);
		}
		
		voteRepository.save(vote);
		
		for (Long boardId : boardIds) {
			Board board = boardRepository.findById(boardId)
					.orElseThrow(() -> new InvalidValueException(ErrorCode.NO_BOARD_FOUND));
			vote.addBoardVote(BoardVote.builder()
										.count(0)
										.vote(vote)
										.board(board)
										.build());
		}
		voteRepository.flush();

		return voteMapper.voteToRes(vote);
	}

	@Transactional
	public VoteDto.Res updateVote(long voteId, Vote vote) {
		Vote persistVote = voteRepository.findById(voteId)
				.orElseThrow(() -> new InvalidValueException(ErrorCode.NO_VOTE_FOUND));
		persistVote.updateVote(vote);
		
		return voteMapper.voteToRes(persistVote);
	}

	@Transactional
	public Boolean deleteVote(long voteId) {
		Vote vote = voteRepository.findById(voteId)
				.orElseThrow(() -> new InvalidValueException(ErrorCode.NO_VOTE_FOUND));
		voteRepository.delete(vote);
		return true;
	}

	@Transactional
	public List<BoardVoteDto.Res> includeBoardIntoVote(long voteId, long boardId) {
		Vote vote = voteRepository.findById(voteId)
				.orElseThrow(() -> new InvalidValueException(ErrorCode.NO_VOTE_FOUND));
		Board board = boardRepository.findById(boardId)
				.orElseThrow(() -> new InvalidValueException(ErrorCode.NO_BOARD_FOUND));
		
		// 요청 투표에 동일한 게시판이 등록된 경우 중복 예외 처리
		Optional<BoardVote> boardVotes = boardVoteRepository.findByVoteAndBoard(vote, board);
		if(boardVotes.isPresent()) {	
			throw new InvalidValueException(ErrorCode.BOARDVOTE_DUPLICATION);
		}

			
		vote.addBoardVote(BoardVote.builder()
										.count(0)
										.vote(vote)
										.board(board)
										.build());

		return boardVoteRepository.findByVote(vote).stream()
						.map(boardVoteMapper::boardVoteToRes)
						.collect(Collectors.toList());
	}

	@Transactional
	public List<BoardVoteDto.Res> includeBoardListIntoVote(long voteId, List<Long> boardIds) {
		Vote vote = voteRepository.findById(voteId)
				.orElseThrow(() -> new InvalidValueException(ErrorCode.NO_VOTE_FOUND));
		List<Board> boards = boardRepository.findAllById(boardIds);
		if(boards.isEmpty())
			throw new InvalidValueException(ErrorCode.NO_BOARD_FOUND);
		
		// 요청 투표에 동일한 게시판이 등록된 경우 중복 예외 처리
		List<BoardVote> foundBoardVotes = boardVoteRepository.findByVoteAndBoardIn(vote, boards);
		if(!foundBoardVotes.isEmpty()) {	
			throw new InvalidValueException(ErrorCode.BOARDVOTE_DUPLICATION);
		}
		
		vote.getBoardVotes().addAll(boards.stream()
										.map(board -> BoardVote.builder()
														.count(0)
														.vote(vote)
														.board(board)
														.build())
										.collect(Collectors.toList()));
		
		return boardVoteRepository.findByVote(vote).stream()
				.map(boardVoteMapper::boardVoteToRes)
				.collect(Collectors.toList());
	}

	@Transactional
	public List<BoardVoteDto.Res> excludeBoardInVote(long voteId, long boardId) {
		Vote vote = voteRepository.findById(voteId)
				.orElseThrow(() -> new InvalidValueException(ErrorCode.NO_VOTE_FOUND));
		Board board = boardRepository.findById(boardId)
				.orElseThrow(() -> new InvalidValueException(ErrorCode.NO_BOARD_FOUND));
		
		boardVoteRepository.deleteByVoteAndBoard(vote, board);
		
		return boardVoteRepository.findByVote(vote).stream()
				.map(boardVoteMapper::boardVoteToRes)
				.collect(Collectors.toList());
	}

	@Transactional
	public List<BoardVoteDto.Res> excludeBoardListInVote(long voteId, List<Long> boardIds) {
		Vote vote = voteRepository.findById(voteId)
				.orElseThrow(() -> new InvalidValueException(ErrorCode.NO_VOTE_FOUND));
		List<Board> boards = boardRepository.findAllById(boardIds);
		if(boards.isEmpty())
			throw new InvalidValueException(ErrorCode.NO_BOARD_FOUND);
		
		boards.stream()
				.forEach(board -> {
					boardVoteRepository.deleteByVoteAndBoard(vote, board);
				});
		
		return boardVoteRepository.findByVote(vote).stream()
				.map(boardVoteMapper::boardVoteToRes)
				.collect(Collectors.toList());
	}
}
