package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 스프링부트와 함꼐 테스트를 한다.
@Transactional // test에서는 성공해도 롤백시킨다.
class MemberServiceTest {
    // 테스트에서는 필드주입을 그냥 사용한다.
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void 회원가입_정상() {
        // given
        Member member = new Member();
        member.setName("a");
        // when
        Long saveId = memberService.join(member);
        // then
        assertThat(member).isEqualTo(memberService.get(saveId));
    }

    @Test
    void 회원가입_중복() {
        // given
        Member member = new Member();
        member.setName("a");

        Member member2 = new Member();
        member2.setName("a");
        // when
        memberService.join(member);
        // then
        assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));
    }
}