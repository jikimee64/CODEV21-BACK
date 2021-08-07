package com.j2kb.codev21.domains.vote.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyParameters;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j2kb.codev21.domains.vote.domain.Vote;
import com.j2kb.codev21.domains.vote.dto.BoardVoteDto;
import com.j2kb.codev21.domains.vote.dto.BoardVoteDto.Res;
import com.j2kb.codev21.domains.vote.dto.VoteDto;
import com.j2kb.codev21.domains.vote.dto.VoteSearchCondition;
import com.j2kb.codev21.domains.vote.service.VoteService;
import com.sun.xml.bind.v2.schemagen.xmlschema.Any;

@SpringBootTest
@ExtendWith({ MockitoExtension.class, RestDocumentationExtension.class, SpringExtension.class })
class VoteControllerTest {
	
	private MockMvc mockMvc;
	
	@MockBean VoteService voteService;
	
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
	
	@Test
	@DisplayName("투표 조회")
	void find_VoteList() throws Exception {
		//given
		
		List<VoteDto.Res> resList = Stream.iterate(VoteDto.Res.builder()	
												.id(0l)
												.startDate(LocalDate.of(2021, Month.MARCH, 15))
												.endDate(LocalDate.of(2021, Month.MARCH, 22))
												.boardVotes(getDumyBoardVoteDtoList(0l, 3))
												.createdAt(LocalDateTime.of(2021, Month.MARCH, 15, 0, 0))
												.updatedAt(LocalDateTime.of(2021, Month.MARCH, 15, 0, 0))
												.build()
							, res -> {
								return VoteDto.Res.builder()
										.id(res.getId() + 1l)
										.startDate(res.getStartDate().plusMonths(1))
										.endDate(res.getEndDate().plusMonths(1))
										.boardVotes(getDumyBoardVoteDtoList(res.getId() + 1l, 3))
										.createdAt(res.getCreatedAt().plusMonths(1))
										.updatedAt(res.getUpdatedAt().plusMonths(1))
										.build();
							}).limit(2)
				.collect(Collectors.toList());
		
		when(voteService.getVoteList(any(VoteSearchCondition.class)))
			.thenReturn(resList);

		//when
		//then
        this.mockMvc
        		.perform(RestDocumentationRequestBuilders.get("/api/v1/admin/votes?processing=true&startDateGoe=2021-03-05&startDateLoe=2021-03-05&endDateGoe=2021-03-05&endDateLoe=2021-03-05")
        				.accept(MediaType.APPLICATION_JSON)
        				.header("Authorization", "Bear {token값}"))
                .andExpect(status().isOk())
                .andDo(document("VoteController/getVoteList",
                		preprocessRequest(prettyPrint()),
                		preprocessResponse(prettyPrint()),
                		requestHeaders(headerWithName("Authorization").description("Bear {token값}")),
                		requestParameters(parameterWithName("processing").description("진행중 유무"),
                					parameterWithName("startDateGoe").description("투표 시작 날짜 <="),
                					parameterWithName("startDateLoe").description("투표 시작 날짜 >="),
                					parameterWithName("endDateGoe").description("투표 종료 날짜 <="),
                					parameterWithName("endDateLoe").description("투표 종료 날짜 >=")),
                		responseFields(fieldWithPath("code").description("code(200,400...)"),
                					fieldWithPath("message").description("message(success...)"),
                					subsectionWithPath("data[]").description("Response 데이터"),
            						fieldWithPath("data[].id").description("투표의 id"),
            						fieldWithPath("data[].startDate").description("투표 시작 날짜"),
            						fieldWithPath("data[].endDate").description("투표 종료 날짜"),
            						subsectionWithPath("data[].boardVotes[]").description("투표에 속한 게시글 리스트"),
            						fieldWithPath("data[].boardVotes[].boardId").description("투표에 속한 게시글의 id"),
            						fieldWithPath("data[].boardVotes[].title").description("투표에 속한 게시글의 제목"),
            						fieldWithPath("data[].boardVotes[].count").description("투표에 속한 게시글의 득표수"),
            						fieldWithPath("data[].createdAt").description("투표가 생성된 날짜"),
            						fieldWithPath("data[].updatedAt").description("투표가 수정된 날짜"))));

	}

	private List<Res> getDumyBoardVoteDtoList(long voteId, int limit) {
		return Stream.iterate(BoardVoteDto.Res.builder()
										.boardVoteId(voteId * 3)
										.boardId(voteId * 3)
										.title("someBoardTitle" + (voteId * 3))
										.count(15 + (int) voteId)
										.build(), 
										boardVoteDtoRes -> {
												return BoardVoteDto.Res.builder()
																	.boardVoteId(boardVoteDtoRes.getBoardId() + 1)
																	.boardId(boardVoteDtoRes.getBoardId() + 1)
																	.title("someBoardTitle" + (boardVoteDtoRes.getBoardId() + 1))
																	.count(boardVoteDtoRes.getCount() + 2)
																	.build();
											})
					.limit(limit)
					.collect(Collectors.toList());
	}
	
	
	@Test
	@DisplayName("투표 단건 조회")
	void find_VoteOne() throws Exception {
		//given
		
		VoteDto.Res res = VoteDto.Res.builder()	
					.id(0l)
					.startDate(LocalDate.of(2021, Month.MARCH, 15))
					.endDate(LocalDate.of(2021, Month.MARCH, 22))
					.boardVotes(getDumyBoardVoteDtoList(0l, 3))
					.createdAt(LocalDateTime.of(2021, Month.MARCH, 15, 0, 0))
					.updatedAt(LocalDateTime.of(2021, Month.MARCH, 15, 0, 0))
					.build();
		
		when(voteService.getVote(anyLong()))
			.thenReturn(res);

		//when
		//then
        this.mockMvc
        		.perform(RestDocumentationRequestBuilders.get("/api/v1/admin/votes/{voteId}", 0l)
        				.accept(MediaType.APPLICATION_JSON)
        				.header("Authorization", "Bear {token값}"))
                .andExpect(status().isOk())
                .andDo(document("VoteController/getVote",
                		preprocessRequest(prettyPrint()),
                		preprocessResponse(prettyPrint()),
                		requestHeaders(headerWithName("Authorization").description("Bear {token값}")),
                		pathParameters(parameterWithName("voteId").description("조회할 투표 번호")),
                		responseFields(fieldWithPath("code").description("code(200,400...)"),
            					fieldWithPath("message").description("message(success...)"),
            					subsectionWithPath("data").description("Response 데이터"),
        						fieldWithPath("data.id").description("투표의 id"),
        						fieldWithPath("data.startDate").description("투표 시작 날짜"),
        						fieldWithPath("data.endDate").description("투표 종료 날짜"),
        						subsectionWithPath("data.boardVotes[]").description("투표에 속한 게시글 리스트"),
        						fieldWithPath("data.boardVotes[].boardId").description("투표에 속한 게시글의 id"),
        						fieldWithPath("data.boardVotes[].title").description("투표에 속한 게시글의 제목"),
        						fieldWithPath("data.boardVotes[].count").description("투표에 속한 게시글의 득표수"),
        						fieldWithPath("data.createdAt").description("투표가 생성된 날짜"),
        						fieldWithPath("data.updatedAt").description("투표가 수정된 날짜"))));

	}

	@DisplayName("유저 투표 요청")
    @Test
    void insertVoteOfUser() throws Exception {
    	//given
		when(voteService.insertVoteOfUser(anyLong(), anyLong()))
			.thenReturn(true);
    	//when
        //then
        this.mockMvc
        	.perform(RestDocumentationRequestBuilders.post("/api/v1/votes/boards/{boardVoteId}/members", 0l)
        			.accept(MediaType.APPLICATION_JSON)
        			.header("Authorization", "Bear {token값}"))
            .andExpect(status().isOk())
            .andDo(document("VoteController/insertVoteOfUser",
            		preprocessRequest(prettyPrint()),
            		preprocessResponse(prettyPrint()),
            		requestHeaders(headerWithName("Authorization").description("Bear {token값}")),
            		pathParameters(parameterWithName("boardVoteId").description("회원이 투표할 게시글의 게시글_투표 번호")),
            		responseFields(fieldWithPath("code").description("code(200,400...)"),
        					fieldWithPath("message").description("message(success...)"),
        					subsectionWithPath("data").description("Response 데이터"),
    						fieldWithPath("data.result").description("유저 투표 반영 결과"))));
    }
	
	@DisplayName("유저 투표 취소 요청")
    @Test
    void cancleVoteOfUser() throws Exception {
    	//given
		when(voteService.cancleVoteOfUser(anyLong(), anyLong()))
			.thenReturn(true);
    	//when
        //then
        this.mockMvc
        	.perform(RestDocumentationRequestBuilders.delete("/api/v1/votes/boards/{boardVoteId}/members", 0l)
        			.accept(MediaType.APPLICATION_JSON)
        			.header("Authorization", "Bear {token값}"))
            .andExpect(status().isOk())
            .andDo(document("VoteController/cancleVoteOfUser",
            		preprocessRequest(prettyPrint()),
            		preprocessResponse(prettyPrint()),
            		requestHeaders(headerWithName("Authorization").description("Bear {token값}")),
            		pathParameters(parameterWithName("boardVoteId").description("회원이 투표 취소할 게시글의 게시글_투표 번호")),
            		responseFields(fieldWithPath("code").description("code(200,400...)"),
        					fieldWithPath("message").description("message(success...)"),
        					subsectionWithPath("data").description("Response 데이터"),
    						fieldWithPath("data.result").description("유저 투표 취소 반영 결과"))));
    }
	
    @DisplayName("투표 등록")
    @Test
    void insertVote() throws Exception {
    	//given
    	 String content = objectMapper.writeValueAsString(VoteDto.Req.builder()
    			 												.startDate(LocalDate.of(2021, Month.MARCH, 15))
    			 												.endDate(LocalDate.of(2021, Month.MARCH, 22))
    			 												.boardIds(List.of(0l, 1l, 2l))
    			 												.build());
    	 
    	 when(voteService.insertVote(any(), any()))
 	 		.thenReturn(VoteDto.Res.builder()	
					.id(0l)
					.startDate(LocalDate.of(2021, Month.MARCH, 15))
					.endDate(LocalDate.of(2021, Month.MARCH, 22))
					.boardVotes(getDumyBoardVoteDtoList(0l, 3))
					.createdAt(LocalDateTime.of(2021, Month.MARCH, 15, 0, 0))
					.updatedAt(LocalDateTime.of(2021, Month.MARCH, 15, 0, 0))
					.build());
    	//when
        //then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/admin/votes")
        	.content(content)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
			.header("Authorization", "Bear {token값}"))
            .andExpect(status().isOk())
            .andDo(document("VoteController/insertVote",
            		preprocessRequest(prettyPrint()),
            		preprocessResponse(prettyPrint()),
            		requestHeaders(headerWithName("Authorization").description("Bear {token값}")),
            		requestFields(fieldWithPath("startDate").description("투표 시작 날짜"),
            				fieldWithPath("endDate").description("투표 종료 날짜"),
            				fieldWithPath("boardIds[]").description("투표에 등록할 게시글 id 배열")),
            		responseFields(fieldWithPath("code").description("code(200,400...)"),
        					fieldWithPath("message").description("message(success...)"),
        					subsectionWithPath("data").description("Response 데이터"),
    						fieldWithPath("data.id").description("등록된 투표의 id"),
    						fieldWithPath("data.startDate").description("등록된 투표 시작 날짜"),
    						fieldWithPath("data.endDate").description("등록된 투표 종료 날짜"),
    						subsectionWithPath("data.boardVotes[]").description("등록된 투표에 속한 게시글 리스트"),
    						fieldWithPath("data.boardVotes[].boardId").description("등록된 투표에 속한 게시글의 id"),
    						fieldWithPath("data.boardVotes[].title").description("등록된 투표에 속한 게시글의 제목"),
    						fieldWithPath("data.boardVotes[].count").description("등록된 투표에 속한 게시글의 득표수"),
    						fieldWithPath("data.createdAt").description("등록된 투표가 생성된 날짜"),
    						fieldWithPath("data.updatedAt").description("등록된 투표가 수정된 날짜"))));
    }	

    @DisplayName("투표 수정")
    @Test
    void updateVote() throws Exception {
    	//given
    	Map<String, LocalDateTime> reqMap = new HashMap<>();
    	reqMap.put("startDate", LocalDateTime.of(2021, Month.MARCH, 15, 0, 0));
    	reqMap.put("endDate", LocalDateTime.of(2021, Month.MARCH, 22, 0, 0));
    	
    	 String content = objectMapper.writeValueAsString(reqMap);
    	 
    	 when(voteService.updateVote(anyLong(), any(Vote.class)))
    	 	.thenReturn(VoteDto.Res.builder()	
					.id(0l)
					.startDate(LocalDate.of(2021, Month.MARCH, 15))
					.endDate(LocalDate.of(2021, Month.MARCH, 22))
					.boardVotes(getDumyBoardVoteDtoList(0l, 3))
					.createdAt(LocalDateTime.of(2021, Month.MARCH, 10, 0, 0))
					.updatedAt(LocalDateTime.of(2021, Month.MARCH, 15, 0, 0))
					.build());
    	 
    	//when
        //then
        this.mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/admin/votes/{voteId}", 0l)
        	.content(content)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
			.header("Authorization", "Bear {token값}"))
            .andExpect(status().isOk())
            .andDo(document("VoteController/updateVote",
            		preprocessRequest(prettyPrint()),
            		preprocessResponse(prettyPrint()),
            		requestHeaders(headerWithName("Authorization").description("Bear {token값}")),
            		pathParameters(parameterWithName("voteId").description("수정할 투표 번호")),
            		requestFields(fieldWithPath("startDate").description("수정할 투표 시작 날짜"),
            				fieldWithPath("endDate").description("수정할투표 종료 날짜")),
            		responseFields(fieldWithPath("code").description("code(200,400...)"),
        					fieldWithPath("message").description("message(success...)"),
        					subsectionWithPath("data").description("Response 데이터"),
    						fieldWithPath("data.id").description("수정된 투표의 id"),
    						fieldWithPath("data.startDate").description("수정된 투표 시작 날짜"),
    						fieldWithPath("data.endDate").description("수정된 투표 종료 날짜"),
    						subsectionWithPath("data.boardVotes[]").description("수정된 투표에 속한 게시글 리스트"),
    						fieldWithPath("data.boardVotes[].boardId").description("수정된 투표에 속한 게시글의 id"),
    						fieldWithPath("data.boardVotes[].title").description("수정된 투표에 속한 게시글의 제목"),
    						fieldWithPath("data.boardVotes[].count").description("수정된 투표에 속한 게시글의 득표수"),
    						fieldWithPath("data.createdAt").description("수정된 투표가 생성된 날짜"),
    						fieldWithPath("data.updatedAt").description("수정된 투표가 수정된 날짜"))));
    }	
    
	@DisplayName("투표 삭제")
    @Test
    void deleteVote() throws Exception {
    	//given
		when(voteService.deleteVote(anyLong()))
			.thenReturn(true);
    	//when
        //then
        this.mockMvc
        	.perform(RestDocumentationRequestBuilders.delete("/api/v1/admin/votes/{voteId}", 0l)
        			.accept(MediaType.APPLICATION_JSON)
        			.header("Authorization", "Bear {token값}"))
            .andExpect(status().isOk())
            .andDo(document("VoteController/deleteVote",
            		preprocessRequest(prettyPrint()),
            		preprocessResponse(prettyPrint()),
            		requestHeaders(headerWithName("Authorization").description("Bear {token값}")),
            		pathParameters(parameterWithName("voteId").description("삭제할 투표 번호")),
            		responseFields(fieldWithPath("code").description("code(200,400...)"),
        					fieldWithPath("message").description("message(success...)"),
        					subsectionWithPath("data").description("Response 데이터"),
    						fieldWithPath("data.result").description("삭제 반영 결과"))));
    }
    
	@DisplayName("투표 게시글 추가(등록)")
    @Test
    void includeBoardListIntoVote() throws Exception {
    	//given
   	 	String content = objectMapper.writeValueAsString(BoardVoteDto.Req.builder()
   	 														.boardIds(List.of(4l, 5l))
   	 														.build());
   	 	
		when(voteService.includeBoardListIntoVote(anyLong(), any()))
				.thenReturn(getDumyBoardVoteDtoList(0l, 5));
   	 	//when
        //then
        this.mockMvc
        	.perform(RestDocumentationRequestBuilders.post("/api/v1/admin/votes/{voteId}/boards", 0l)
        			.content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
        			.header("Authorization", "Bear {token값}"))
            .andExpect(status().isOk())
            .andDo(document("VoteController/includeBoardListIntoVote",
            		preprocessRequest(prettyPrint()),
            		preprocessResponse(prettyPrint()),
            		requestHeaders(headerWithName("Authorization").description("Bear {token값}")),
            		pathParameters(parameterWithName("voteId").description("게시글들을 포함시킬 투표 번호")),
            		requestFields(fieldWithPath("boardIds").description("투표에 포함시킬 게시글 ID List")),
            		responseFields(fieldWithPath("code").description("code(200,400...)"),
        					fieldWithPath("message").description("message(success...)"),
        					subsectionWithPath("data[]").description("Response 데이터"),
       						fieldWithPath("data[].boardId").description("투표 게시글의 게시글 ID"),
    						fieldWithPath("data[].boardVoteId").description("투표 게시글의 ID"),
    						fieldWithPath("data[].title").description("투표 게시글의 게시글 제목"),
    						fieldWithPath("data[].count").description("투표 게시글의 득표수"))));
    }
	
	@DisplayName("투표 게시글 단건 추가(등록)")
    @Test
    void includeBoardIntoVote() throws Exception {
    	//given
		
		when(voteService.includeBoardIntoVote(anyLong(), anyLong()))
				.thenReturn(getDumyBoardVoteDtoList(0l, 3));
		
    	//when
        //then
        this.mockMvc
        	.perform(RestDocumentationRequestBuilders.post("/api/v1/admin/votes/{voteId}/boards/{boardId}", 0l, 0l)
                    .accept(MediaType.APPLICATION_JSON)
        			.header("Authorization", "Bear {token값}"))
            .andExpect(status().isOk())
            .andDo(document("VoteController/includeBoardIntoVote",
            		preprocessRequest(prettyPrint()),
            		preprocessResponse(prettyPrint()),
            		requestHeaders(headerWithName("Authorization").description("Bear {token값}")),
            		pathParameters(parameterWithName("voteId").description("게시글을 포함시킬 투표 번호"),
            				parameterWithName("boardId").description("투표에 포함시킬 게시글 번호")),
            		responseFields(fieldWithPath("code").description("code(200,400...)"),
        					fieldWithPath("message").description("message(success...)"),
        					subsectionWithPath("data[]").description("Response 데이터"),
    						fieldWithPath("data[].boardId").description("게시글을 등록한 투표에 속한 게시글의 id"),
    						fieldWithPath("data[].title").description("게시글을 등록한 속한 게시글의 제목"),
    						fieldWithPath("data[].count").description("게시글을 등록한 속한 게시글의 득표수"))));
    }

	@DisplayName("투표 게시글 제외(삭제)")
    @Test
    void excludeBoardListInVote() throws Exception {
    	//given
   	 	String content = objectMapper.writeValueAsString(BoardVoteDto.Req.builder()
   	 														.boardIds(List.of(3l, 4l))
   	 														.build());
		List<Res> dumyBoardVoteDtoList = getDumyBoardVoteDtoList(0, 3);
		dumyBoardVoteDtoList.removeIf(dto -> dto.getBoardId() == 0l);
		when(voteService.excludeBoardListInVote(anyLong(), any()))
				.thenReturn(dumyBoardVoteDtoList);
		
    	//when
        //then
        this.mockMvc
        	.perform(RestDocumentationRequestBuilders.delete("/api/v1/admin/votes/{voteId}/boards", 0l)
        			.content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
        			.header("Authorization", "Bear {token값}"))
            .andExpect(status().isOk())
            .andDo(document("VoteController/excludeBoardListInVote",
            		preprocessRequest(prettyPrint()),
            		preprocessResponse(prettyPrint()),
            		requestHeaders(headerWithName("Authorization").description("Bear {token값}")),
            		pathParameters(parameterWithName("voteId").description("게시글들이 제외될 투표 번호")),
            		requestFields(fieldWithPath("boardIds").description("투표에 제외시킬 게시글 ID List")),
            		responseFields(fieldWithPath("code").description("code(200,400...)"),
        					fieldWithPath("message").description("message(success...)"),
        					subsectionWithPath("data").description("Response 데이터"),
       						fieldWithPath("data[].boardId").description("투표 게시글의 게시글 ID"),
    						fieldWithPath("data[].boardVoteId").description("투표 게시글의 ID"),
    						fieldWithPath("data[].title").description("투표 게시글의 게시글 제목"),
    						fieldWithPath("data[].count").description("투표 게시글의 득표수"))));
    }
	
	@DisplayName("투표 게시글 단건 제외(삭제)")
    @Test
    void excludeBoardInVote() throws Exception {
    	//given
		List<Res> dumyBoardVoteDtoList = getDumyBoardVoteDtoList(0, 3);
		dumyBoardVoteDtoList.removeIf(dto -> dto.getBoardId() == 0l);
		when(voteService.excludeBoardInVote(anyLong(), anyLong()))
				.thenReturn(dumyBoardVoteDtoList);
    	//when
        //then
        this.mockMvc
        	.perform(RestDocumentationRequestBuilders.delete("/api/v1/admin/votes/{voteId}/boards/{boardId}", 0l, 0l)
                    .accept(MediaType.APPLICATION_JSON)
        			.header("Authorization", "Bear {token값}"))
            .andExpect(status().isOk())
            .andDo(document("VoteController/excludeBoardInVote",
            		preprocessRequest(prettyPrint()),
            		preprocessResponse(prettyPrint()),
            		requestHeaders(headerWithName("Authorization").description("Bear {token값}")),
            		pathParameters(parameterWithName("voteId").description("게시글이 제외될 투표 번호"),
            				parameterWithName("boardId").description("투표에서 제외될 게시글 번호")),
            		responseFields(fieldWithPath("code").description("code(200,400...)"),
        					fieldWithPath("message").description("message(success...)"),
        					subsectionWithPath("data[]").description("Response 데이터"),
    						fieldWithPath("data[].boardId").description("투표 게시글의 게시글 ID"),
    						fieldWithPath("data[].boardVoteId").description("투표 게시글의 ID"),
    						fieldWithPath("data[].title").description("투표 게시글의 게시글 제목")
    						,fieldWithPath("data[].count").description("투표 게시글의 득표수"))));
    }
	
	

}
