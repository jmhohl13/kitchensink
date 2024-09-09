package com.jhohl.kitchensink;

import com.jhohl.kitchensink.data.MemberRepository;
import com.jhohl.kitchensink.model.Member;
import com.jhohl.kitchensink.service.MemberService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class MemberServiceIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testFindAllOrderedByName() {
        Member m1 = new Member("Alice", "alice@example.com", "1234567890");
        Member m2 = new Member("Bob", "bob@example.com", "0987654321");
        entityManager.persist(m1);
        entityManager.persist(m2);
        entityManager.flush();

        List<Member> members = memberRepository.findAllOrderedByName();

        assertEquals(2, members.size());
        assertTrue(members.get(0).getName().compareTo(members.get(1).getName()) < 0);
    }
}
