package com.spring.jpadata.repository.jpadata;

import com.spring.jpadata.dto.MemberDto;
import com.spring.jpadata.entity.Member;
import com.spring.jpadata.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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


}