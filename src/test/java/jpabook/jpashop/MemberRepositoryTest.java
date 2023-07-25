package jpabook.jpashop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional // 테스트에 있으면 DB 롤백을 함.
    @Rollback(value = false) // 이 옵션이 있으면 롤백을 하지않음.
    void save() {
        // given
        Member member = new Member();
        member.setName("a");
        // when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.findById(saveId);
        // then
        assertThat(findMember.getId()).isEqualTo(saveId);
    }
}