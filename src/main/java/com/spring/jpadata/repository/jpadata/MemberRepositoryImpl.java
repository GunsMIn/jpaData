package com.spring.jpadata.repository.jpadata;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpadata.dto.MemberSearchCondition;
import com.spring.jpadata.dto.MemberTeamDto;
import com.spring.jpadata.dto.QMemberTeamDto;
import com.spring.jpadata.entity.Member;
import com.spring.jpadata.entity.QMember;
import com.spring.jpadata.entity.QTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.spring.jpadata.entity.QMember.*;
import static com.spring.jpadata.entity.QTeam.*;
import static org.springframework.util.StringUtils.*;

// + Impl를 꼭 맞추어주자!
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {

        List<MemberTeamDto> memberTeamDtoList = queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .where(
                        userNameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()))
                .leftJoin(member.team, team)
                .fetch();

        return memberTeamDtoList;
    }

    //fetchResult() 사용
    @Override
    public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable) {

        QueryResults<MemberTeamDto> results = queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .where(
                        userNameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()))
                .leftJoin(member.team, team)
                .offset(pageable.getOffset()) // 몇 번째
                .limit(pageable.getPageSize()) // 몇 개
                .fetchResults();

        List<MemberTeamDto> memberTeamDtoList = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(memberTeamDtoList, pageable,total);
    }

    /**데이터의 내용과 전체 count를 별도로 조회하는 메소드*/
    @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {
        //content
        List<MemberTeamDto> memberTeamDtoList = queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .where(
                        userNameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()))
                .leftJoin(member.team, team)
                .offset(pageable.getOffset()) // 몇 번째
                .limit(pageable.getPageSize()) // 몇 개
                .fetch();


        JPAQuery<Member> countQuery = queryFactory
                .select(member)
                .from(member)
                .where(
                        userNameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()))
                .leftJoin(member.team, team);

        /*  count 쿼리가 생략 가능한 경우 생략해서 처리🔽
            페이지 시작이면서 컨텐츠 사이즈가 페이지 사이즈보다 작을 때
            마지막 페이지 일 때 (offset + 컨텐츠 사이즈를 더해서 전체 사이즈 구함*/
        return PageableExecutionUtils.getPage(memberTeamDtoList, pageable, () -> countQuery.fetchCount());

    }


    private BooleanExpression userNameEq(String username) {
        if (hasText(username)) {
            return member.username.eq(username);
        }
        return null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        if (hasText(teamName)) {
            return team.name.eq(teamName);
        }
        return null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }

}
