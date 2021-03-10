package com.j2kb.codev21.domains.board.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.j2kb.codev21.domains.board.dto.BoardDto.Res;
import com.j2kb.codev21.domains.board.dto.BoardSearchCondition;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles({ "board_test_init", "dev", "secret" })
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
public class BoardServiceTest {

	@Autowired
	private BoardService boardService;
	
	@Test
	public void 게시판_전체조회() {
		
		BoardSearchCondition condition = new BoardSearchCondition();
		condition.setGisuId(null);
		List<Res> result = boardService.getBoardList(condition);
		
		
	}
	
	
	
}
