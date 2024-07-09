package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.Memo;
import com.birca.bircabackend.command.birca.domain.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;

    public void save(Long birthdayCafeId, String content) {
        memoRepository.deleteByBirthdayCafeId(birthdayCafeId);
        Memo memo = new Memo(birthdayCafeId, content);
        memoRepository.save(memo);
    }
}
