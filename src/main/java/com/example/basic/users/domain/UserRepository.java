package com.example.basic.users.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "authorities")    // Eager 조회로 authorities 정보를 같이 가져온다.
    Optional<User> findOneWithAuthoritiesByUsername(String username);  // username을 기준으로 User 정보를 가져올 때, 권한 정보고 함께 가져온다.
}
