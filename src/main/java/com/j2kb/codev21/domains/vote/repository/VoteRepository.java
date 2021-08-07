package com.j2kb.codev21.domains.vote.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.j2kb.codev21.domains.vote.domain.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long>, VoteRepositoryCustom {

	Optional<Vote> findByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);
}
