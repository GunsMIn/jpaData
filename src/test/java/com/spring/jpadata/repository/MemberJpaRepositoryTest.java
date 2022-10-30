package com.spring.jpadata.repository;

import com.spring.jpadata.entity.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false) // test에서 디비를 확인하고싶다면 RollBack(false)를 해주어야 한다.
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("김건우");
        Member save = memberJpaRepository.save(member);
        Member ids = memberJpaRepository.find(save.getId());

        assertThat(save.getId()).isEqualTo(ids.getId());
        assertThat(save.getUsername()).isEqualTo(ids.getUsername());
        assertThat(save).isEqualTo(ids);
    }



}