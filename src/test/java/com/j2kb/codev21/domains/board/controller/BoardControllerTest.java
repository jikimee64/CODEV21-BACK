package com.j2kb.codev21.domains.board.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.j2kb.codev21.domains.board.dto.BoardDto;
import com.j2kb.codev21.domains.board.dto.BoardDto.Res;
import com.j2kb.codev21.domains.board.service.BoardService;

@WebMvcTest(BoardController.class)
@ExtendWith({ MockitoExtension.class, RestDocumentationExtension.class, SpringExtension.class })
public class BoardControllerTest {

	private MockMvc mockMvc;
	
	@MockBean BoardService boardService;
	
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.apply(documentationConfiguration(restDocumentation)
						.uris()
						.withScheme("http")
						.withHost("localhost.com")
						.withPort(8080))
				.build();
	}
	@DisplayName("기수 별 프로젝트 조회")
	@Test
	void find_all_boardListByGisu() throws Exception {
		//given
		
		List<Res> resList = Stream.iterate(BoardDto.Res.builder()
							.id(0)
							.content("content0")
							.title("title0")
							.gisu("1")
							.summary("summary0")
							.build()
							, res -> {
								long curId = res.getId() + 1;
								
								return BoardDto.Res.builder()
										.id(curId)
										.content("content" + curId)
										.title("title" + curId)
										.gisu(String.valueOf(1 + (curId % 2)))
										.summary("summary" + curId)
										.build();
							}).limit(5)
				.collect(Collectors.toList());
		
		when(boardService.getBoardList(anyString()))
			.thenReturn(resList);

		//when
		//then
        this.mockMvc
        		.perform(get("/api/v1/boards")
        				.param("gisu" , "1")
        				.accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("BoardController/getBoardList",
                		preprocessRequest(prettyPrint()),
                		preprocessResponse(prettyPrint())));

	}
}
