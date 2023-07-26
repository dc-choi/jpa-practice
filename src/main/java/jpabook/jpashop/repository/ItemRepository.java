package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepository {
    @PersistenceContext
    EntityManager entityManager;

    public void save(Item item) {
        // DB에 값이 없으면 PK가 없음.
        if (item.getId() == null) {
            entityManager.persist(item); // 새로 저장
        } else {
            entityManager.merge(item); // 갱신
        }
    }

    public Item findOne(Long id) {
        return entityManager.find(Item.class, id);
    }

    public List<Item> findAll() {
        return entityManager.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
