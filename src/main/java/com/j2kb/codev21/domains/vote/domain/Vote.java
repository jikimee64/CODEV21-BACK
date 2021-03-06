package com.j2kb.codev21.domains.vote.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.j2kb.codev21.domains.vote.dto.VoteDto;
import com.j2kb.codev21.global.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "VOTE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Vote extends BaseTimeEntity {
	
	@Id @GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "START_DATE")
	private LocalDate startDate;
	
	@Column(name = "END_DATE")
	private LocalDate endDate;
	
	@OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
	private List<BoardVote> boardVotes = new ArrayList<>();
	
	@OneToMany(mappedBy = "vote")
	private List<UserBoardVote> userBoardVotes = new ArrayList<>();

	@Builder
	public Vote(LocalDate startDate, LocalDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public void updateVote(Vote vote) {
		this.startDate = vote.getStartDate() != null ? vote.getStartDate() : this.startDate;
		this.endDate = vote.getEndDate() != null ? vote.getEndDate() : this.endDate;
	}
	
	public void addBoardVote(BoardVote boardVote) {
		boardVotes.add(boardVote);
		boardVote.setVote(this);
	}
}
