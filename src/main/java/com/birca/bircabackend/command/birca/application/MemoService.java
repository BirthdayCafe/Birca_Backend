package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.Memo;
import com.birca.bircabackend.command.birca.domain.MemoRepository;
import com.birca.bircabackend.common.EntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode.NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final EntityUtil entityUtil;

    public void save(Long birthdayCafeId, String content, Long cafeOwnerId) {
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, NOT_FOUND);
        memoRepository.findByBirthdayCafeId(birthdayCafeId)
                .ifPresentOrElse(
                        memo -> birthdayCafe.updateMemo(memo, cafeOwnerId, content),
                        () -> memoRepository.save(new Memo(birthdayCafeId, content))
                );
    }
}
