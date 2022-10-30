package com.spring.jpadata.repository;

import com.spring.jpadata.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
                                            //<타입,pk매팽타입>
}
