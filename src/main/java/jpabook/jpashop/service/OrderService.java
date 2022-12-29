package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.TypedQuery;
import java.lang.reflect.Type;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Transactional
    // 주문 하기
    public Long order(Long memberId, Long itemId, int count) {
        // 회원과 상품 가져오기
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 주문 시에 필요한 멤버, 배송, 아이템 만들기
        // 1. 배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // order를 repository에 넣기
        orderRepository.save(order);
        return order.getId();
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    public Order findOne(Long orderId) {
        return orderRepository.findOne(orderId);
    }

    // 검색
    public List<Order> searchOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }

}
