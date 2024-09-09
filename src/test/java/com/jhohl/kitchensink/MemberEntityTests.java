package com.jhohl.kitchensink;

import com.jhohl.kitchensink.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class MemberEntityTests {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testMemberMappings() {
        Member member = new Member();
        member.setName("John Doe");
        member.setEmail("john.doe@example.com");
        member.setPhoneNumber("1234567890");

        Member savedMember = entityManager.persistFlushFind(member);

        assertNotNull(savedMember.getId());
        assertEquals("John Doe", savedMember.getName());
        assertEquals("john.doe@example.com", savedMember.getEmail());
        assertEquals("1234567890", savedMember.getPhoneNumber());
    }
}
