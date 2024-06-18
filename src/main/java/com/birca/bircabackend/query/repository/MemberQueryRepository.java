package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.member.domain.Member;
import com.birca.bircabackend.command.member.domain.Nickname;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MemberQueryRepository extends Repository<Member, Long> {

    Boolean existsByNickname(Nickname nickname);

    @Query("select m.role from Member m where m.id = :memberId")
    Optional<String> findById(Long memberId);
}
