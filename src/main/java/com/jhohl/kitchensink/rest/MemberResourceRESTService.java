package com.jhohl.kitchensink.rest;

import com.jhohl.kitchensink.model.Member;
import com.jhohl.kitchensink.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/members")
public class MemberResourceRESTService {

    @Autowired
    private MemberService memberService;

    private static final Logger logger = LoggerFactory.getLogger(MemberResourceRESTService.class);

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        logger.info("Fetching all members");
        List<Member> members = memberService.getAllMembersOrderedbyName();
        logger.info("Retrieved {} members", members.size());
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        logger.info("Fetching member with ID: {}", id);
        return memberService.getMemberById(id)
                .map(member -> {
                    logger.info("Member found: {}", member.getName());
                    return ResponseEntity.ok(member);
                })
                .orElseGet(() -> {
                    logger.warn("No member found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });

    }

    @PostMapping
    public ResponseEntity<Object> createMember(@Valid @RequestBody Member member, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMap = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            logger.warn("Validation errors: {}", errorMap);
            return ResponseEntity.badRequest().body(errorMap);
        }
        try {
            memberService.registerNewMember(member);
            logger.info("Member created: {}", member.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(member);
        } catch (ValidationException ve) {
            logger.error("Error registering member: {}", ve.getMessage());
            return ResponseEntity.badRequest().body(ve.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        logger.info("Attempting to delete member with ID: {}", id);
        Optional<Member> member = memberService.getMemberById(id);
        if (member.isPresent()) {
            memberService.deleteMember(id);
            logger.info("Member with ID {} deleted", id);
            return ResponseEntity.ok().build();
        } else {
            logger.warn("No member to delete with ID: {}", id);
            return ResponseEntity.notFound().build();  // Return 404 if the member doesn't exist
        }
    }

}
