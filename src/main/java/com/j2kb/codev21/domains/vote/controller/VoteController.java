package com.j2kb.codev21.domains.vote.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.j2kb.codev21.domains.vote.dto.BoardVoteDto;
import com.j2kb.codev21.domains.vote.dto.VoteDto;
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
	
	@GetMapping("/admin/votes")
	public CommonResponse<List<VoteDto.Res>> getVoteList() {
		return CommonResponse.<List<VoteDto.Res>>builder()
					.code("200")
					.message("ok")
					.data(voteService.getVoteList())
					.build();
	}
	
	@GetMapping("/admin/votes/{id}")
	public CommonResponse<VoteDto.Res> getVote(@PathVariable("id") long id) {
		return CommonResponse.<VoteDto.Res>builder()
				.code("200")
				.message("ok")
				.data(voteService.getVote(id))
				.build();
	}
	
	@PostMapping("/votes/boards/{boardVoteId}/members")
	public CommonResponse<HashMap<String, Boolean>> insertVoteOfUser(@PathVariable("boardVoteId") long boardVoteId) {
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", true);
		
		return CommonResponse.<HashMap<String, Boolean>>builder()
						.code("200")
						.message("ok")
						.data(result)
						.build();
	}
	
	@DeleteMapping("/votes/boards/{boardVoteId}/members")
	public CommonResponse<HashMap<String, Boolean>> deleteVoteOfUser(@PathVariable("boardVoteId") long boardVoteId) {
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", true);
		
		return CommonResponse.<HashMap<String, Boolean>>builder()
						.code("200")
						.message("ok")
						.data(result)
						.build();
	}
	
	@PostMapping("/admin/votes")
	public CommonResponse<VoteDto.Res> insertVote(VoteDto.Req req) {
		return CommonResponse.<VoteDto.Res>builder()
				.code("200")
				.message("ok")
				.data(voteService.insertVote(req))
				.build();
	}
	
	@PatchMapping("/admin/votes/{id}")
	public CommonResponse<VoteDto.Res> updateVote(@PathVariable("id") long id, VoteDto.Req req) {
		return CommonResponse.<VoteDto.Res>builder()
				.code("200")
				.message("ok")
				.data(voteService.updateVote(id, req))
				.build();
	}
	
	@DeleteMapping("/admin/votes/{id}")
	public CommonResponse<HashMap<String, Boolean>> deleteVote(@PathVariable("id") long id) {
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", true);
		
		return CommonResponse.<HashMap<String, Boolean>>builder()
						.code("200")
						.message("ok")
						.data(result)
						.build();
	}
	
	@PostMapping("/admin/votes/{voteId}/boards")
	public CommonResponse<List<BoardVoteDto.Res>> includeBoardListIntoVote(@PathVariable("voteId") long voteId, BoardVoteDto.Req boardVoteReq) {
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", true);
		
		return CommonResponse.<List<BoardVoteDto.Res>>builder()
						.code("200")
						.message("ok")
						.data(voteService.includeBoardListIntoVote(voteId, boardVoteReq))
						.build();
	}
	
	@PostMapping("/admin/votes/{voteId}/boards/{boardId}")
	public CommonResponse<List<BoardVoteDto.Res>> includeBoardIntoVote(@PathVariable("voteId") long voteId, @PathVariable("boardId") long boardVoteId) {
		
		return CommonResponse.<List<BoardVoteDto.Res>>builder()
						.code("200")
						.message("ok")
						.data(voteService.includeBoardIntoVote(voteId, boardVoteId))
						.build();
	}
	
	@DeleteMapping("/admin/votes/{id}/boards")
	public CommonResponse<HashMap<String, Boolean>> excludeBoardListInVote(@PathVariable("id") long voteId, BoardVoteDto.Req boardVoteReq) {
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", true);
		
		return CommonResponse.<HashMap<String, Boolean>>builder()
						.code("200")
						.message("ok")
						.data(result)
						.build();
	}
	
	@DeleteMapping("/admin/votes/{voteId}/boards/{boardId}")
	public CommonResponse<HashMap<String, Boolean>> excludeBoardInVote(@PathVariable("voteId") long voteId, @PathVariable("boardId") long boardVoteId) {
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", true);
		
		return CommonResponse.<HashMap<String, Boolean>>builder()
						.code("200")
						.message("ok")
						.data(result)
						.build();
	}
	
}
