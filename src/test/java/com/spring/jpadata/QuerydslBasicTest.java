package com.spring.jpadata;


import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpadata.dto.MemberDto;
import com.spring.jpadata.dto.MemberResponse;
import com.spring.jpadata.dto.QMemberResponse;
import com.spring.jpadata.entity.Member;

import com.spring.jpadata.entity.QMember;
import com.spring.jpadata.entity.QTeam;
import com.spring.jpadata.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


import java.util.List;


import static com.spring.jpadata.entity.QMember.*;
import static com.spring.jpadata.entity.QTeam.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    public void before() {
        jpaQueryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    @DisplayName("JPQL로 작성한 테스트 코드")
    public void startJPQL() {
        Member findMember = em.createQuery("select m from Member m where m.username =:username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    @DisplayName("QueryDsl로 작성한 테스트 코드")
    public void startDsl() {

        //import static com.spring.jpadata.entity.QMember.*;
        Member findMember = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();


        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    @DisplayName("검색조건")
    public void search() {
        Member member1 = jpaQueryFactory.selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10))).fetchOne();


        assertThat(member1.getUsername()).isEqualTo("member1");
        assertThat(member1.getAge()).isEqualTo(10);
        assertThat(member1.getTeam().getName()).isEqualTo("teamA");
    }

    /**
     * 회원 정렬
     * 1. 회원 나이 (내림차순)
     * 2. 회원 이름 (올림차순)
     * 단 2에서 회원 이름이 없으면 마지막에 출려(nulls last)
     */
    @Test
    @DisplayName("QueryDsl 정렬")
    public void sort() {
        em.persist(new Member(null,100));
        em.persist(new Member("member5",100));
        em.persist(new Member("member6",100));

        List<Member> memberList = jpaQueryFactory.selectFrom(member)
                .where(member.age.goe(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast()) //정렬
                .fetch();

        Member member5 = memberList.get(0);
        Member member6 = memberList.get(1);
        Member memberNull = memberList.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    @DisplayName("페이징 처리")
    public void paging() {
        em.persist(new Member("member5",100));
        em.persist(new Member("member6",100));

        List<Member> list = jpaQueryFactory.selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1) // 0부터 시작
                .limit(2)
                .fetch();

        Member member5 = list.get(0);
        Member member4 = list.get(1);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member4.getUsername()).isEqualTo("member4");
    }

    @Test
    @DisplayName("집합 함수")
    public void aggregation() {
        //타입이 Tuple
        List<Tuple> list = jpaQueryFactory.select(
                member.count(),
                member.age.sum(),
                member.age.avg(),
                member.age.max(),
                member.age.min()
        )
                .from(member)
                .fetch();

        Tuple tuple = list.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);


    }

    /**팀의 이름과 각 팀의 평균연령을 구해라*/
    @Test
    @DisplayName("join해서 집합함수 사용하기")
    void group() throws Exception {

       List<Tuple> result = jpaQueryFactory
               .select(team.name, member.age.avg())
               .from(member)
               .join(member.team, team)
               .groupBy(team.name)
               .fetch();

       Tuple teamA = result.get(0);
       Tuple teamB = result.get(1);
       assertThat(teamA.get(team.name)).isEqualTo("teamA");
       assertThat(teamA.get(member.age.avg())).isEqualTo(15);
       assertThat(teamB.get(team.name)).isEqualTo("teamB");
       assertThat(teamB.get(member.age.avg())).isEqualTo(35);

    }

    @Test
    @DisplayName("join test")
    void join() throws Exception {

        List<Member> result = jpaQueryFactory.selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");

    }

    /**회원의 이름이 팀이름과 같은 사람 */
    @Test
    @DisplayName("theta 조인")
    public void theta_join() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Member> result = jpaQueryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    /**
     * 예) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     * JPQL: SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'teamA'
     * SQL: SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.TEAM_ID=t.id and
     t.name='teamA'
     */
    @Test
    @DisplayName("on절 조인인")
   void join_on_filtering() throws Exception {

        List<Tuple> result = jpaQueryFactory.select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

    }

    @Test
    @DisplayName("내부조인과 외부조인의 차이")
    void innerJoin_and_outerJoin() throws Exception {

        List<Tuple> result = jpaQueryFactory.select(member, team)
                .from(member)
                .join(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

    }

    @Test
    @DisplayName("연관관계가 없는 엔티티의 외부조인")
    void join_on_no_relation() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        List<Tuple> result = jpaQueryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team) // 여기가 큰 차이 원래는 member.team
                //이었지만 team만 넣기 때문에 막 join이다 id로 조인 x 조건으로만
                .on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("t=" + tuple);
        }
    }

    @Test
    @DisplayName("일반 조인 not fetchjoin")
    void join2() throws Exception {

        em.flush();
        em.clear();

        Member findMember = jpaQueryFactory
                .selectFrom(member)
                .join(member.team,team)
                .where(member.username.eq("member1"))
                .fetchOne();

    }

    @Test
    @DisplayName("fetch join 적용시")
    void fetch() throws Exception {

        em.flush();
        em.clear();

        Member findMember = jpaQueryFactory
                .selectFrom(member)
                .join(member.team,team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();


    }


    /**나이가 가장 많은 회원 조회*/
    @Test
    @DisplayName("서브 쿼리")
    void subQuery() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = jpaQueryFactory.selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub)
                )).fetch();

        assertThat(result).extracting("age").containsExactly(40);
    }

    @Test
    @DisplayName("나이가 평균 이상인 회원 조회")
    void subQuery2() throws Exception {
        QMember memberSub = new QMember("memberSub");
        // 주의 !!!!!!!!!!!! 서브쿼리안에 기존의 member사용하지 않기
        List<Member> result = jpaQueryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();
        //25
        assertThat(result).extracting("age")
                .containsExactly(30, 40);


    }

    /***10살 제외 in절 사용*/
    @Test
    @DisplayName("서브쿼리 in절 사용 ")
    void inSub() throws Exception {

        QMember memberSub = new QMember("memberSub");

        List<Member> result = jpaQueryFactory
                .selectFrom(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(20, 30, 40);

    }

    @Test
    @DisplayName("case문 ")
    void caseExcercise() throws Exception {
        List<String> result = jpaQueryFactory
                .select(member.age
                .when(10).then("10살")
                .when(20).then("20살")
                .otherwise("기타"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    @DisplayName("문자 더하기 concat")
    void concat() throws Exception {
        List<String> list = jpaQueryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()).concat("살"))
                .from(member)
                .fetch();

        for (String s : list) {
            System.out.println("member = " + s);
        }
    }
    
    @Test
    @DisplayName("튜플")
    void getTuple () throws Exception {

        List<Tuple> result = jpaQueryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String userName = tuple.get(member.username);
            Integer userAge = tuple.get(member.age);
            System.out.println("userName = " + userName);
            System.out.println("userAge = " + userAge);
        }
    }

    @Test
    @DisplayName("jpql dto")
    void findtoByJpql () throws Exception {

        List<MemberResponse> memberResponseList =
                em.createQuery("select new com.spring.jpadata.dto.MemberResponse(m.username,m.age)" +
                " from Member m", MemberResponse.class).getResultList();

        for (MemberResponse memberResponse : memberResponseList) {
            System.out.println("memberResponse = " + memberResponse);
        }
    }

    @Test
    @DisplayName("쿼리디에스엘 dto setter접근")
    void dto() throws Exception {
        //dto로 조회
        List<MemberResponse> result = jpaQueryFactory.select(
                Projections.bean(MemberResponse.class,
                        member.username,
                        member.age)
        ).from(member)
                .fetch();

        for (MemberResponse memberResponse : result) {
            System.out.println("memberResponse = " + memberResponse);
        }

    }

    @Test
    @DisplayName("쿼리디에스엘 dto 필드접근")
    void dtoByField() throws Exception {
        //dto로 조회
        List<MemberResponse> result = jpaQueryFactory.select(
                Projections.fields(MemberResponse.class,
                        member.username,
                        member.age)
        ).from(member)
                .fetch();

        for (MemberResponse memberResponse : result) {
            System.out.println("memberResponse = " + memberResponse);
        }

    }


    @Test
    @DisplayName("쿼리디에스엘 dto생성자접근")
    void dtoByConstructer() throws Exception {
        //dto로 조회
        List<MemberResponse> result = jpaQueryFactory.select(
                Projections.constructor(MemberResponse.class,
                        member.username,
                        member.age)
        ).from(member)
                .fetch();

        for (MemberResponse memberResponse : result) {
            System.out.println("memberResponse = " + memberResponse);
        }
    }

    @Test
    @DisplayName("@QueryProjection")
    void dtoByAnnotaion() throws Exception {

        //컴파일시 에러를 잡아줄 수 있다.
        List<MemberResponse> memberResponseList = jpaQueryFactory
                .select(new QMemberResponse(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberResponse memberResponse : memberResponseList) {
            System.out.println("memberResponse = " + memberResponse);
        }
    }

    @Test
    @DisplayName("@BooleanBulider 사용 동적쿼리")
    void dynamicQuery_BooleanBuilder() throws Exception {
        //검색 조건
        String usernameParam = "member1";
        Integer ageParam = null;

        List<Member> members = searchMember1(usernameParam, ageParam);

        assertThat(members.size()).isEqualTo(1);


    }

    private List<Member> searchMember1(String usernameCond,Integer ageCond) {
        BooleanBuilder builder = new BooleanBuilder();
        //usernameCond 에 값이 있으면 BooleanBuilder에 and 조건을 넣어 준 것이다.
        if (usernameCond != null) {
            builder.and(member.username.eq(usernameCond));
        }
        if (ageCond != null) {
            builder.and(member.age.eq(ageCond));
        }

        return jpaQueryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }


    @Test
    @DisplayName("Where 다중 파라미터 사용 ")
    void dynamicQuery_WhereParam() throws Exception {

        //검색 조건
        String usernameParam = "member2";
        Integer ageParam = 20;
        List<Member> members = searchMember2(usernameParam, ageParam);

        assertThat(members.size()).isEqualTo(1);


    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        return jpaQueryFactory
                .selectFrom(member) // where에 null이 있다면 그냥 무시가된다. 그래서 동적쿼리가 작용하는것
                .where(usernameEq(usernameCond),ageEq(ageCond)) // allEq(usernameCond,ageCond)
                .fetch();
    }


    //장점 : 조립가능
    private BooleanExpression allEq(String usernameCond, Integer ageCond) {
        return usernameEq(usernameCond).and(ageEq(ageCond));
    }

    private BooleanExpression usernameEq(String usernameCond) {
        //usernameCond가 null이면 null을 반환
        return usernameCond != null ? member.username.eq(usernameCond) : null;
    }

    private BooleanExpression ageEq(Integer ageCond) {
        if(ageCond == null) return null;
        return member.age.eq(ageCond);
    }

    @Test
    @DisplayName("벌크 연산")
    void bulk() throws Exception {
        long count = jpaQueryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();

        //벌크연산은 디비와 영속성 컨텍스트가 안맞기 때문에 ->영속성 컨텍스트를 꼭 초기화해주자!
        em.flush(); // 영속성 컨텍스트에 있는걸 다 보내고
        em.clear(); // 영속성 컨텍스트를 지우자

        //디비와 영속성 컨텍스트를 확인하기 위함
        List<Member> result = jpaQueryFactory.selectFrom(member)
                .fetch();

        for (Member mem : result) {
            System.out.println("mem = " + mem);
        }
    }


    @Test
    @DisplayName("더하기 곱하기")
    void bulkAdd() throws Exception {
        //빼기라면 member.age.add(-1)
        //곱하기하면 member.age.multiply(2);

        jpaQueryFactory
                .update(member)
                .set(member.age, member.age.add(-1))
                .execute();
    }

    @Test
    @DisplayName("삭제")
    void delete() throws Exception {
        long count = jpaQueryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();

        System.out.println("count = " + count);


    }

}
