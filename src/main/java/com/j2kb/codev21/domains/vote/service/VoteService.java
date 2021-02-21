package com.j2kb.codev21.domains.vote.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.j2kb.codev21.domains.vote.dto.BoardVoteDto;
import com.j2kb.codev21.domains.vote.dto.VoteDto;
import com.j2kb.codev21.domains.vote.repository.BoardVoteRepository;
import com.j2kb.codev21.domains.vote.repository.VoteRepoistory;
import com.j2kb.codev21.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class VoteService {
	
	private final VoteRepoistory voteRepository;
	private final BoardVoteRepository boardVoteRepository;
	
	public List<VoteDto.Res> getVoteList() {
		return new ArrayList<>();
	}
	
	public VoteDto.Res getVote(long id) {
		
		return new VoteDto.Res();
	}
	
	public Boolean insertVoteOfUser(long userId, long boardVoteId) {
		
		return true;
	}
	
	public VoteDto.Res insertVote(VoteDto.Req req) {
	
		return new VoteDto.Res();
	}
	
	public VoteDto.Res updateVote(long id, VoteDto.Req req) {
		
		return new VoteDto.Res();
	}
	
	public Boolean deleteVote(long id) {
		
		return true;
	}
	

	public List<BoardVoteDto.Res> includeBoardIntoVote(long voteId, long boardId) {
		
		return new ArrayList<>();
	}
	
	public List<BoardVoteDto.Res> includeBoardListIntoVote(long voteId, BoardVoteDto.Req boardVoteReq) {
		
		return new ArrayList<>();
	}
	
	public Boolean excludeBoardInVote(long voteId, long boardId) {
		
		return true;
	}
	
	public Boolean excludeBoardListInVote(long voteId, BoardVoteDto.Req boardVoteReq) {
		
		return true;
	}


}
