package com.birca.bircabackend.command.member;

import com.birca.bircabackend.command.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberFixtureRepository extends JpaRepository<Member, Long> {
}
