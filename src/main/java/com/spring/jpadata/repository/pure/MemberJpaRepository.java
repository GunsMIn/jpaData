package com.spring.jpadata.repository.pure;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpadata.dto.MemberSearchCondition;
import com.spring.jpadata.dto.MemberTeamDto;
import com.spring.jpadata.dto.QMemberResponse;
import com.spring.jpadata.dto.QMemberTeamDto;
import com.spring.jpadata.entity.Member;
import com.spring.jpadata.entity.QMember;
import com.spring.jpadata.entity.QTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static com.spring.jpadata.entity.QMember.*;
import static com.spring.jpadata.entity.QTeam.team;

//순수 jpa 레퍼지토리
@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    private EntityManager em;

    private final JPAQueryFactory queryFactory; // 메인application에 bean 으로 등록



    //insert
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    //delete
    public void delete(Member member) {
        em.remove(member);
    }

    //select by id (Optional)
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }


    //select all
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
    /**QueryDsl**/
    //쿼리디에스일 select all
    public List<Member> findAllByDSl() {
        return queryFactory.selectFrom(member).fetch();
    }

    //userName으로 찾기
    public List<Member> findByUsername(String username) {
        List<Member> resultList = em.createQuery("select m from Member m where m.username =:username", Member.class)
                .setParameter("username", username)
                .getResultList();
        return resultList;
    }

    /**QueryDsl**/
    //쿼리디에스일 findByUsename
    public List<Member> findDslByUsername(String username) {
        return queryFactory
                .selectFrom(member)
                .where(member.username.eq(username))
                .fetch();
    }


    /**
     * QueryDsl
     **/
    //검색하기
    public List<MemberTeamDto> searchByBulider(MemberSearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();
        //null이 들어올 수 있고 ""이 들어올 수도 있다.
        if (StringUtils.hasText(condition.getUsername())) {
            builder.and(member.username.eq(condition.getUsername()));
        }
        if (StringUtils.hasText(condition.getTeamName())) {
            builder.and(team.name.eq(condition.getTeamName()));
        }
        if (condition.getAgeGoe() != null) {
            builder.and(member.age.goe(condition.getAgeGoe()));
        }
        if (condition.getAgeLoe() != null) {
            builder.and(member.age.loe(condition.getAgeLoe()));
        }

        queryFactory
                .select(
                        new QMemberTeamDto(
                                member.id.as("memberId"),
                                member.username,
                                member.age,
                                team.id.as("teamId"),
                                team.name.as("teamName")))
                .from(member)
                .where(builder)
                .leftJoin(member.team, team)
                .fetch();

    }




    //count
    public Long getCount() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }


    public List<Member> findByUsernameAndAgeGreaterThen(String username, int age) {
        return em.createQuery("select m from Member m where m.username =:username and m.age > :age", Member.class)
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    //순수 페이징 처리
    //검색 조건: 나이가 10살
    //정렬 조건: 이름으로 내림차순
    //페이징 조건: 첫 번째 페이지, 페이지당 보여줄 데이터는 3건
    public List<Member> findByPage(int age, int offset, int limit) { // 몇번째 부터 시작해서 몇 개를 가져와
        return em.createQuery("select m from Member m where m.age =:age order by m.username desc")
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    //페이징 처리할때에는 내가 지금현재 몇번째 페이지야 라는것이 필요하다
    public Long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age =:age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
        //단순 count라서 sort필요없다.

    }



}
