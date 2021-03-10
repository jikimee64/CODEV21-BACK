package com.j2kb.codev21.domains.team.domain;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.j2kb.codev21.domains.user.domain.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER_TEAM")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTeam {
	
	@Id @GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "LEADER")
	private Boolean leader;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEAM_ID")
	private Team team;
	
	@Builder
	public UserTeam(Boolean leader) {
		this.leader = leader;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	
}
