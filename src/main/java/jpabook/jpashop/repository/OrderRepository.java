package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;
    public void save(Order order) {
        em.persist(order);
    }
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }
    //동적 쿼리 작성법
    //실무에서는 조건에 따라 실행되는 쿼리가 달라지는 동적쿼리를 많이 사용한다.
    //쿼리를 분기식을 통해 문자로 직접 짜는 방법은 복잡하고 실수가 잦을 수 있으므로 비추.
    //JPA 공식문서에 criteria 를 활용한 동적쿼리방법이 있지만
    // 실제 실무에서는 Querydsl로 처리할 것을 추천한다.
    public List<Order> findAll(OrderSearch orderSearch) {
        List<Order> resultList = em.createQuery("select o from Order o join o.member m" +
                        " where o.status = :status " +
                        "and m.name like :name", Order.class)
                .setParameter("status",orderSearch.getOrderStatus())
                .setParameter("name",orderSearch.getMemberName())
                //.setFirstResult(100) :100부터 시작함 (페이징할때 사용)
                .setMaxResults(1000)
                .getResultList();
        return resultList;
    }
}
