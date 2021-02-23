package com.j2kb.codev21.domains.board.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.j2kb.codev21.domains.board.domain.Board;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BoardDto {
	
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
	public static class Req {
    	
    	private String gisu;
    	
    	private String title;

		private String content;

		private String summary;

		private long teamId;
	}
	
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
	public static class Res{
		
		private long id;

		private String gisu;
		
		private String title;

		private String content;

		private String summary;

		private Writer writerInfo;

		private TeamInfo teamInfo;

		private String image;

		private VoteInfo voteInfo;
		
		private LocalDateTime created_At;
		
		private LocalDateTime updated_At;
		
		public Res(Board board){
			this.id = board.getId();
//			this.gisu = board.getGisuCategory().getGisu();
			this.title = board.getTitle();
			this.content = board.getContent();
			this.summary = board.getSummary();
//			this.writerInfo
//			this.teamInfo
			this.image = board.getImage();
//			this.voteInfo
			this.created_At = board.getCreatedAt();
			this.updated_At = board.getUpdatedAt();
		}

	}
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Writer {

    	private long userId;
    	
    	private String userName;
    }
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class TeamInfo {
    	
    	private String teamName;
    	
    	private List<String> teamMembers = new ArrayList<>();
    }
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class VoteInfo {

    	private boolean voting;
    	
    	private Long boardVoteId;
    }

}
