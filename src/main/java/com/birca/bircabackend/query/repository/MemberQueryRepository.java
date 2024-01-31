package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.member.domain.Member;
import com.birca.bircabackend.command.member.domain.Nickname;
import org.springframework.data.repository.Repository;

public interface MemberQueryRepository extends Repository<Member, Long> {

    Boolean existsByNickname(Nickname nickname);
}
