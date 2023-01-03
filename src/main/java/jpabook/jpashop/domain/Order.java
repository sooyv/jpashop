package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// "ORDER BY"
@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    // 연관관계의 주인 : FK 있는 쪽 (Order)
    // 지연 로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // -> DB의 FK
    private Member member;  //member table의 member_id (FK)로 하겠다는 것을 jpa에게 알려줌

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @JsonIgnore                         //엔티티의 상태가 변하면, 연관된 엔티티까지 알려준다?
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    // domain 내에 메소드를 선언
    // == 연관 관계 ==
    public void setMember(Member member) {  //위의 member를 여기서 지정
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }


    // 비즈니스 로직 //
    // 주문 등록
    public static Order createOrder(Member member,
                                    Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    //주문 취소 - 배송이 완료 되었으면 주문 취소 안됌
    public void cancel() {
        if(delivery.getStatus() == DeliveryStatus.READY.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소할 수 없습니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        // 취소한 상품 재고를 증가 시켜야 함
        // 주문에 대한 주문 상품들이 여러개 있을 수 있다.
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // 전체 주문 가격 조회
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
