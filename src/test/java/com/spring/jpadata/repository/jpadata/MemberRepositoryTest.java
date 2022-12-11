/*
package com.spring.jpadata.repository.jpadata;

import com.spring.jpadata.dto.MemberDto;
import com.spring.jpadata.entity.Member;
import com.spring.jpadata.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberJpaRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void basicCRUD() {
        //생성
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //단건 조회
        Optional<Member> memberByIdOptional1 = memberJpaRepository.findById(1L);
        Optional<Member> memberByIdOptional2 = memberJpaRepository.findById(2L);
        Member memberById1 = memberByIdOptional1.orElseGet(() -> new Member("존재 회원 없음"));
        Member memberById2  = memberByIdOptional2.orElse(new Member("존재 회원 없음"));

        assertThat(member1).isEqualTo(memberById1);
        assertThat(member2).isEqualTo(memberById2);

        //리스트 조회
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        //count 검증
        Long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        Long deleteCount = memberJpaRepository.count();
        assertThat(deleteCount).isEqualTo(0);
    }


    @Test
    void greatThan(){
        Member member1 = new Member("kim", 19);
        Member member2 = new Member("kim", 20);
        Member member3 = new Member("kim", 21);
        Member member4 = new Member("kim", 22);
        Member member5 = new Member("kim", 17);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);
        memberJpaRepository.save(member5);
        List<Member> overNineteen = memberJpaRepository.findByUsernameAndAgeGreaterThan("kim", 19);

        assertThat(overNineteen.size()).isEqualTo(3);
    }


    @Test
    @DisplayName("@Query, 리포지토리 메소드에 쿼리 정의하기")
    void findNameAndAgeTest() {
        Member member1 = new Member("kim", 19);
        Member member2 = new Member("kim", 20);
        Member member3 = new Member("kim", 21);
        Member member4 = new Member("kim", 22);
        Member member5 = new Member("kim", 17);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);
        memberJpaRepository.save(member5);

        List<Member> kimand22 = memberJpaRepository.findUser("kim", 22);
        assertThat(kimand22.get(0).getUsername()).isEqualTo("kim");
        assertThat(kimand22.get(0).getAge()).isEqualTo(22);
        assertThat(kimand22.get(0)).isEqualTo(member4);

    }

    @Test
    @DisplayName("dto로 조회")
    void dto() {
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        Team team3 = new Team("team3");

        teamRepository.save(team1);
        teamRepository.save(team2);
        teamRepository.save(team3);

        Member member1 = new Member("lee", 19,team1);
        Member member2 = new Member("kim", 20,team2);
        Member member3 = new Member("kim", 21,team3);
        Member member4 = new Member("kim", 22,team1);
        Member member5 = new Member("kim", 17,team2);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);
        memberJpaRepository.save(member5);

        List<MemberDto> memberDtos = memberJpaRepository.findMemberDto();

        assertThat(memberDtos.size()).isEqualTo(5);
        assertThat(memberDtos.get(2).getTeamname()).isEqualTo("team3");
        assertThat(memberDtos.get(1).getTeamname()).isEqualTo("team2");
        assertThat(memberDtos.get(0).getUsername()).isEqualTo("lee");


        for (MemberDto memberDto : memberDtos) {
            System.out.println("memberDto = " + memberDto);
        }
    }


    @Test
    @DisplayName("@Query,컬렉션을 파라미터로 넘기기 테스트")
    void collectionParameter() {
        Member member1 = new Member("kim", 19);
        Member member2 = new Member("lee", 20);
        Member member3 = new Member("park", 21);
        Member member4 = new Member("kim", 22);
        Member member5 = new Member("kim", 17);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);
        memberJpaRepository.save(member5);



        List<Member> byNames = memberJpaRepository.findByNames(Arrays.asList("kim", "park"));
        assertThat(4).isEqualTo(byNames.size());
        assertThat(19).isEqualTo(byNames.get(0).getAge());
    }

   */
/* @Test
    @DisplayName("jpa 페이징처리 테스트")
    public void paging() {
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));
        int age = 10;

        //spring data jpa 페이징처리는 index0이 1페이지이다
        PageRequest pageRequest =
                PageRequest.of(0,3,Sort.by(Sort.Direction.DESC,"username"));//0페이지에서 3개가져와

        Page<Member> page = memberJpaRepository.findByAge(age, pageRequest);
        //-->눈에 보이지는 않지만 totalCount 쿼리까지 날려준다
        //엔티티는 api에서 노출되면 안되기때문에! 페이지를 유지하면서 dto로 반환
        Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        //페이지에서 실제로 꺼내오고 싶을때
        List<Member> content = page.getContent();
        //totalcount
        long totalCount = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);//페이지 번호 가져옴
        assertThat(page.getTotalPages()).isEqualTo(2); // member는 5명인데 3명씩나누니까 총페이지는 2개
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }*//*



    @Test
    @DisplayName("jpa 페이징처리 테스트")
    public void pagingSlice() {
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));
        int age = 10;

        //spring data jpa 페이징처리는 index0이 1페이지이다
        PageRequest pageRequest =
                PageRequest.of(0,3,Sort.by(Sort.Direction.DESC,"username"));//0페이지에서 3개가져와

        Slice<Member> page = memberJpaRepository.findByAge(age, pageRequest);
        //Slice (count X) 추가로 limit + 1을 조회한다.
        // 그래서 다음 페이지 여부 확인(최근 모바일 리스트생각해보면 됨)

        //페이지에서 실제로 꺼내오고 싶을때
        List<Member> content = page.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);//페이지 번호 가져옴
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    @DisplayName("벌크성 쿼리")
    public void bulk() {
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 20));
        memberJpaRepository.save(new Member("member3", 21));
        memberJpaRepository.save(new Member("member4", 25));
        memberJpaRepository.save(new Member("member5", 40));

        int result = memberJpaRepository.bulkAgePlus(20);
       */
/* em.flush(); // 남아있는게 디비에 반영이되는거고
        em.clear();// 영속성 컨텍스트의 데이터를 완전히 날려버린다.*//*


        assertThat(result).isEqualTo(4);

        Optional<Member> memberJpaRepositoryById = memberJpaRepository.findById(5L);
        Member member = memberJpaRepositoryById.get();
        assertThat(member.getAge()).isEqualTo(41);
    }


    @Test
    @DisplayName("Lazy fetch N+1해결방법")
    public void findMemberLazy() {
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberJpaRepository.save(new Member("member1", 10, teamA));
        memberJpaRepository.save(new Member("member2", 20, teamB));

        em.flush();
        em.clear(); // 영속성 컨텍스트를 깔끔하게 지우기위함

        //한번에 다끌고온다.
        List<Member> members = memberJpaRepository.findJpqlGraph();

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.team = " + member.getTeam().getName());
        }


    }
}*/
