package com.j2kb.codev21.domains.board.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.j2kb.codev21.domains.board.dto.BoardDto;
import com.j2kb.codev21.domains.board.service.BoardService;
import com.j2kb.codev21.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	@GetMapping("/{boardId}")
	public CommonResponse<BoardDto.Res> getBoard(@PathVariable("boardId") Long boardId) {
		return CommonResponse.<BoardDto.Res>builder()
				.code("200")
				.message("ok")
				.data(boardService.getBoard(boardId))
				.build();
	}
	
	@PostMapping
	public CommonResponse<BoardDto.Res> insertBoard(@RequestPart("json-data") BoardDto.Req req, 
													@RequestPart(name = "image-file", required = false) MultipartFile image) {
		log.info(req.toString());
		return CommonResponse.<BoardDto.Res>builder()
				.code("200")
				.message("ok")
				.data(boardService.insertBoard(req))
				.build();
	}
	
	@PatchMapping("/{boardId}")
	public CommonResponse<BoardDto.Res> updateBoard(@PathVariable("boardId") long boardId, 
													@RequestPart(name = "json-data", required = false) BoardDto.Req req,
													@RequestPart(name = "image-file", required = false) MultipartFile image) {
		return CommonResponse.<BoardDto.Res>builder()
				.code("200")
				.message("ok")
				.data(boardService.updateBoard(boardId, req))
				.build();
	}
	
	@DeleteMapping("/{boardId}")
	public CommonResponse<HashMap<String, Boolean>> deleteBoard(@PathVariable("boardId") long boardId) {
		HashMap<String, Boolean> res = new HashMap<String, Boolean>();
		res.put("result", boardService.deleteBoard(boardId));
		
		return CommonResponse.<HashMap<String, Boolean>>builder()
				.code("200")
				.message("ok")
				.data(res)
				.build();
	}
}
