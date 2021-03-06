package com.j2kb.codev21.domains.vote.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.j2kb.codev21.domains.vote.dto.BoardVoteDto;
import com.j2kb.codev21.domains.vote.dto.VoteDto;
import com.j2kb.codev21.domains.vote.dto.VoteSearchCondition;
import com.j2kb.codev21.domains.vote.dto.mapper.VoteMapper;
import com.j2kb.codev21.domains.vote.service.VoteService;
import com.j2kb.codev21.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class VoteController {
	
	private final VoteService voteService;
	
	private final VoteMapper voteMapper;
	
	// TODO: 관리자 접근 권한 검증
	@GetMapping("/admin/votes")
	public CommonResponse<List<VoteDto.Res>> getVoteList(VoteSearchCondition condition) {
		return CommonResponse.<List<VoteDto.Res>>builder()
					.code("200")
					.message("ok")
					.data(voteService.getVoteList(condition))
					.build();
	}
	
	
	// TODO: 관리자 접근 권한 검증
	@GetMapping("/admin/votes/{voteId}")
	public CommonResponse<VoteDto.Res> getVote(@PathVariable("voteId") long voteId) {
		return CommonResponse.<VoteDto.Res>builder()
				.code("200")
				.message("ok")
				.data(voteService.getVote(voteId))
				.build();
	}
	
	// TODO: 사용자 인증 정보 파라미터 받아오기
	@PostMapping("/votes/boards/{boardVoteId}/members")
	public CommonResponse<HashMap<String, Boolean>> insertVoteOfUser(@PathVariable("boardVoteId") long boardVoteId) {
		long userId = 0l;
		HashMap<String, Boolean> wrapper = new HashMap<String, Boolean>();
		Boolean result = voteService.insertVoteOfUser(userId, boardVoteId);
		wrapper.put("result", result);
		
		return CommonResponse.<HashMap<String, Boolean>>builder()
						.code("200")
						.message("ok")
						.data(wrapper)
						.build();
	}
	
	// TODO: 사용자 인증 정보 파라미터 받아오기
	@DeleteMapping("/votes/boards/{boardVoteId}/members")
	public CommonResponse<HashMap<String, Boolean>> cancleVoteOfUser(@PathVariable("boardVoteId") long boardVoteId) {
		long userId = 0l;
		HashMap<String, Boolean> wrapper = new HashMap<String, Boolean>();
		Boolean result = voteService.cancleVoteOfUser(userId, boardVoteId);
		wrapper.put("result", result);
		
		return CommonResponse.<HashMap<String, Boolean>>builder()
						.code("200")
						.message("ok")
						.data(wrapper)
						.build();
	}
	
	// TODO: 관리자 접근 권한 검증
	@PostMapping("/admin/votes")
	public CommonResponse<VoteDto.Res> insertVote(VoteDto.Req req) {
		return CommonResponse.<VoteDto.Res>builder()
				.code("200")
				.message("ok")
				.data(voteService.insertVote(voteMapper.reqToVote(req), req.getBoardIds()))
				.build();
	}
	
	// TODO: 관리자 접근 권한 검증
	@PatchMapping("/admin/votes/{voteId}")
	public CommonResponse<VoteDto.Res> updateVote(@PathVariable("voteId") long voteId, VoteDto.Req req) {
		return CommonResponse.<VoteDto.Res>builder()
				.code("200")
				.message("ok")
				.data(voteService.updateVote(voteId, voteMapper.reqToVote(req)))
				.build();
	}
	
	// TODO: 관리자 접근 권한 검증
	@DeleteMapping("/admin/votes/{voteId}")
	public CommonResponse<HashMap<String, Boolean>> deleteVote(@PathVariable("voteId") long voteId) {
		HashMap<String, Boolean> dataWrapper = new HashMap<String, Boolean>();
		Boolean result = voteService.deleteVote(voteId);
		dataWrapper.put("result", result);
		
		return CommonResponse.<HashMap<String, Boolean>>builder()
						.code("200")
						.message("ok")
						.data(dataWrapper)
						.build();
	}
	
	// TODO: 관리자 접근 권한 검증
	@PostMapping("/admin/votes/{voteId}/boards")
	public CommonResponse<List<BoardVoteDto.Res>> includeBoardListIntoVote(@PathVariable("voteId") long voteId, BoardVoteDto.Req boardVoteReq) {
		return CommonResponse.<List<BoardVoteDto.Res>>builder()
						.code("200")
						.message("ok")
						.data(voteService.includeBoardListIntoVote(voteId, boardVoteReq.getBoardIds()))
						.build();
	}
	
	// TODO: 관리자 접근 권한 검증
	@PostMapping("/admin/votes/{voteId}/boards/{boardId}")
	public CommonResponse<List<BoardVoteDto.Res>> includeBoardIntoVote(@PathVariable("voteId") long voteId, @PathVariable("boardId") long boardVoteId) {
		return CommonResponse.<List<BoardVoteDto.Res>>builder()
						.code("200")
						.message("ok")
						.data(voteService.includeBoardIntoVote(voteId, boardVoteId))
						.build();
	}
	
	// TODO: 관리자 접근 권한 검증
	@DeleteMapping("/admin/votes/{voteId}/boards")
	public CommonResponse<List<BoardVoteDto.Res>> excludeBoardListInVote(@PathVariable("voteId") long voteId, BoardVoteDto.Req boardVoteReq) {
		return CommonResponse.<List<BoardVoteDto.Res>>builder()
						.code("200")
						.message("ok")
						.data(voteService.excludeBoardListInVote(voteId, boardVoteReq.getBoardIds()))
						.build();
	}
	
	// TODO: 관리자 접근 권한 검증
	@DeleteMapping("/admin/votes/{voteId}/boards/{boardId}")
	public CommonResponse<List<BoardVoteDto.Res>> excludeBoardInVote(@PathVariable("voteId") long voteId, @PathVariable("boardId") long boardVoteId) {
		return CommonResponse.<List<BoardVoteDto.Res>>builder()
						.code("200")
						.message("ok")
						.data(voteService.excludeBoardInVote(voteId, boardVoteId))
						.build();
	}
	
}
