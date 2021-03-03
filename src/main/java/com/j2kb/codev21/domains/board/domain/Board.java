package com.j2kb.codev21.domains.board.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.domains.vote.domain.BoardVote;
import com.j2kb.codev21.global.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BOARD")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Board extends BaseTimeEntity {

	@Id @GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "CONTENT")
	private String content;
	
	@Column(name = "SUMMARY")
	private String summary;
	
	@Column(name = "WRITER")
	private String writer;
	
	@Column(name = "IMAGE")
	private String image;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;
	
	@OneToMany(mappedBy = "board")
	private List<BoardVote> boardVotes = new ArrayList<>();
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "TEAM_ID")
//	private Team team;
	

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "GISU_CATEGORY_ID")
//	private GisuCategory gisuCategory;
	
	public Board(Long id) {
		this.id = id;
	}

	public Board(String title, String content, String summary, String writer, String image, User user) {
		this.title = title;
		this.content = content;
		this.summary = summary;
		this.writer = writer;
		this.image = image;
		this.user = user;
	}
	
	
}
