package com.j2kb.codev21.domains.vote.service.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

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
import com.j2kb.codev21.domains.vote.dto.mapper.VoteMapper;
import com.j2kb.codev21.domains.vote.service.VoteService;
import com.j2kb.codev21.global.error.ErrorCode;
import com.j2kb.codev21.global.error.exception.InvalidValueException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles({ "vote_test_init", "dev", "secret" })
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
public class VoteServiceExceptionTest {
	
	@Autowired
	VoteService voteService;
	
	@Autowired
	VoteMapper voteMapper;
	
	@Test
	void 투표_단건_조회_NO_VOTE_FOUND() {
		// given

		try {
			// when
			voteService.getVote(Long.MAX_VALUE);
			
			// then
			fail("InvalidValueException 발생하지 않음!");
		} catch (InvalidValueException e) {
			// then
			assertThat(e.getErrorCode()).isEqualByComparingTo(ErrorCode.NO_VOTE_FOUND);
		}
	}
	
	@Test
	void 유저_투표_USERBOARDVOTE_DUPLICATION() {
		// given
		Req param = VoteDto.Req.builder()
				.startDate(LocalDate.of(2021, 4, 25))
				.endDate(LocalDate.of(2021, 4, 25).plusDays(7))
				.boardIds(List.of(1l, 2l, 3l, 4l))
				.build();

		VoteDto.Res res = voteService.insertVote(voteMapper.reqToVote(param), param.getBoardIds());
		try {
			// when
			// 중복투표 발생
			voteService.insertVoteOfUser(1l, res.getBoardVotes().get(0).getBoardVoteId());
			voteService.insertVoteOfUser(1l, res.getBoardVotes().get(0).getBoardVoteId());
			
			// then
			fail("InvalidValueException 발생하지 않음!");
		} catch (InvalidValueException e) {
			// then
			assertThat(e.getErrorCode()).isEqualByComparingTo(ErrorCode.USERBOARDVOTE_DUPLICATION);
		}
	}
	
	@Test
	void 유저_투표_취소_NO_USERBOARDVOTE_FOUND() {
		// given
		Req param = VoteDto.Req.builder()
				.startDate(LocalDate.of(2021, 5, 25))
				.endDate(LocalDate.of(2021, 5, 25).plusDays(7))
				.boardIds(List.of(1l, 2l, 3l, 4l))
				.build();

		VoteDto.Res res = voteService.insertVote(voteMapper.reqToVote(param), param.getBoardIds());
		try {
			// when
			voteService.cancleVoteOfUser(1l, res.getBoardVotes().get(0).getBoardVoteId());
			// then
			fail("InvalidValueException 발생하지 않음!");
		} catch (InvalidValueException e) {
			// then
			assertThat(e.getErrorCode()).isEqualByComparingTo(ErrorCode.NO_USERBOARDVOTE_FOUND);
		}
	}
	
	@Test
	void 투표_등록_VOTE_DUPLICATION() {
		// given
		Req param = VoteDto.Req.builder()
				.startDate(LocalDate.of(2021, 6, 25))
				.endDate(LocalDate.of(2021, 6, 25).plusDays(7))
				.boardIds(List.of(1l, 2l, 3l, 4l))
				.build();

		try {
			// when
			voteService.insertVote(voteMapper.reqToVote(param), param.getBoardIds());
			voteService.insertVote(voteMapper.reqToVote(param), param.getBoardIds());
			// then
			fail("InvalidValueException 발생하지 않음!");
		} catch (InvalidValueException e) {
			// then
			assertThat(e.getErrorCode()).isEqualByComparingTo(ErrorCode.VOTE_DUPLICATION);
		}
	}
	
	@Test
	void 투표_투표게시글_추가_BOARDVOTE_DUPLICATION() {
		// given
		Req param = VoteDto.Req.builder()
				.startDate(LocalDate.of(2021, 7, 25))
				.endDate(LocalDate.of(2021, 7, 25).plusDays(7))
				.boardIds(List.of(1l, 2l, 3l, 4l))
				.build();

		VoteDto.Res res = voteService.insertVote(voteMapper.reqToVote(param), param.getBoardIds());
		try {
			// when
			voteService.includeBoardIntoVote(res.getId(), 1l);
			// then
			fail("InvalidValueException 발생하지 않음!");
		} catch (InvalidValueException e) {
			// then
			assertThat(e.getErrorCode()).isEqualByComparingTo(ErrorCode.BOARDVOTE_DUPLICATION);
		}
	}
}
