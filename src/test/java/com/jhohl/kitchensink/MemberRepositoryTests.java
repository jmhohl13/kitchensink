package com.jhohl.kitchensink;

import com.jhohl.kitchensink.data.MemberRepository;
import com.jhohl.kitchensink.model.Member;
import com.jhohl.kitchensink.service.SequenceGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@DataMongoTest
@AutoConfigureDataMongo
@ExtendWith(MockitoExtension.class)
public class MemberRepositoryTests {

    @MockBean
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        // Mock the sequence generator to return incremental IDs for each save
        when(sequenceGeneratorService.generateSequence(Member.SEQUENCE_NAME)).thenReturn(1L, 2L);
    }
    @Test
    public void testFindByEmail() {
        Member member = new Member();
        member.setId(sequenceGeneratorService.generateSequence(Member.SEQUENCE_NAME));
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
        john.setId(sequenceGeneratorService.generateSequence(Member.SEQUENCE_NAME));
        Member jane = new Member("Jane Doe", "jane@example.com", "1234567891");
        jane.setId(sequenceGeneratorService.generateSequence(Member.SEQUENCE_NAME));

        memberRepository.save(john);
        memberRepository.save(jane);

        List<Member> members = memberRepository.findAllByOrderByNameAsc();
        assertEquals(2, members.size());
        assertTrue(members.get(0).getName().compareTo(members.get(1).getName()) <= 0);
    }
}
