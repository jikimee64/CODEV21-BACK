package com.j2kb.codev21.domains.vote.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.j2kb.codev21.domains.board.domain.Board;
import com.j2kb.codev21.domains.board.dto.BoardDto;
import com.j2kb.codev21.domains.vote.domain.BoardVote;
import com.j2kb.codev21.domains.vote.domain.Vote;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class VoteDto {
	
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
	public static class Req {
    	
    	private LocalDateTime startDate;
    	
    	private LocalDateTime endDate;

		private List<Long> boardIds;

	}
	
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
	public static class Res{
		
		private long id;

    	private LocalDateTime startDate;
    	
    	private LocalDateTime endDate;

		private List<BoardVoteDto.Res> boardVotes;

		private LocalDateTime created_at;
		
		private LocalDateTime updated_at;
		
		public Res(Vote vote) {
			this.id = vote.getId();
			this.startDate = vote.getStartDate();
			this.endDate = vote.getEndDate();
			this.created_at = vote.getCreatedAt();
			this.updated_at = vote.getUpdatedAt();
		}
	}
}
