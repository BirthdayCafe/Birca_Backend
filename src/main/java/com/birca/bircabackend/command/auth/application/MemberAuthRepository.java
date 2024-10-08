package com.birca.bircabackend.command.auth.application;

import com.birca.bircabackend.command.member.domain.IdentityKey;
import com.birca.bircabackend.command.member.domain.Member;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MemberAuthRepository extends Repository<Member, Long> {

    Optional<Member> findByIdentityKey(IdentityKey identityKey);
}
