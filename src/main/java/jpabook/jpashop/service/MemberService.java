package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 읽을때는 트랜잭션을 사용하지 않도록 함.
// 생성자를 하나만 둘때는 @Autowired를 사용할 수 있음.
// @AllArgsConstructor // 프로퍼티를 전부 사용해서 생성자를 만듬.
@RequiredArgsConstructor // final이 있는 프로퍼티를 전부 사용해서 생성자를 만듬.
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional // CUD에서는 트랜잭션을 사용해야 함.
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    public List<Member> list() {
        return memberRepository.findAll();
    }

    public Member get(Long id) {
        return memberRepository.findOne(id);
    }

    /**
     * 참고: 실무에서는 검증 로직이 있어도 멀티 쓰레드 상황을 고려해서 회원 테이블의 회원명 컬럼에 유니크 제약 조건을 추가하는 것이 안전하다.
     */
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}
