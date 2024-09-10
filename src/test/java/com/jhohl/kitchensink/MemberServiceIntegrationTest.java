package com.jhohl.kitchensink;

import com.jhohl.kitchensink.data.MemberRepository;
import com.jhohl.kitchensink.model.Member;
import com.jhohl.kitchensink.service.MemberService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MemberServiceIntegrationTest {


    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setup() {
        memberRepository.deleteAll();
    }
    @Test
    public void testFindAllOrderedByName() {
        Member m1 = new Member("Alice", "alice@example.com", "1234567890");
        Member m2 = new Member("Bob", "bob@example.com", "0987654321");
        memberService.registerNewMember(m1);
        memberService.registerNewMember(m2);

        List<Member> members = memberService.getAllMembersOrderedbyName();
        // Check if the list is sorted correctly without assuming the list size
        assertFalse(members.isEmpty(), "The member list should not be empty");
        assertTrue(isSortedByName(members), "Members should be sorted by name");
    }


    private boolean isSortedByName(List<Member> members) {
        for (int i = 0; i < members.size() - 1; i++) {
            if (members.get(i).getName().compareTo(members.get(i + 1).getName()) > 0) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void testSaveAndRetrieveMember() {
        Member member = new Member();
        member.setName("John Doe");
        member.setEmail("john@example.com");
        member.setPhoneNumber("1234567890");

        // Saving the member
        member = memberService.registerNewMember(member);

        // Retrieving the saved member
        Member found = memberService.getMemberById(member.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("John Doe", found.getName());
        assertEquals("john@example.com", found.getEmail());
    }

    @Test
    public void testDeleteMember() {
        // Create and save a new member to ensure it exists
        Member member = new Member();
        member.setName("Jane Doe");
        member.setEmail("jane@example.com");
        member.setPhoneNumber("1234567890");
        member = memberService.saveMember(member);

        // Ensure member is saved
        assertNotNull(memberService.getMemberById(member.getId()));

        // Delete the member
        memberService.deleteMember(member.getId());

        // Verify the member is deleted
        assertTrue(memberService.getMemberById(member.getId()).isEmpty());
    }

}
