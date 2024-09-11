package com.jhohl.kitchensink.service;

import com.jhohl.kitchensink.data.MemberRepository;
import com.jhohl.kitchensink.model.Member;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<Member> getMembers(){
        logger.debug("Retrieving all members");

        return memberRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Optional<Member> getMemberById(Long id){
        logger.debug("Retrieving member by ID: {}", id);
        Optional<Member> member = memberRepository.findById(id);
        if (member.isPresent()) {
            logger.debug("Member found: {}", member.get());
        } else {
            logger.debug("No member found with ID: {}", id);
        }
        return member;
    }

    @Transactional(readOnly = true)
    public Optional<Member> getMemberByEmail(String email){
        logger.debug("Retrieving member by Email: {}", email);
        Optional<Member> member = memberRepository.findByEmail(email);

        if (member.isPresent()) {
            logger.debug("Member found: {}", member.get());
        } else {
            logger.debug("No member found with email: {}", email);
        }
        return member;
    }

    @Transactional(readOnly = true)
    public List<Member> getAllMembersOrderedbyName(){

        logger.debug("Retrieving all members ordered by name");
        return memberRepository.findAllOrderedByName();
    }

    @Transactional
    public Member saveMember(Member member) {
        logger.info("Saving member: {}", member);

        Member savedMember = memberRepository.save(member);

        logger.info("Member saved: {}", savedMember);
        return savedMember;
    }

    @Transactional
    public Member registerNewMember(@Valid Member newMember) throws ValidationException{
        logger.info("Registering new member: {}", newMember);

        validateMember(newMember);
        Member registered = memberRepository.save(newMember);

        logger.info("New member registered: {}", registered);
        return registered;
    }

    private void validateMember(Member member) throws ValidationException {
        if (emailAlreadyExists(member.getEmail())) {
            throw new ValidationException("A user with this email already exists!");
        }
    }

    private boolean emailAlreadyExists(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void deleteMember(Long id) {
        logger.info("Deleting member with ID: {}", id);

        memberRepository.deleteById(id);

        logger.info("Member deleted with ID: {}", id);
    }


}
