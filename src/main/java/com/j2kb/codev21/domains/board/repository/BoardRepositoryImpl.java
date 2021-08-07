package com.j2kb.codev21.domains.board.repository;

import static com.j2kb.codev21.domains.board.domain.QBoard.board;
import static com.j2kb.codev21.domains.gisucategory.domain.QGisuCategory.gisuCategory;
import static com.j2kb.codev21.domains.team.domain.QTeam.team;

import java.util.List;

import com.j2kb.codev21.domains.board.domain.Board;
import com.j2kb.codev21.domains.board.dto.BoardSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;
	
	@Override
	public List<Board> search(BoardSearchCondition condition) {
		
		return queryFactory
				.select(board)
				.from(board)
				.leftJoin(board.team, team)
				.leftJoin(board.gisuCategory, gisuCategory)
				.where(gisuIdEq(condition.getGisuId()))
				.fetch();
	}
	
	private BooleanExpression gisuIdEq(Long gisuId) {
		return gisuId != null ? board.gisuCategory.id.eq(gisuId) : null;
	}

}
