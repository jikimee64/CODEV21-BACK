package com.j2kb.codev21.domains.user.repository;

import com.j2kb.codev21.domains.user.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
