package com.j2kb.codev21.domains.team.domain;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Column;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.j2kb.codev21.domains.board.domain.Board;
import com.j2kb.codev21.domains.gisucategory.domain.GisuCategory;
import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.global.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TEAM")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Team extends BaseTimeEntity {
	
	@Id @GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "TEAM_NAME")
	private String teamName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GISU_CATEGORY_ID")
	private GisuCategory gisuCategory;
	
	@OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
	private List<UserTeam> userTeams = new ArrayList<>();

	@OneToMany(mappedBy = "team")
	private List<Board> boards = new ArrayList<>();
	
	public void addUserTeam(UserTeam userTeam) {
		this.userTeams.add(userTeam);
		userTeam.setTeam(this);
	}
	
	public void removeUserTeam(UserTeam userTeam) {
		this.userTeams.remove(userTeam);
	}
	
	public void changeGisuCategory(GisuCategory gisuCategory) {
		if(this.gisuCategory != null) {
			this.gisuCategory.getTeams().remove(this);
		}
		this.gisuCategory = gisuCategory;
		gisuCategory.getTeams().add(this);
	}
	
	public void addUserTeams(UserTeam userTeam) {
		this.userTeams.add(userTeam);
	}
	
	public void cleanUserTeams() {
		this.userTeams = new ArrayList<UserTeam>();
	}
}
