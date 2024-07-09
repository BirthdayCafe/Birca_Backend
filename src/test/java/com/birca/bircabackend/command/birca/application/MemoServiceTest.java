package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.Memo;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Sql("/fixture/memo-fixture.sql")
class MemoServiceTest extends ServiceTest {

    @Autowired
    private MemoService memoService;

    @PersistenceContext
    private EntityManager entityManager;

    @Nested
    @DisplayName("생일 카페 메모를 저장할 때")
    class MemoSaveTest {

        private static final Long BIRTHDAY_CAFE_ID = 1L;

        @Test
        void 정상적으로_저장한다() {
            // given
            String content = "생일 카페 메모 내용";

            // when
            memoService.save(BIRTHDAY_CAFE_ID, content);
            Memo memo = entityManager.createQuery("select m from Memo m where m.birthdayCafeId = :birthdayCafeId", Memo.class)
                    .setParameter("birthdayCafeId", BIRTHDAY_CAFE_ID)
                    .getSingleResult();

            // then
            assertAll(
                    () -> assertThat(memo.getBirthdayCafeId()).isEqualTo(BIRTHDAY_CAFE_ID),
                    () -> assertThat(memo.getContent()).isEqualTo(content)
            );
        }
    }
}