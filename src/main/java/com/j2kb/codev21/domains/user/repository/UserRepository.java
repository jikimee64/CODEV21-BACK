package com.j2kb.codev21.domains.user.repository;

import com.j2kb.codev21.domains.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByEmail(String email);

    @Query("select m from User m left join fetch m.authorities where m.email= :email")
    Optional<User> findOneWithAuthoritiesByAccount(@Param("email") String email);
}


