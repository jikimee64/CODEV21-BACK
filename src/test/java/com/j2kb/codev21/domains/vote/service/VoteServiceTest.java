package com.j2kb.codev21.domains.vote.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.j2kb.codev21.domains.vote.dto.BoardVoteDto;
import com.j2kb.codev21.domains.vote.dto.VoteDto;
import com.j2kb.codev21.domains.vote.dto.VoteDto.Req;
import com.j2kb.codev21.domains.vote.dto.VoteDto.Res;
import com.j2kb.codev21.domains.vote.dto.VoteSearchCondition;
import com.j2kb.codev21.domains.vote.dto.mapper.VoteMapper;
import com.j2kb.codev21.global.error.exception.InvalidValueException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles({ "vote_test_init", "dev", "secret" })
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
class VoteServiceTest {

	@Autowired
	VoteService voteService;

	@Autowired
	VoteMapper voteMapper;
	
	@Test
	void 투표_등록() {
		// given
		Req param = VoteDto.Req.builder()
				.startDate(LocalDate.of(2021, 3, 25))
				.endDate(LocalDate.of(2021, 3, 25).plusDays(7))
				.boardIds(List.of(1l, 2l, 3l, 4l)).build();

		// when
		Res response = voteService.insertVote(voteMapper.reqToVote(param), param.getBoardIds());

		// then
		assertThat(response.getStartDate()).isEqualTo(param.getStartDate());
		assertThat(response.getEndDate()).isEqualTo(param.getEndDate());
		assertThat(response.getBoardVotes().stream()
						.map(BoardVoteDto.Res::getBoardId)
						.collect(Collectors.toList()))
			.containsAll(param.getBoardIds());
	}
	
	@Test
	void 투표_조회() {
		// given
		Req param = VoteDto.Req.builder()
				.startDate(LocalDate.of(2021, 4, 25))
				.endDate(LocalDate.of(2021, 4, 25).plusDays(7))
				.boardIds(List.of(1l, 2l, 3l, 4l))
				.build();

		VoteDto.Res res = voteService.insertVote(voteMapper.reqToVote(param), param.getBoardIds());

		// when
		Res result = voteService.getVote(res.getId());

		// then
		log.info("result = " + result);
		assertThat(result.getStartDate()).isEqualTo(param.getStartDate());
		assertThat(result.getEndDate()).isEqualTo(param.getEndDate());
		assertThat(result.getBoardVotes().stream()
						.map(boardVote -> boardVote.getBoardId())
						.collect(Collectors.toList()))
				.containsAll(param.getBoardIds());
	}
	
	@Test
	void 전체_투표_조회() {
		// given
		Req param = VoteDto.Req.builder()
				.startDate(LocalDate.of(2021, 5, 25))
				.endDate(LocalDate.of(2021, 5, 25).plusDays(7))
				.boardIds(List.of(1l, 2l, 3l, 4l))
				.build();
		Req param2 = VoteDto.Req.builder()
				.startDate(LocalDate.of(2021, 6, 1))
				.endDate(LocalDate.of(2021, 6, 1).plusDays(7))
				.boardIds(List.of(5l, 6l, 7l, 8l))
				.build();

		VoteDto.Res res1 = voteService.insertVote(voteMapper.reqToVote(param), param.getBoardIds());
		VoteDto.Res res2 = voteService.insertVote(voteMapper.reqToVote(param2), param2.getBoardIds());

		// when
		 List<Res> result = voteService.getVoteList(new VoteSearchCondition());

		// then
		assertThat(result.stream()
							.mapToLong(VoteDto.Res::getId)
							.toArray())
				.contains(res1.getId(), res2.getId());
	}
	
	@Test
	void 투표_수정() {
		// given
		Req insertParam = VoteDto.Req.builder()
				.startDate(LocalDate.of(2021, 6, 25))
				.endDate(LocalDate.of(2021, 6, 25).plusDays(7))
				.boardIds(List.of(1l, 2l, 3l, 4l))
				.build();
		
		Req updateParam = VoteDto.Req.builder()
				.startDate(LocalDate.of(2021, 6, 27))
				.endDate(LocalDate.of(2021, 6, 27).plusDays(7))
				.build();


		VoteDto.Res res = voteService.insertVote(voteMapper.reqToVote(insertParam), insertParam.getBoardIds());

		// when
		Res result = voteService.updateVote(res.getId(), voteMapper.reqToVote(updateParam));

		// then
		assertThat(result.getStartDate()).isEqualTo(updateParam.getStartDate());
		assertThat(result.getEndDate()).isEqualTo(updateParam.getEndDate());
	}
	
	@Test
	void 투표_삭제() {
		// given
		Req insertParam = VoteDto.Req.builder()
				.startDate(LocalDate.of(2021, 7, 25))
				.endDate(LocalDate.of(2021, 7, 25).plusDays(7))
				.boardIds(List.of(1l, 2l, 3l, 4l))
				.build();


		VoteDto.Res res = voteService.insertVote(voteMapper.reqToVote(insertParam), insertParam.getBoardIds());
		
		// when
		Boolean result = voteService.deleteVote(res.getId());

		// then
		assertThat(result).isTrue();
		assertThrows(InvalidValueException.class,
					() -> voteService.getVote(res.getId()));
		
	}
	
	@Test
	void 투표_게시글_단건_등록() {
		// given
		Req insertParam = VoteDto.Req.builder()
				.startDate(LocalDate.of(2021, 8, 25))
				.endDate(LocalDate.of(2021, 8, 25).plusDays(7))
				.boardIds(List.of(1l, 2l, 3l))
				.build();


		VoteDto.Res res = voteService.insertVote(voteMapper.reqToVote(insertParam), insertParam.getBoardIds());

		// when
		List<BoardVoteDto.Res> result = voteService.includeBoardIntoVote(res.getId(), 4l);
		
		// then
		assertThat(result.stream()
						.mapToLong(BoardVoteDto.Res::getBoardId)
						.toArray())
			.contains(1l, 2l, 3l, 4l);

	}
	
	@Test
	void 투표_게시글_다건_등록() {
		// given
		Req insertParam = VoteDto.Req.builder()
				.startDate(LocalDate.of(2021, 9, 25))
				.endDate(LocalDate.of(2021, 9, 25).plusDays(7))
				.boardIds(List.of(1l, 2l))
				.build();


		VoteDto.Res res = voteService.insertVote(voteMapper.reqToVote(insertParam), insertParam.getBoardIds());
		BoardVoteDto.Req includeParam = BoardVoteDto.Req.builder()
				.boardIds(List.of(3l, 4l))
				.build();
		// when
		List<BoardVoteDto.Res> result = voteService.includeBoardListIntoVote(res.getId(), includeParam.getBoardIds());
		
		// then
		assertThat(result.stream()
						.mapToLong(BoardVoteDto.Res::getBoardId)
						.toArray())
			.contains(1l, 2l, 3l, 4l);

	}

	@Test
	void 투표_게시글_단건_제외() {
		// given
		Req insertParam = VoteDto.Req.builder()
				.startDate(LocalDate.of(2021, 10, 25))
				.endDate(LocalDate.of(2021, 10, 25).plusDays(7))
				.boardIds(List.of(1l, 2l, 3l))
				.build();


		VoteDto.Res res = voteService.insertVote(voteMapper.reqToVote(insertParam), insertParam.getBoardIds());

		// when
		List<BoardVoteDto.Res> result = voteService.excludeBoardInVote(res.getId(), 1l);
		
		// then
		assertThat(result.stream()
						.mapToLong(boardVote -> boardVote.getBoardId())
						.toArray())
			.containsExactlyInAnyOrder(insertParam.getBoardIds().stream()
										.filter(boardId -> boardId != 1l)
										.mapToLong(Long::longValue)
										.toArray());


	}
	
	@Test
	void 투표_게시글_다건_제외() {
		// given
		Req insertParam = VoteDto.Req.builder()
				.startDate(LocalDate.of(2021, 11, 25))
				.endDate(LocalDate.of(2021, 11, 25).plusDays(7))
				.boardIds(List.of(1l, 2l, 3l))
				.build();


		VoteDto.Res res = voteService.insertVote(voteMapper.reqToVote(insertParam), insertParam.getBoardIds());
		BoardVoteDto.Req excludeParam = BoardVoteDto.Req.builder()
				.boardIds(List.of(1l, 2l))
				.build();
		
		// when
		List<BoardVoteDto.Res> result = voteService.excludeBoardListInVote(res.getId(), excludeParam.getBoardIds());
		
		
		// then
		assertThat(result.stream()
				.mapToLong(boardVote -> boardVote.getBoardId())
				.toArray())
			.containsExactlyInAnyOrder(insertParam.getBoardIds().stream()
										.filter(boardId -> !excludeParam.getBoardIds().contains(boardId))
										.mapToLong(Long::longValue)
										.toArray());

	}
	
	@Test
	void 사용자_투표_요청() {
		// given
		Req insertParam = VoteDto.Req.builder()
				.startDate(LocalDate.of(2021, 12, 25))
				.endDate(LocalDate.of(2021, 12, 25).plusDays(7))
				.boardIds(List.of(1l, 2l, 3l))
				.build();


		VoteDto.Res res = voteService.insertVote(voteMapper.reqToVote(insertParam), insertParam.getBoardIds());

		// when
		Res beforeVote = voteService.getVote(res.getId());
		Boolean result = voteService.insertVoteOfUser(1l,
					beforeVote.getBoardVotes().get(0).getBoardVoteId());
		
		Res afterVote = voteService.getVote(res.getId());
		BoardVoteDto.Res boardVote = afterVote.getBoardVotes().stream()
				.filter(bv -> bv.getBoardVoteId() == beforeVote.getBoardVotes().get(0).getBoardVoteId())
				.findFirst().orElseThrow();
		// then
		assertThat(result).isTrue();
		assertThat(boardVote.getCount()).isEqualTo(1);
		// 사용자 투표 중복 시 발생하는 InvalidValueException으로 데이터가 들어갔는 지 검증
		assertThrows(InvalidValueException.class, 
				() -> voteService.insertVoteOfUser(1l,
						beforeVote.getBoardVotes().get(0).getBoardVoteId()));
	}
	
	@Test
	void 사용자_투표_취소_요청() {
		// given
		Req insertParam = VoteDto.Req.builder()
				.startDate(LocalDate.of(2022, 1, 25))
				.endDate(LocalDate.of(2022, 1, 25).plusDays(7))
				.boardIds(List.of(1l, 2l, 3l))
				.build();


		VoteDto.Res res = voteService.insertVote(voteMapper.reqToVote(insertParam), insertParam.getBoardIds());
		Res beforeVote = voteService.getVote(res.getId());
		voteService.insertVoteOfUser(1l,
					beforeVote.getBoardVotes().get(0).getBoardVoteId());
		voteService.insertVoteOfUser(2l,
				beforeVote.getBoardVotes().get(0).getBoardVoteId());
		
		// when
		Boolean result = voteService.cancleVoteOfUser(1l, beforeVote.getBoardVotes().get(0).getBoardVoteId());
		
		Res afterVote = voteService.getVote(res.getId());
		BoardVoteDto.Res boardVote = afterVote.getBoardVotes().stream()
				.filter(bv -> bv.getBoardVoteId() == beforeVote.getBoardVotes().get(0).getBoardVoteId())
				.findFirst().orElseThrow();
		// then
		assertThat(result).isTrue();
		assertThat(boardVote.getCount()).isEqualTo(1);
		// 존재하지 않는 투표를 취소하고자할 때 발생하는 IllegalArgumentException으로 삭제가 되었는지 검증
		assertThrows(InvalidValueException.class, 
				() -> voteService.cancleVoteOfUser(1l, beforeVote.getBoardVotes().get(0).getBoardVoteId()));

	}
}
