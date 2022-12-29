package jpabook.jpashop.controller;

import jpabook.jpashop.controller.form.MemberForm;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        // 뷰 : members/createMemberForm
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMembersForm";
    }

    @PostMapping("/members/new")
    //              NotEmpty를 Valid가 검증
    // form을 보고 BindingResult 와 바인딩         // model역할을 같이??????
    public String join(@Valid MemberForm form, BindingResult result) {

        if(result.hasErrors()) {
            return "members/createMembersForm";
        }
        //form에 클라이언트가 데이터를 받도록
        System.out.println(form.getName());
        System.out.println(form.getCity());
        System.out.println(form.getStreet());
        System.out.println(form.getZipcode());

        //form에 클라이언트가 보낸 데이터를 받도록 수정
        // 저장하고 홈으로


        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(new Address(form.getCity(), form.getStreet(), form.getZipcode()));
        memberService.join(member);

        return "redirect:/";
    }

    // 조회
    @GetMapping("/members")
    public String memberList(Model model) {
        List<Member> members = memberService.findMembers();
        // model로 던져주기
        model.addAttribute("members", members);
        return "members/memberList";
    }



}
