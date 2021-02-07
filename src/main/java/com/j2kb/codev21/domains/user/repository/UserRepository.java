package com.j2kb.codev21.domains.user.repository;

import com.j2kb.codev21.domains.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

}
