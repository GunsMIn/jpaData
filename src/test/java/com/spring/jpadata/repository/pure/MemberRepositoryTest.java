package com.spring.jpadata.repository;

import com.spring.jpadata.entity.Member;
import com.spring.jpadata.repository.jpadata.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {



    @Autowired
    MemberRepository memberRepository;


    @BeforeEach
    void setUp() {
        Member member1 = new Member("a");;
        Member member2 = new Member("b");;
        Member member3 = new Member("c");;
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
    }

    @Test
    void testMember() {
        Member member = new Member("김건희");
        Member saveMember = memberRepository.save(member);
        Optional<Member> byIdMember = memberRepository.findById(saveMember.getId());
        Member memberfindId = byIdMember.get();
        assertThat(member).isEqualTo(memberfindId);
        assertThat(member.getUsername()).isEqualTo(memberfindId.getUsername());
        assertThat(member.getId()).isEqualTo(memberfindId.getId());
    }

    @Test
    @DisplayName("등록된 유저의 count 테슽츠")
    void countMember() {
        List<Member> all = memberRepository.findAll();
        int size = all.size();
        assertThat(size).isEqualTo(3);
    }


}