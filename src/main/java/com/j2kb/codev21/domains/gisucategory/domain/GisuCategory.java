package com.j2kb.codev21.domains.gisucategory.domain;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.j2kb.codev21.domains.board.domain.Board;
import com.j2kb.codev21.domains.team.domain.Team;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "GISU_CATEGORY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GisuCategory {
	
	@Id @GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "GISU")
	private String gisu;
	
	@OneToMany(mappedBy = "gisuCategory")
	private List<Board> boards = new ArrayList<>();
	
	@OneToMany(mappedBy = "gisuCategory")
	private List<Team> teams = new ArrayList<>();

	@Builder
	public GisuCategory(String gisu) {
		this.gisu = gisu;
	}
	
	public void updateGisuCategory(GisuCategory gisuCategory) {
		this.gisu = gisuCategory.getGisu();
	}
}
