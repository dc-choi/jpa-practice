package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        // @Valid: form을 검증하는 어노테이션
        // 에러가 발생하면 BindingResult에 에러가 넘어감.
        // 검증하는 부분에서 에러가 나는 경우 다시 페이지로 복귀
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        // 원칙적으로는 DTO를 선언해서 그 DTO로 응답을 해줘야함. 요청도 마찬가지.
        // Entity를 외부에 노춣시키지 않아야 한다.
        // Entity가 변경될 경우 API 스펙이 변경되기 때문이다.
        List<Member> members = memberService.list();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
