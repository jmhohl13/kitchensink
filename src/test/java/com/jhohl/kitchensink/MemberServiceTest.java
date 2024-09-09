package com.jhohl.kitchensink;

import com.jhohl.kitchensink.data.MemberRepository;
import com.jhohl.kitchensink.model.Member;
import com.jhohl.kitchensink.service.MemberService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;


    @Test
    void testFindMemberById() {
        Member member = new Member();
        member.setId(1L);
        member.setEmail("example@example.com");
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Optional<Member> found = memberService.getMemberById(1L);

        assertTrue(found.isPresent(), "Member should be present");
        assertEquals("example@example.com", found.get().getEmail());
    }

    @Test
    void testSaveMember() {
        Member member = new Member();
        member.setName("John Doe");
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        Member savedMember = memberService.saveMember(member);

        assertNotNull(savedMember);
        assertEquals("John Doe", savedMember.getName());
    }
}
