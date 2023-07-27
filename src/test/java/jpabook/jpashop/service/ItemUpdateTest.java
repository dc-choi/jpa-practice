package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * 준영속 엔티티?
 * 영속성 컨텍스트가 더는 관리하지 않는 엔티티를 말한다.
 * 여기서는 itemService.save(book)에서 수정을 시도하는 Book 객체다.
 * Book 객체는 이미 DB에 한번 저장되어서 식별자가 존재한다.
 * 이렇게 임의로 만들어낸 엔티티도 기존 식별자를 가지고 있으면 준영속 엔티티로 볼 수 있다.
 *
 * 준영속 엔티티를 수정하는 2가지 방법
 * 1. 변경감지(Dirty checking)
 * 영속성 컨텍스트에서 엔티티를 다시 조회한 후에 데이터를 수정하는 방법이다.
 * 트랜잭션 안에서 엔티티를 다시 조회, 변경할 값 선택
 * 트랜잭션 커밋 시점에 변경 감지(Dirty Checking)이 동작해서 데이터베이스에 UPDATE SQL 실행
 *
 * 2. 병합(merge)
 * 준영속 상태의 엔티티를 영속 상태로 변경할 때 사용하는 기능이다.
 * 병합에서도 내부적으로는 변경감지와 같이 준영속 엔티티를 영속 상태로 변경한다.
 *
 * 병합 동작 방식
 * 1. merge()를실행한다.
 * 2. 파라미터로 넘어온 준영속 엔티티의 식별자 값으로 1차 캐시에서 엔티티를 조회한다.
 * 2-1. 만약 1차 캐시에 엔티티가 없으면 데이터베이스에서 엔티티를 조회하고, 1차 캐시에 저장한다.
 * 3. 조회한 영속 엔티티(mergeMember)에 member 엔티티의 값을 채워 넣는다.
 * (member 엔티티의 모든 값을 mergeMember에 밀어 넣는다. 이때 mergeMember의 “회원1”이라는 이름이 “회원명변경”으로 바뀐다.)
 * 4. 영속 상태인 mergeMember를 반환한다.
 *
 * 정리
 * 1. 준영속 엔티티의 식별자 값으로 영속 엔티티를 조회한다.
 * 2. 영속 엔티티의 값을 준영속 엔티티의 값으로 모두 교체한다.(병합한다.)
 * 3. 트랜잭션 커밋 시점에 변경 감지 기능이 동작해서 데이터베이스에 UPDATE SQL이 실행
 *
 * 주의: 변경 감지 기능을 사용하면 원하는 속성만 선택해서 변경할 수 있지만, 병합을 사용하면 모든 속성이 변경된다.
 * 병합시 값이 없으면 null로 업데이트 할 위험도 있다. (병합은 모든 필드를 교체한다.)
 *
 * 가장 좋은 해결 방법
 * 엔티티를 변경할 때는 항상 변경 감지를 사용하세요
 * 컨트롤러에서 어설프게 엔티티를 생성하지 마세요.
 * 트랜잭션이 있는 서비스 계층에 식별자( id )와 변경할 데이터를 명확하게 전달하세요.(파라미터 or dto)
 * 트랜잭션이 있는 서비스 계층에서 영속 상태의 엔티티를 조회하고, 엔티티의 데이터를 직접 변경하세요.
 * 트랜잭션 커밋 시점에 변경 감지가 실행됩니다.
 */
@SpringBootTest
@Transactional
class ItemUpdateTest {
    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void test() {
        // 변경감지에 해당하는 코드... Dirty checking
        Book book = entityManager.find(Book.class, 1L);
        book.setName("setName!!!");
    }
    @Test
    public void test2() {
        // 병합에 해당하는 의사 코드...
        // Item param // 해당 코드는 매개변수라고 가정... 이게 준영속 엔티티
        // Item merge = entityManager.merge(param); // 반환되는 값이 영속성을 가진 영속 엔티티
    }

}
