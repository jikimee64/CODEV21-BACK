package com.j2kb.codev21.domains.vote.service.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.j2kb.codev21.domains.vote.dto.VoteDto;
import com.j2kb.codev21.domains.vote.dto.VoteDto.Req;
import com.j2kb.codev21.domains.vote.service.VoteService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles({ "vote_test_init", "dev", "secret" })
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
public class VoteServiceExceptionTest {
	
	@Autowired
	VoteService voteService;

	@Test
	void 투표_등록() {
		// given
		Req param = VoteDto.Req.builder()
				.startDate(LocalDate.now())
				.endDate(LocalDate.now().plusDays(7))
				.boardIds(List.of(1l, 2l, 3l, 4l)).build();

		// when
		Long insertedVoteId = voteService.insertVote(param);

		// then
		assertThat(insertedVoteId).isNotNull();

	}
}
