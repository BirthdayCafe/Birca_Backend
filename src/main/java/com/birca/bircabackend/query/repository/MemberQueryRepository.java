package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.member.domain.Member;
import org.springframework.data.repository.Repository;

public interface MemberQueryRepository extends Repository<Member, Long> {

    Boolean existsByNickname(String nickname);
}
