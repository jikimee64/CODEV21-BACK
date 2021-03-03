package com.j2kb.codev21.domains.vote.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.j2kb.codev21.domains.vote.dto.mapper.VoteMapper;
import com.j2kb.codev21.domains.vote.repository.BoardVoteRepository;
import com.j2kb.codev21.domains.vote.repository.UserBoardVoteRepository;
import com.j2kb.codev21.domains.vote.repository.VoteRepoistory;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoteService {
	
	private final VoteRepoistory voteRepository;
	private final BoardRepository boardRepository;
	private final BoardVoteRepository boardVoteRepository;
	private final UserBoardVoteRepository userBoardVoteRepository;
	private final UserRepository userRepository;

	public List<VoteDto.Res> getVoteList() {
		List<Vote> votes = voteRepository.findAll();
		List<VoteDto.Res> result = votes.stream()
					.map(vote -> new VoteDto.Res(vote))
					.collect(Collectors.toList());
		return result;
	}

	public VoteDto.Res getVote(long id) {
		Optional<Vote> vote = voteRepository.findById(id);
		return new VoteDto.Res(vote.orElseThrow());
	}

	@Transactional
	public Boolean insertVoteOfUser(long userId, long boardVoteId) {
		User user = userRepository.findById(userId).orElseThrow();
		BoardVote boardVote = boardVoteRepository.findById(boardVoteId).orElseThrow();
		
		UserBoardVote userBoardVote = new UserBoardVote(user, boardVote.getVote(), boardVote);
		userBoardVoteRepository.save(userBoardVote);
		boardVote.increaseCount();
		
		return true;
	}
	
	@Transactional
	public Boolean cancleVoteOfUser(long userId, long boardVoteId) {
		User user = userRepository.findById(userId).orElseThrow();
		BoardVote boardVote = boardVoteRepository.findById(boardVoteId).orElseThrow();
		
		userBoardVoteRepository.deleteByUserAndBoardVote(user, boardVote);
		boardVote.decreaseCount();
		
		return true;
	}

	@Transactional
	public Long insertVote(VoteDto.Req req) {
		Vote vote = new Vote(req.getStartDate(), req.getEndDate());
		
		List<Long> boardIds = req.getBoardIds();
		
		voteRepository.save(vote);
		
		for (Long boardId : boardIds) {
			Board board = boardRepository.findById(boardId).orElseThrow();
			boardVoteRepository.save(new BoardVote(0, vote, board));
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
		
		boardVoteRepository.saveAndFlush(new BoardVote(0, vote, board));
		
		List<BoardVote> boardVotes = boardVoteRepository.findByVote(vote);
		return boardVotes.stream()
						.map(BoardVoteDto.Res::new)
						.collect(Collectors.toList());
	}

	@Transactional
	public List<BoardVoteDto.Res> includeBoardListIntoVote(long voteId, BoardVoteDto.Req boardVoteReq) {
		Vote vote = voteRepository.findById(voteId).orElseThrow();
		List<Board> boards = boardRepository.findAllById(boardVoteReq.getBoardIds());
		
		boards.stream()
				.forEach(board -> {
					boardVoteRepository.save(new BoardVote(0, vote, board));
				});
		boardVoteRepository.flush();
		
		List<BoardVote> boardVotes = boardVoteRepository.findByVote(vote);
		return boardVotes.stream()
				.map(BoardVoteDto.Res::new)
				.collect(Collectors.toList());
	}

	@Transactional
	public List<BoardVoteDto.Res> excludeBoardInVote(long voteId, long boardId) {
		Vote vote = voteRepository.findById(voteId).orElseThrow();
		Board board = boardRepository.findById(boardId).orElseThrow();
		
		boardVoteRepository.deleteByVoteAndBoard(vote, board);
		
		List<BoardVote> boardVotes = boardVoteRepository.findByVote(vote);
		return boardVotes.stream()
						.map(BoardVoteDto.Res::new)
						.collect(Collectors.toList());
	}

	@Transactional
	public List<BoardVoteDto.Res> excludeBoardListInVote(long voteId, BoardVoteDto.Req boardVoteReq) {
		Vote vote = voteRepository.findById(voteId).orElseThrow();
		List<Board> boards = boardRepository.findAllById(boardVoteReq.getBoardIds());
		
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
