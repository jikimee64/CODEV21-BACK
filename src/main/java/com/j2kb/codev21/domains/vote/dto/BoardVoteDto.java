package com.j2kb.codev21.domains.vote.dto;

import java.util.List;

import com.j2kb.codev21.domains.vote.domain.BoardVote;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BoardVoteDto {
	
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
	public static class Req {
    	
    	private List<Long> boardIds;
	}
   
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
	public static class Res{
    	
		private long boardId;
		
    	private long boardVoteId;
    	
		private String title;
    	
		private int count;
	}
}
