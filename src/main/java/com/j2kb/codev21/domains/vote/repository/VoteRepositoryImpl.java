package com.j2kb.codev21.domains.vote.repository;

import static com.j2kb.codev21.domains.vote.domain.QVote.vote;

import java.time.LocalDate;
import java.util.List;

import com.j2kb.codev21.domains.vote.dto.VoteDto;
import com.j2kb.codev21.domains.vote.dto.VoteSearchCondition;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VoteRepositoryImpl implements VoteRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	
	@Override
	public List<VoteDto.Res> search(VoteSearchCondition condition) {
		return queryFactory
					.select(Projections.constructor(VoteDto.Res.class, vote))
					.from(vote)
					.where(isProcessing(condition.getProcessing()),
							startDateGoe(condition.getStartDateGoe()),
							startDateLoe(condition.getStartDateLoe()),
							endDateGoe(condition.getEndDateGoe()),
							endDateloe(condition.getEndDateLoe()))
					.fetch();
	}
	
	private BooleanExpression isProcessing(Boolean processing) {
		return processing != null ? 
					(processing ? vote.startDate.goe(LocalDate.now())
									.and(vote.endDate.loe(LocalDate.now()))
						: vote.startDate.loe(LocalDate.now())
							.and(vote.endDate.goe(LocalDate.now()))) 
					: null;
	}
	
	private BooleanExpression startDateGoe(LocalDate startDateGoe) {
		return startDateGoe != null ? vote.startDate.goe(startDateGoe) : null;
	}
	private BooleanExpression startDateLoe(LocalDate startDateLoe) {
		return startDateLoe != null ? vote.startDate.goe(startDateLoe) : null;
	}
	private BooleanExpression endDateGoe(LocalDate endDateGoe) {
		return endDateGoe != null ? vote.startDate.goe(endDateGoe) : null;
	}
	private BooleanExpression endDateloe(LocalDate endDateloe) {
		return endDateloe != null ? vote.startDate.goe(endDateloe) : null;
	}
}
