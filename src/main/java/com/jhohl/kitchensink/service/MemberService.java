package com.jhohl.kitchensink.service;

import com.jhohl.kitchensink.data.MemberRepository;
import com.jhohl.kitchensink.model.Member;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<Member> getMembers(){
        return memberRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Optional<Member> getMemberById(Long id){
        return memberRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Member getMemberByEmail(String email){
        return memberRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Member> getAllMembersOrderedbyName(){
        return memberRepository.findAllOrderedByName();
    }

    @Transactional
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    @Transactional
    public Member registerNewMember(@Valid Member newMember) {
        // Additional business logic can be implemented here if needed
        return memberRepository.save(newMember);
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }


}
