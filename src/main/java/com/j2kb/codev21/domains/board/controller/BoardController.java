package com.j2kb.codev21.domains.board.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.management.RuntimeErrorException;

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

import com.j2kb.codev21.domains.board.domain.Board;
import com.j2kb.codev21.domains.board.dto.BoardDto;
import com.j2kb.codev21.domains.board.dto.BoardSearchCondition;
import com.j2kb.codev21.domains.board.dto.mapper.BoardMapper;
import com.j2kb.codev21.domains.board.service.BoardService;
import com.j2kb.codev21.domains.board.service.MultipartFileService;
import com.j2kb.codev21.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardMapper boardMapper;
	
	private final BoardService boardService;
	private final MultipartFileService multipartFileService;
	
	@GetMapping
	public CommonResponse<List<BoardDto.Res>> getBoardList(BoardSearchCondition condition) {
		return CommonResponse.<List<BoardDto.Res>>builder()
					.code("200")
					.message("ok")
					.data(boardService.getBoardList(condition))
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
	public CommonResponse<BoardDto.Res> insertBoard(@RequestPart(name = "json-data") BoardDto.Req req, 
													@RequestPart(name = "image-file", required = false) MultipartFile image) 
															throws IllegalStateException, IOException {
		// TODO: 시큐리티 활용하여 유저 정보 추출
		Board boardParam = boardMapper.reqToBoard(req);

		String filePath = multipartFileService.saveFile(image);

		boardParam.setImage(filePath);
		
		
		log.info(req.toString());
		return CommonResponse.<BoardDto.Res>builder()
				.code("200")
				.message("ok")
				.data(boardService.insertBoard(boardParam, req.getTeamId(), req.getGisuId(), 0l))
				.build();
	}
	
	@PatchMapping("/{boardId}")
	public CommonResponse<BoardDto.Res> updateBoard(@PathVariable("boardId") long boardId, 
													@RequestPart(name = "json-data", required = false) BoardDto.Req req,
													@RequestPart(name = "image-file", required = false) MultipartFile image) 
															throws IllegalStateException, IOException {
		// TODO: 시큐리티 활용하여 유저 정보 추출
		Board boardParam = boardMapper.reqToBoard(req);
		Long teamIdParam = req != null ? req.getTeamId() : null;
		Long gisuIdParam = req != null ? req.getGisuId() : null;
		String filePath = null;
		if(image != null) {
			filePath = multipartFileService.saveFile(image);
			if(boardParam != null)
				boardParam.setImage(filePath);
			else
				boardParam = Board.builder().image(filePath).build();
		}
		
		
		return CommonResponse.<BoardDto.Res>builder()
				.code("200")
				.message("ok")
				.data(boardService.updateBoard(boardId, boardParam, teamIdParam, gisuIdParam, 0l))
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
