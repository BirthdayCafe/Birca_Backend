package com.birca.bircabackend.command.member.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MemberRepository extends Repository<Member, Long> {

    Optional<Member> findById(Long id);

    boolean existsByNickname(String nickname);
}
