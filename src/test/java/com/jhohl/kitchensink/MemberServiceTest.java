package com.jhohl.kitchensink;

import com.jhohl.kitchensink.data.MemberRepository;
import com.jhohl.kitchensink.model.Member;
import com.jhohl.kitchensink.service.MemberService;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private static Validator validator;

    @BeforeAll
    static void setupValidatorInstance() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGetMembers() {
        when(memberRepository.findAll()).thenReturn(Arrays.asList(new Member(), new Member()));
        List<Member> members = memberService.getMembers();
        assertEquals(2, members.size());
        verify(memberRepository).findAll();
    }

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

    @Test
    void testDeleteMember() {
        Long memberId = 1L;
        memberService.deleteMember(memberId);
        verify(memberRepository).deleteById(memberId);
    }

    @Test
    void testRegisterNewMember() {
        Member newMember = new Member();
        newMember.setEmail("new@example.com");
        when(memberRepository.save(any(Member.class))).thenReturn(newMember);

        Member registeredMember = memberService.registerNewMember(newMember);

        assertNotNull(registeredMember);
        assertEquals("new@example.com", registeredMember.getEmail());
        verify(memberRepository).save(newMember);
    }




}
