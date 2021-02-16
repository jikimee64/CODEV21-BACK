package com.j2kb.codev21.domains.board.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

		private TeamInfo teamInfo;

		private String image;

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

		private Writer writer;

		private TeamInfo teamInfo;

		private String image;

		private boolean isVoting;
		
		private LocalDateTime create_at;
		
		private LocalDateTime updated_at;

	}
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Writer {

    	private String id;
    	
    	private String name;
    }
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class TeamInfo {
    	
    	private String teamName;
    	
    	private List<String> teamMembers = new ArrayList<>();
    }

}
