package com.mycustomblog.blog.repository;

import com.mycustomblog.blog.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long>{
    Member findByUserId(String userId);
    Member findByEmail(String email);
}
