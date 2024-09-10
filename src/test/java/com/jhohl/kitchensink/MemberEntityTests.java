package com.jhohl.kitchensink;

import com.jhohl.kitchensink.model.Member;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class MemberEntityTests {

    @Autowired
    private TestEntityManager entityManager;
    private static Validator validator;


    @BeforeAll
    static void setupValidatorInstance() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenallValid_thenValidationPasses() {
        Member member = new Member();
        member.setName("John Doe");
        member.setPhoneNumber("1234567890");
        member.setEmail("good.email@example.com");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertTrue(violations.isEmpty());
    }

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

    @Test
    public void whenEmailInvalid_thenValidationFails() {
        Member member = new Member();
        member.setName("John Doe");
        member.setPhoneNumber("1234567890");
        member.setEmail("bad-email");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Must be a well-formed email address")));
    }

    @Test
    public void whenPhoneInvalid_thenValidationFails() {
        Member member = new Member();
        member.setName("John Doe");
        member.setPhoneNumber("12345");
        member.setEmail("good.email@example.com");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Must be between 10-12 characters")));
    }

    @Test
    public void whenNameInvalid_thenValidationFails() {
        Member member = new Member();
        member.setName("John123");
        member.setPhoneNumber("1234567890");
        member.setEmail("good.email@example.com");
        Set<ConstraintViolation<Member>> violations = validator.validateProperty(member, "name");
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Must not contain numbers")));
    }



}
