package com.j2kb.codev21.domains.board.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j2kb.codev21.domains.board.dto.BoardDto;
import com.j2kb.codev21.domains.board.dto.BoardDto.Req;
import com.j2kb.codev21.domains.board.dto.BoardDto.Res;
import com.j2kb.codev21.domains.board.service.BoardService;
import com.j2kb.codev21.util.MultiValueMapConverter;

@WebMvcTest(BoardController.class)
@ExtendWith({ MockitoExtension.class, RestDocumentationExtension.class, SpringExtension.class })
public class BoardControllerTest {

	private MockMvc mockMvc;
	
	@MockBean BoardService boardService;
	
    @Autowired private ObjectMapper objectMapper;
    
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
	void find_all_boardList_ByGisu() throws Exception {
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
        		.perform(RestDocumentationRequestBuilders.get("/api/v1/boards")
        				.param("gisu" , "value")
        				.accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("BoardController/getBoardList",
                		preprocessRequest(prettyPrint()),
                		preprocessResponse(prettyPrint()),
                		requestParameters(parameterWithName("gisu").description("검색 조건에 사용할 기수 정보"))));

	}
	
	@DisplayName("프로젝트 단건 조회")
	@Test
	void find_board_ById() throws Exception {
		//given
		when(boardService.getBoard(anyLong()))
			.thenReturn(BoardDto.Res.builder()
					.id(1L)
					.content("content")
					.title("title")
					.gisu("1")
					.summary("summary")
					.build());
		
		
		//when
		//then
        this.mockMvc
        		.perform(RestDocumentationRequestBuilders.get("/api/v1/boards/{id}", 1l)
        				.accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("BoardController/getBoard",
                		preprocessRequest(prettyPrint()),
                		preprocessResponse(prettyPrint()),
                		pathParameters(parameterWithName("id").description("조회할 프로젝트 게시글 번호"))));

	}
	
    @DisplayName("프로젝트 등록")
    @Test
    void insert_Board() throws Exception {
    	//given
    	MockMultipartFile image = new MockMultipartFile("image", "filename-1.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
    	MultiValueMap<String, String> params = MultiValueMapConverter.convert(objectMapper, getMockBoardReq());
    	 params.remove("image");
    	 
    	 String content = objectMapper.writeValueAsString(getMockBoardReq());
    	 
    	 MockMultipartFile json = new MockMultipartFile("meta-data", "meta-data", "application/json", content.getBytes(StandardCharsets.UTF_8));
    	//when
        //then
        this.mockMvc.perform(multipart("/api/v1/boards")
        	.file(image)
        	.params(params)
            .contentType(MediaType.MULTIPART_MIXED)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8"))
            .andExpect(status().isOk())
            .andDo(document("BoardController/insertBoard",
            		preprocessRequest(prettyPrint()),
            		preprocessResponse(prettyPrint()),
					requestParts(partWithName("image").description("등록할 이미지"))));
    }

    
    @DisplayName("프로젝트 수정")
    @Test
    void update_Board() throws Exception {
    	//given
    	MockMultipartFile image = new MockMultipartFile("image", "filename-1.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
    	MultiValueMap<String, String> params = MultiValueMapConverter.convert(objectMapper, getMockBoardReq());
        params.remove("image");
    	
        //when
        //then
        
        // multipart() 혹은 fileUpload()의 HTTP 메소드는 POST로 하드코딩 되어 있음, 하여 with()을 통해 PUT으로 메소드를 수정해준다.
        MockMultipartHttpServletRequestBuilder fileUpload = RestDocumentationRequestBuilders.fileUpload("/api/v1/boards/{id}", 1l);
        fileUpload.with(request -> {
        	request.setMethod("PUT");
        	return request;
        });
        
        this.mockMvc.perform(fileUpload
        	.file(image)
        	.params(params)
            .contentType(MediaType.MULTIPART_MIXED)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8"))
            .andExpect(status().isOk())
            .andDo(document("BoardController/updateBoard",
            		preprocessRequest(prettyPrint()),
            		preprocessResponse(prettyPrint()),
            		pathParameters(parameterWithName("id").description("수정할 프로젝트 게시글 번호")),
					requestParts(partWithName("image").description("수정할 이미지"))));
    }
    
    @DisplayName("프로젝트 삭제")
    @Test
    void delete_Board() throws Exception {
    	//given
    	when(boardService.deleteBoard(anyLong()))
    			.thenReturn(true);
    	
        //when
        //then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/boards/{id}", 1l)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("BoardController/deleteBoard",
            		preprocessRequest(prettyPrint()),
            		preprocessResponse(prettyPrint()),
            		pathParameters(parameterWithName("id").description("삭제할 프로젝트 게시글 번호"))));
    }
    

	private Req getMockBoardReq() {
		return BoardDto.Req.builder()
			.gisu("someGisu")
			.title("someTitle")
			.content("someContent")
			.summary("someSummary")
			.teamId(1l)
			.build();
	}
}
