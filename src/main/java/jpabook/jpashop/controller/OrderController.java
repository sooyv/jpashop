package jpabook.jpashop.controller;

import jpabook.jpashop.controller.form.OrderForm;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/orders/new")
        public String createOrder(Model model) {
        //model을 view에게 던져주기

        // 멤버 가져오기 -- 번호로?
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        // 상품 리스트에서 상품 가져오기
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);

        return "/orders/orderForm";
    }

//    @PostMapping("/order")
//    // 선택한 내용을 저장
//    public String order(OrderForm orderForm) {
//        Member member = memberService.findOne(orderForm.getMemberId());
//        Item item = itemService.findOne(orderForm.getItemId());
//        int count = orderForm.getCount();
//
//        orderService.order(member.getId(), item.getId(), count);
//
//        return "redirect:/";
//    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {
//        log.info();
        orderService.order(memberId, itemId, count);
        return "redirect:/";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {

        List<Order> orders = orderService.searchOrders(orderSearch);

        model.addAttribute("orders", orders);

        return "/orders/orderList";
    }

    @PostMapping("/orders/{id}/cancel")
    public String orderStatus(@PathVariable("id") Long id) {
        orderService.cancelOrder(id);

        return "redirect:/orders";
    }
}
