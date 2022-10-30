package com.spring.jpadata.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    void testEntity() {
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("김건우", 28, teamA);
        Member member2 = new Member("김건희", 26, teamB);
        Member member3 = new Member("김민희", 14, teamA);
        Member member4 = new Member("노승규", 20, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        /*초기화 작업*/
        //em.persist를 하면 바로 디비로 데이터를 보내는것이 아니라 영속성 컨텍스트에 데이터를 모아논다.
        //em.flush를 하면 강제로 디비에 insert쿼리를 날려보낸다.
        em.flush();
        //em.clear를 하면 영속성컨텍스트에 있는 1차 캐시를 다 없애버린다.
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("->member.team= " + member.getTeam());
        }
    }
}