package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.Memo;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
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

        @Test
        void 아무것도_없으면_새로_저장한다() {
            // given
            LoginMember loginMember = new LoginMember(3L);
            Long birthdayCafeId = 2L;
            String content = "새로운 생일 카페 메모 내용";

            // when
            memoService.save(birthdayCafeId, content, loginMember.id());
            Memo memo = entityManager.createQuery("select m from Memo m where m.birthdayCafeId = :birthdayCafeId", Memo.class)
                    .setParameter("birthdayCafeId", birthdayCafeId)
                    .getSingleResult();

            // then
            assertAll(
                    () -> assertThat(memo.getBirthdayCafeId()).isEqualTo(birthdayCafeId),
                    () -> assertThat(memo.getContent()).isEqualTo(content)
            );
        }

        @Test
        void 이미_존재하면_수정한다() {
            // given
            LoginMember loginMember = new LoginMember(3L);
            Long birthdayCafeId = 1L;
            String content = "변경된 생일 카페 메모 내용";

            // when
            memoService.save(birthdayCafeId, content, loginMember.id());
            Memo memo = entityManager.createQuery("select m from Memo m where m.birthdayCafeId = :birthdayCafeId", Memo.class)
                    .setParameter("birthdayCafeId", birthdayCafeId)
                    .getSingleResult();

            // then
            assertAll(
                    () -> assertThat(memo.getBirthdayCafeId()).isEqualTo(birthdayCafeId),
                    () -> assertThat(memo.getContent()).isEqualTo(content)
            );
        }

        @Test
        void 존재하지_않는_생일_카페는_예외가_발생한다() {
            // given
            LoginMember loginMember = new LoginMember(3L);
            Long birthdayCafeId = 100L;
            String content = "생일 카페 메모 내용";

            // when then
            assertThatThrownBy(() -> memoService.save(birthdayCafeId, content, loginMember.id()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.NOT_FOUND);
        }

        @Test
        void 카페_주인이_아니면_예외가_발생한다() {
            // given
            LoginMember loginMember = new LoginMember(100L);
            Long birthdayCafeId = 1L;
            String content = "생일 카페 메모 내용";

            // when then
            assertThatThrownBy(() -> memoService.save(birthdayCafeId, content, loginMember.id()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPDATE);
        }
    }
}