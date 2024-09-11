package com.jhohl.kitchensink.data;

import com.jhohl.kitchensink.model.Member;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends MongoRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    List<Member> findAllByOrderByNameAsc();
}
