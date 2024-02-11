package com.birca.bircabackend.command.member.domain;

import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    boolean existsByNickname(Nickname nickname);

    void save(Member member);

    boolean existsByIdentityKey(IdentityKey identityKey);
}
