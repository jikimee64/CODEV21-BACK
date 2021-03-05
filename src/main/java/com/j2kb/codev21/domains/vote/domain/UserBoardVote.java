package com.j2kb.codev21.domains.vote.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.global.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER_BOARD_VOTE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserBoardVote extends BaseTimeEntity{
	
	@Id @GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VOTE_ID")
	private Vote vote;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BOARD_VOTE_ID")
	private BoardVote boardVote;

	@Builder
	public UserBoardVote(User user, Vote vote, BoardVote boardVote) {
		this.user = user;
		this.vote = vote;
		this.boardVote = boardVote;
	}
	
	public void setBoardVote(BoardVote boardVote) {
		this.boardVote = boardVote;
	}
	
	
}
