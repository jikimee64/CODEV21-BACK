package com.j2kb.codev21.domains.vote.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    	
    	private LocalDate startDate;
    	
    	private LocalDate endDate;

		private List<Long> boardIds;
	}
	
	@Data
	@Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
	public static class Res{
		
		private long id;

    	private LocalDate startDate;
    	
    	private LocalDate endDate;

		private List<BoardVoteDto.Res> boardVotes;

		private LocalDateTime created_at;
		
		private LocalDateTime updated_at;
		
		public Res(Vote vote) {
			this.id = vote.getId();
			this.startDate = vote.getStartDate();
			this.endDate = vote.getEndDate();
			this.created_at = vote.getCreatedAt();
			this.updated_at = vote.getUpdatedAt();
			this.boardVotes = vote.getBoardVotes().stream()
							.map(boardVote -> new BoardVoteDto.Res(boardVote))
							.collect(Collectors.toList());
		}
	}
}
