package jpabook.jpashop.controller;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    @ResponseBody
    public Order order() {
        // 1. 멤버 생성 확인
        Member member = new Member();
        member.setName("이재석");
        member.setAddress(new Address("경기도", "만안구", "11111"));
//        memberService.join(member);
        Long memberId = memberService.join(member);

        // 2. 상품 생성 // 3. 상품 가격
        Item book = new Book();
        book.setName("해리포터");
        book.setPrice(12000);
        book.setStockQuantity(20);
//        itemService.saveItem(book);
        Long itemId = itemService.saveItem(book);

        // 주문!
        // 주문조회 (다시 가져오기) -> 클라이언트한테 응답
        //                                           memberId     itemId
        Long orderId = orderService.order(member.getId(), book.getId(), 20);

        return orderService.findOne(orderId);
    }



}
