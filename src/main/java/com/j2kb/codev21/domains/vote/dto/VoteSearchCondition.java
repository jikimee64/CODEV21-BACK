package com.j2kb.codev21.domains.vote.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class VoteSearchCondition {
	// 진행 유무(processing), 시작 날짜(startDateGoe, startDateLoe), 끝 날짜(endDateGoe, endDateLoe)
	
	private Boolean processing;
	
	private LocalDate startDateGoe;

	private LocalDate startDateLoe;
	
	private LocalDate endDateGoe;
	
	private LocalDate endDateLoe;

}
