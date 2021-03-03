package com.j2kb.codev21.domains.vote.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.j2kb.codev21.domains.vote.dto.BoardVoteDto;
import com.j2kb.codev21.domains.vote.dto.VoteDto;
import com.j2kb.codev21.domains.vote.dto.VoteDto.Req;
import com.j2kb.codev21.domains.vote.dto.VoteDto.Res;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles({ "vote_test_init", "dev", "secret" })
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
class VoteServiceTest {

	@Autowired
	VoteService voteService;

	@Test
	void 투표_등록() {
		// given
		Req param = VoteDto.Req.builder()
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(7))
				.boardIds(List.of(1l, 2l, 3l, 4l)).build();

		// when
		Long insertedVoteId = voteService.insertVote(param);

		// then
		assertThat(insertedVoteId).isNotNull();

	}
	
	@Test
	void 투표_조회() {
		// given
		Req param = VoteDto.Req.builder()
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(7))
				.boardIds(List.of(1l, 2l, 3l, 4l))
				.build();

		Long insertedVoteId = voteService.insertVote(param);

		// when
		Res result = voteService.getVote(insertedVoteId);

		// then
		log.info("result = " + result);
		assertThat(result.getStartDate()).isEqualToIgnoringNanos(param.getStartDate());
		assertThat(result.getEndDate()).isEqualToIgnoringNanos(param.getEndDate());
		assertThat(result.getBoardVotes().stream()
						.map(boardVote -> boardVote.getBoardId())
						.collect(Collectors.toList()))
				.containsAll(param.getBoardIds());
	}
	
	@Test
	void 전체_투표_조회() {
		// given
		Req param = VoteDto.Req.builder()
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(7))
				.boardIds(List.of(1l, 2l, 3l, 4l))
				.build();
		Req param2 = VoteDto.Req.builder()
				.startDate(LocalDateTime.now().plusDays(3))
				.endDate(LocalDateTime.now().plusDays(10))
				.boardIds(List.of(5l, 6l, 7l, 8l))
				.build();

		Long insertedVoteId1 = voteService.insertVote(param);
		Long insertedVoteId2 = voteService.insertVote(param2);

		// when
		 List<Res> result = voteService.getVoteList();

		// then
		assertThat(result.stream()
							.mapToLong(VoteDto.Res::getId)
							.toArray())
				.contains(insertedVoteId1, insertedVoteId2);
	}
	
	@Test
	void 투표_수정() {
		// given
		Req insertParam = VoteDto.Req.builder()
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(7))
				.boardIds(List.of(1l, 2l, 3l, 4l))
				.build();
		
		Req updateParam = VoteDto.Req.builder()
				.startDate(LocalDateTime.now().plusDays(7))
				.endDate(LocalDateTime.now().plusDays(14))
				.build();


		Long insertedVoteId = voteService.insertVote(insertParam);

		// when
		Res result = voteService.updateVote(insertedVoteId, updateParam);

		// then
		assertThat(result.getStartDate()).isEqualToIgnoringNanos(updateParam.getStartDate());
		assertThat(result.getEndDate()).isEqualToIgnoringNanos(updateParam.getEndDate());
	}
	
	@Test
	void 투표_삭제() {
		// given
		Req insertParam = VoteDto.Req.builder()
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(7))
				.boardIds(List.of(1l, 2l, 3l, 4l))
				.build();


		Long insertedVoteId = voteService.insertVote(insertParam);
		
		// when
		Boolean result = voteService.deleteVote(insertedVoteId);

		// then
		assertThat(result).isTrue();
		assertThrows(NoSuchElementException.class,
					() -> voteService.getVote(insertedVoteId));
		
	}
	
	@Test
	void 투표_게시글_단건_등록() {
		// given
		Req insertParam = VoteDto.Req.builder()
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(7))
				.boardIds(List.of(1l, 2l, 3l))
				.build();


		Long insertedVoteId = voteService.insertVote(insertParam);

		// when
		List<BoardVoteDto.Res> result = voteService.includeBoardIntoVote(insertedVoteId, 4l);
		
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
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(7))
				.boardIds(List.of(1l, 2l, 3l))
				.build();


		Long insertedVoteId = voteService.insertVote(insertParam);

		// when
		List<BoardVoteDto.Res> result = voteService.includeBoardIntoVote(insertedVoteId, 4l);
		
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
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(7))
				.boardIds(List.of(1l, 2l, 3l))
				.build();


		Long insertedVoteId = voteService.insertVote(insertParam);

		// when
		List<BoardVoteDto.Res> result = voteService.excludeBoardInVote(insertedVoteId, 1l);
		
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
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(7))
				.boardIds(List.of(1l, 2l, 3l))
				.build();


		Long insertedVoteId = voteService.insertVote(insertParam);

		// when
		BoardVoteDto.Req excludeParam = BoardVoteDto.Req.builder()
										.boardIds(List.of(1l, 2l))
										.build();
		List<BoardVoteDto.Res> result = voteService.excludeBoardListInVote(insertedVoteId, excludeParam);
		
		
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
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(7))
				.boardIds(List.of(1l, 2l, 3l))
				.build();


		Long insertedVoteId = voteService.insertVote(insertParam);

		// when
		Res beforeVote = voteService.getVote(insertedVoteId);
		Boolean result = voteService.insertVoteOfUser(1l,
					beforeVote.getBoardVotes().get(0).getBoardVoteId());
		
		Res afterVote = voteService.getVote(insertedVoteId);
		BoardVoteDto.Res boardVote = afterVote.getBoardVotes().stream()
				.filter(bv -> bv.getBoardVoteId() == beforeVote.getBoardVotes().get(0).getBoardVoteId())
				.findFirst().orElseThrow();
		// then
		assertThat(result).isTrue();
		assertThat(boardVote.getCount()).isEqualTo(1);
	}
	
	@Test
	void 사용자_투표_취소_요청() {
		// given
		Req insertParam = VoteDto.Req.builder()
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(7))
				.boardIds(List.of(1l, 2l, 3l))
				.build();


		Long insertedVoteId = voteService.insertVote(insertParam);
		Res beforeVote = voteService.getVote(insertedVoteId);
		voteService.insertVoteOfUser(1l,
					beforeVote.getBoardVotes().get(0).getBoardVoteId());
		voteService.insertVoteOfUser(2l,
				beforeVote.getBoardVotes().get(0).getBoardVoteId());
		
		// when
		Boolean result = voteService.cancleVoteOfUser(1l, beforeVote.getBoardVotes().get(0).getBoardVoteId());
		
		Res afterVote = voteService.getVote(insertedVoteId);
		BoardVoteDto.Res boardVote = afterVote.getBoardVotes().stream()
				.filter(bv -> bv.getBoardVoteId() == beforeVote.getBoardVotes().get(0).getBoardVoteId())
				.findFirst().orElseThrow();
		// then
		assertThat(result).isTrue();
		assertThat(boardVote.getCount()).isEqualTo(1);


	}
}
