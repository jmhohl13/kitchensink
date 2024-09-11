package com.jhohl.kitchensink.controller;

import com.jhohl.kitchensink.model.Member;
import com.jhohl.kitchensink.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping()
public class MemberController {

    @Autowired
    private MemberService memberService;

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private Member newMember;

    @ModelAttribute("newMember")
    public Member initNewMember() {
        return new Member();
    }

    @GetMapping()
    public String home(Model model) {
        logger.info("Accessing home page");
        model.addAttribute("newMember", new Member());
        List<Member> members = memberService.getMembers();
        model.addAttribute("members", members);
        return "home"; // Refers to a Thymeleaf or JSP page that displays members
    }


    @PostMapping
    public String registerMember(@Valid @ModelAttribute("newMember") Member member, BindingResult result, Model model, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            logger.warn("Validation failed for member: {}", member.getEmail());
            model.addAttribute("newMember", member);
            model.addAttribute("members", memberService.getMembers());
            return "home";
        }
        try{
            memberService.registerNewMember(member);
            logger.info("New member registered: {}", member.getEmail());
            redirectAttrs.addFlashAttribute("successMessage", "Member registered successfully!");
        } catch(ValidationException e){
            logger.error("Error during registration: {}", e.getMessage());

            redirectAttrs.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/";
    }

    private String getRootErrorMessage(Exception e) {
        Throwable t = e;
        while (t.getCause() != null) {
            t = t.getCause();
        }
        return t.getLocalizedMessage(); // Simplified error handling
    }
}
