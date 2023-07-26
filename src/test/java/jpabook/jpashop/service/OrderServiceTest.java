package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    EntityManager entityManager;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    void 주문_정상() {
        // given
        Member member = createMember();
        Book book = createBook("book", 10000, 10);

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), 2);
        Order order = orderRepository.findOne(orderId);

        // then
        // 주문상태에 대한 테스트
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER);
        // 주문상품 개수에 대한 테스트
        assertThat(order.getOrderItems().size()).isEqualTo(1);
        // 주문 총 가격에 대한 테스트
        assertThat(order.getTotalPrice()).isEqualTo(book.getPrice() * 2);
        // 남은 재고에 대한 테스트 (10 - 2)
        assertThat(book.getStockQuantity()).isEqualTo(8);
    }

    @Test
    void 재고수량_초과() {
        // given
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);
        int orderCount = 13;

        // when
        assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), book.getId(), orderCount));
        // then
    }

    @Test
    void 주문_취소() {
        // given
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);
        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        orderService.cancel(orderId);
        Order order = orderRepository.findOne(orderId);

        // then
        // 주문 상태에 대한 테스트
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
        // 주문 재고 수량에 대한 테스트
        assertThat(book.getStockQuantity()).isEqualTo(10);
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("a");
        member.setAddress(new Address("seoul", "nowon", "11111"));
        this.entityManager.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int quantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        this.entityManager.persist(book);
        return book;
    }
}