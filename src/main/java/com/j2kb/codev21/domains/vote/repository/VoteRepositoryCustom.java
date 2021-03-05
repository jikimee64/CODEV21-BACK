package com.j2kb.codev21.domains.vote.repository;

import java.util.List;

import com.j2kb.codev21.domains.vote.dto.VoteDto;
import com.j2kb.codev21.domains.vote.dto.VoteSearchCondition;

public interface VoteRepositoryCustom {
	
	public List<VoteDto.Res> search(VoteSearchCondition condition);
}
