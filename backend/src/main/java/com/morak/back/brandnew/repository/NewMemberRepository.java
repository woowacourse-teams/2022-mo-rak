package com.morak.back.brandnew.repository;

import com.morak.back.brandnew.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewMemberRepository extends JpaRepository<Member, Long> {
}
