package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderService orderService;
    @Autowired
    MemberService memberService;
    @Autowired
    ItemService itemService;


    @Test
    public void 주문취소() {
        //given
        Member member = new Member();
        member.setName("김우성");
        Long memberId = memberService.join(member);

        Item item = new Book();
        item.setName("뉴문");
        item.setPrice(10000);
        item.setStockQuantity(50);
        Long itemId = itemService.saveItem(item);

        Long orderId = orderService.order(memberId, itemId, 1);
        System.out.println("책의 재고" + item.getStockQuantity());

        // when 주문취소
        orderService.cancelOrder(orderId);

        // then
        Order order = orderService.findOne(orderId);
        // 검증
        // 1. OrderStatus가 CANCEL로 변경
        assertEquals(order.getStatus(), OrderStatus.CANCEL);
        // 2. 주문 취소된 상품은 그만큼 재고가 다시 증가해야 한다.
        assertEquals(item.getStockQuantity(), 50);
    }


    @Test
    public Order 주문등록() {
        Member member = new Member();
        member.setName("chloe");
        Long memberId = memberService.join(member);

        Item movie = new Movie();
        movie.setName("브레이킹던");
        movie.setPrice(15000);
        movie.setStockQuantity(20);
        Long itemId = itemService.saveItem(movie);

        Long orderId = orderService.order(member.getId(), movie.getId(), 10);

        return orderService.findOne(orderId);
    }
}