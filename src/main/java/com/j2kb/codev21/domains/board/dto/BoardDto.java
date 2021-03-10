package com.j2kb.codev21.domains.board.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.BeanMapping;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.j2kb.codev21.domains.board.domain.Board;
import com.j2kb.codev21.domains.vote.domain.Vote;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

public class BoardDto {

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PUBLIC)
	public static class Req {

		private Long gisuId;

		private Long teamId;

		private String title;

		private String content;

		private String summary;
	}

	@Data
	@Builder(buildMethodName = "buildByParam")
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PUBLIC)
	public static class Res {

		private long id;

		private GisuInfo gisuInfo;

		private String title;

		private String content;

		private String summary;

		private Writer writerInfo;

		private TeamInfo teamInfo;

		private String image;

		private VoteInfo voteInfo;

		private LocalDateTime createdAt;

		private LocalDateTime updatedAt;

		@Builder
		public Res(Board board){
			this.id = board.getId();
			this.gisuInfo = new GisuInfo(board.getGisuCategory().getId(), board.getGisuCategory().getGisu());
			this.title = board.getTitle();
			this.content = board.getContent();
			this.summary = board.getSummary();
			this.writerInfo = new Writer(board.getUser().getId(), board.getUser().getName());
			this.teamInfo = new TeamInfo(board.getTeam().getId(), board.getTeam().getTeamName(), board.getTeam().getUserTeams().stream().map(userTeam -> userTeam.getUser().getName()).collect(Collectors.toList()));
			this.image = board.getImage();
//			this.voteInfo 
			this.createdAt = board.getCreatedAt();
			this.updatedAt = board.getUpdatedAt();
		}

	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PUBLIC)
	public static class GisuInfo {

		private long gisuId;

		private String gisuName;
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PUBLIC)
	public static class Writer {

		private long userId;

		private String userName;
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PUBLIC)
	public static class TeamInfo {
		private long teamId;

		private String teamName;

		private List<String> teamMembers = new ArrayList<>();
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PUBLIC)
	public static class VoteInfo {

		private boolean voting;

		private Long boardVoteId;

	}

}
