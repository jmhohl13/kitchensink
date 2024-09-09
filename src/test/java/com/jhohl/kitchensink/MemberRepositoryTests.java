package com.jhohl.kitchensink;

import com.jhohl.kitchensink.data.MemberRepository;
import com.jhohl.kitchensink.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testFindByEmail() {
        Member member = new Member();
        member.setName("John Doe");
        member.setEmail("john.doe@example.com");
        member.setPhoneNumber("1234567890");

        memberRepository.save(member);

        Optional<Member> foundMember = memberRepository.findByEmail("john.doe@example.com");
        assertTrue(foundMember.isPresent(), "Member should be present");
        assertEquals("John Doe", foundMember.get().getName());
    }

    @Test
    public void testFindAllOrderedByName() {
        Member john = new Member("John Doe", "john@example.com", "1234567890");
        Member jane = new Member("Jane Doe", "jane@example.com", "1234567891");

        memberRepository.save(john);
        memberRepository.save(jane);

        List<Member> members = memberRepository.findAllOrderedByName();
        assertEquals(2, members.size());
        assertTrue(members.get(0).getName().compareTo(members.get(1).getName()) <= 0);
    }
}
