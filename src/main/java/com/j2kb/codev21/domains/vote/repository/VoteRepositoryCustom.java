package com.j2kb.codev21.domains.vote.repository;

import java.util.List;

import com.j2kb.codev21.domains.vote.domain.Vote;
import com.j2kb.codev21.domains.vote.dto.VoteSearchCondition;

public interface VoteRepositoryCustom {
	
	public List<Vote> search(VoteSearchCondition condition);
}
