package com.jhohl.kitchensink.controller;

import com.jhohl.kitchensink.model.Member;
import com.jhohl.kitchensink.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    private Member newMember;

    @ModelAttribute("newMember")
    public Member initNewMember() {
        return new Member();
    }

    @GetMapping
    public String listMembers(Model model) {
        List<Member> members = memberService.getMembers();
        model.addAttribute("members", members);
        return "members/list"; // Refers to a Thymeleaf or JSP page that displays members
    }

    @PostMapping()
    public String registerMember(@Valid @ModelAttribute("member") Member member, BindingResult result, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            // Include errors and member information in the redirect to pre-populate the form and show errors
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.member", result);
            redirectAttrs.addFlashAttribute("member", member);
            return "redirect:/members"; // Assume there's a registration form at this path
        }

        try {
            memberService.registerNewMember(member);
            redirectAttrs.addFlashAttribute("successMessage", "Member registered successfully!");
            return "redirect:/members";
        } catch (ValidationException e) {
            redirectAttrs.addFlashAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "redirect:/members";
        }
    }

    private String getRootErrorMessage(Exception e) {
        Throwable t = e;
        while (t.getCause() != null) {
            t = t.getCause();
        }
        return t.getLocalizedMessage(); // Simplified error handling
    }
}
