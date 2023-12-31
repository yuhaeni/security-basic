package com.example.basic.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String username); // username을 기준으로 User정보를 가져올 때, 권한 정보도 함께 가져온다.
}
