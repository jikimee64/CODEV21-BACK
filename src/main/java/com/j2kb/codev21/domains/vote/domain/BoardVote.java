package com.j2kb.codev21.domains.vote.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.j2kb.codev21.domains.board.domain.Board;
import com.j2kb.codev21.global.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BOARD_VOTE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardVote extends BaseTimeEntity{

	@Id @GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "COUNT")
	private int count;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VOTE_ID")
	private Vote vote;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BOARD_ID")
	private Board board;
	
	@OneToMany(mappedBy = "boardVote", cascade = CascadeType.ALL)
	private List<UserBoardVote> userBoardVotes = new ArrayList<>();

	@Builder
	public BoardVote(int count, Vote vote, Board board) {
		super();
		this.count = count;
		this.vote = vote;
		this.board = board;
	}
	
	public void increaseCount() {
		this.count += 1;
	}
	
	public void decreaseCount() {
		this.count = count > 0 ? count - 1 : 0;
	}
	
	public void setVote(Vote vote) {
		this.vote = vote;
	}
	
	public void addUserBoardVote(UserBoardVote userBoardVote) {
		this.userBoardVotes.add(userBoardVote);
		userBoardVote.setBoardVote(this);
	}
	
	public void removeUserBoardVote(UserBoardVote userBoardVote) {
		this.userBoardVotes.remove(userBoardVote);
		userBoardVote.setBoardVote(null);
	}
	
	
}
