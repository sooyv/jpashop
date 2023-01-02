package jpabook.jpashop.controller;

import jpabook.jpashop.controller.form.OrderForm;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/orders/new")
        public String createOrder(Model model) {

        // 멤버 가져오기 -- 번호로?
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        // 상품 리스트에서 상품 가져오기
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);

        return "/orders/orderForm";
    }

    @PostMapping("/order")
    // 선택한 내용을 저장
    public String productOrder(OrderForm orderForm) {
        Member member = memberService.findOne(orderForm.getMemberId());
        Item item = itemService.findOne(orderForm.getItemId());
        int count = orderForm.getCount();

        orderService.order(member.getId(), item.getId(), count);

        return "redirect:/";
    }
}
