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
import com.j2kb.codev21.domains.vote.repository.BoardVoteRepository;
import com.j2kb.codev21.domains.vote.repository.UserBoardVoteRepository;
import com.j2kb.codev21.domains.vote.repository.VoteRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoteService {
	
	private final VoteRepository voteRepository;
	private final BoardRepository boardRepository;
	private final BoardVoteRepository boardVoteRepository;
	private final UserBoardVoteRepository userBoardVoteRepository;
	private final UserRepository userRepository;

	public List<VoteDto.Res> getVoteList(VoteSearchCondition condition) {
		return voteRepository.search(condition);
	}

	public VoteDto.Res getVote(long id) {
		Optional<Vote> vote = voteRepository.findById(id);
		
		if(vote.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		return new VoteDto.Res(vote.get());
	}

	@Transactional
	public Boolean insertVoteOfUser(long userId, long boardVoteId) {
		// TODO: 유저 데이터 접근 권한 검증 로직 추가
		User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
		BoardVote boardVote = boardVoteRepository.findById(boardVoteId).orElseThrow(IllegalArgumentException::new);
		
		// 유저 투표 중복 데이터 검증
		Optional<UserBoardVote> userBoardVote = userBoardVoteRepository.findByUserAndVote(user, boardVote.getVote());
		if(userBoardVote.isPresent()) {
			throw new IllegalArgumentException();
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
		User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
		BoardVote boardVote = boardVoteRepository.findById(boardVoteId).orElseThrow(IllegalArgumentException::new);
		
		Optional<UserBoardVote> userBoardVote = userBoardVoteRepository.findByUserAndBoardVote(user, boardVote);
		if(userBoardVote.isPresent()) {
			userBoardVoteRepository.delete(userBoardVote.get());
			boardVote.decreaseCount();
		} else {
			throw new IllegalArgumentException();
		}
		
		
		return true;
	}

	@Transactional
	public Long insertVote(VoteDto.Req req) {
		Vote vote = Vote.builder()
						.startDate(req.getStartDate())
						.endDate(req.getEndDate())
						.build();
		
		// 투표 시작 날짜와 투표 종료 날짜가 동일한 row가 중복될 경우 예외 처리
		if(voteRepository.findByStartDateAndEndDate(req.getStartDate(), req.getEndDate())
				.isPresent()) {
			throw new IllegalArgumentException();
		}
		
		List<Long> boardIds = req.getBoardIds();
		
		voteRepository.save(vote);
		
		for (Long boardId : boardIds) {
			Board board = boardRepository.findById(boardId)
					.orElseThrow(IllegalArgumentException::new);
			vote.addBoardVote(BoardVote.builder()
										.count(0)
										.vote(vote)
										.board(board)
										.build());
		}

		return vote.getId();
	}

	@Transactional
	public VoteDto.Res updateVote(long id, VoteDto.Req req) {
		Vote vote = voteRepository.findById(id).orElseThrow();
		vote.updateVote(req);
		
		return new VoteDto.Res(vote);
	}

	@Transactional
	public Boolean deleteVote(long id) {
		voteRepository.deleteById(id);
		return true;
	}

	@Transactional
	public List<BoardVoteDto.Res> includeBoardIntoVote(long voteId, long boardId) {
		Vote vote = voteRepository.findById(voteId).orElseThrow();
		Board board = boardRepository.findById(boardId).orElseThrow();
		
		// 요청 투표에 동일한 게시판이 등록된 경우 중복 예외 처리
		Optional<BoardVote> boardVotes = boardVoteRepository.findByVoteAndBoard(vote, board);
		if(boardVotes.isPresent()) {	
			throw new IllegalArgumentException();
		}

			
		vote.addBoardVote(BoardVote.builder()
										.count(0)
										.vote(vote)
										.board(board)
										.build());

		return boardVoteRepository.findByVote(vote).stream()
						.map(BoardVoteDto.Res::new)
						.collect(Collectors.toList());
	}

	@Transactional
	public List<BoardVoteDto.Res> includeBoardListIntoVote(long voteId, BoardVoteDto.Req boardVoteReq) {
		Vote vote = voteRepository.findById(voteId).orElseThrow();
		List<Board> boards = boardRepository.findAllById(boardVoteReq.getBoardIds());
		
		// 요청 투표에 동일한 게시판이 등록된 경우 중복 예외 처리
		List<BoardVote> foundBoardVotes = boardVoteRepository.findByVoteAndBoardIn(vote, boards);
		if(!foundBoardVotes.isEmpty()) {	
			throw new IllegalArgumentException();
		}
		
		vote.getBoardVotes().addAll(boards.stream()
										.map(board -> BoardVote.builder()
														.count(0)
														.vote(vote)
														.board(board)
														.build())
										.collect(Collectors.toList()));
		
		List<BoardVote> boardVotes = boardVoteRepository.findByVote(vote);
		return boardVotes.stream()
				.map(BoardVoteDto.Res::new)
				.collect(Collectors.toList());
	}

	@Transactional
	public List<BoardVoteDto.Res> excludeBoardInVote(long voteId, long boardId) {
		Vote vote = voteRepository.findById(voteId).orElseThrow(IllegalArgumentException::new);
		Board board = boardRepository.findById(boardId).orElseThrow(IllegalArgumentException::new);
		
		boardVoteRepository.deleteByVoteAndBoard(vote, board);
		
		List<BoardVote> boardVotes = boardVoteRepository.findByVote(vote);
		return boardVotes.stream()
						.map(BoardVoteDto.Res::new)
						.collect(Collectors.toList());
	}

	@Transactional
	public List<BoardVoteDto.Res> excludeBoardListInVote(long voteId, BoardVoteDto.Req boardVoteReq) {
		Vote vote = voteRepository.findById(voteId).orElseThrow(IllegalArgumentException::new);
		List<Board> boards = boardRepository.findAllById(boardVoteReq.getBoardIds());
		if(boards.isEmpty())
			throw new IllegalArgumentException();
		
		boards.stream()
				.forEach(board -> {
					boardVoteRepository.deleteByVoteAndBoard(vote, board);
				});
		
		List<BoardVote> boardVotes = boardVoteRepository.findByVote(vote);
		return boardVotes.stream()
						.map(BoardVoteDto.Res::new)
						.collect(Collectors.toList());
	}
}
