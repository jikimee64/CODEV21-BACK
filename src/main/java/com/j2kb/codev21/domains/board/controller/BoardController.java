package com.j2kb.codev21.domains.board.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.j2kb.codev21.domains.board.dto.BoardDto;
import com.j2kb.codev21.domains.board.service.BoardService;
import com.j2kb.codev21.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardService boardService;
	
	@GetMapping
	public CommonResponse<List<BoardDto.Res>> getBoardList(@RequestParam("gisu") String gisu) {
		return CommonResponse.<List<BoardDto.Res>>builder()
					.code("200")
					.message("ok")
					.data(boardService.getBoardList(gisu))
					.build();
	}
	
	@GetMapping("/{id}")
	public CommonResponse<BoardDto.Res> getBoard(@PathVariable("id") Long id) {
		return CommonResponse.<BoardDto.Res>builder()
				.code("200")
				.message("ok")
				.data(boardService.getBoard(id))
				.build();
	}
	
	@PostMapping
	public CommonResponse<BoardDto.Res> insertBoard(BoardDto.Req req) {
		return CommonResponse.<BoardDto.Res>builder()
				.code("200")
				.message("ok")
				.data(boardService.insertBoard(req))
				.build();
	}
	
	@PutMapping("/{id}")
	public CommonResponse<BoardDto.Res> updateBoard(@PathVariable("id") long id, BoardDto.Req req) {
		return CommonResponse.<BoardDto.Res>builder()
				.code("200")
				.message("ok")
				.data(boardService.updateBoard(id, req))
				.build();
	}
	
	@DeleteMapping("/{id}")
	public CommonResponse<HashMap<String, Boolean>> deleteBoard(@PathVariable("id") long id) {
		HashMap<String, Boolean> res = new HashMap<String, Boolean>();
		res.put("result", boardService.deleteBoard(id));
		
		return CommonResponse.<HashMap<String, Boolean>>builder()
				.code("200")
				.message("ok")
				.data(res)
				.build();
	}
}
