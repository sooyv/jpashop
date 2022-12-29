package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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

    // 주문 조회 (동적) querydsl
    public List<Order> findAll(OrderSearch orderSearch) {

        // 아무것도 없을때
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;

        // 둘 다 있을때

        // 회원만 있을때

        // 주문만 있을때

        // 주문상태가 있으면 if문 블록 실행
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false; // 있으면 false로 바꿔줌
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        // 회원과 주문 상태를 파악, 동적 쿼리 생성 -> 쿼리를 DB에 전달(jpa)
        // jpql을 만들어서 jpa에게 전달

        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (orderSearch.getMemberName() != null) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }


}
