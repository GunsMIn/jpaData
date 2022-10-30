package com.spring.jpadata.repository.jpadata;

import com.spring.jpadata.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {
                                            //<타입,pk매팽타입>

    //쿼리 메소드드
   List<Member> findByUsernameAndAgeGreaterThan(String username,int age);

}
