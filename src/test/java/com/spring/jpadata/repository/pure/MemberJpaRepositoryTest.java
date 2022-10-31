package com.spring.jpadata.repository.pure;

import com.spring.jpadata.entity.Member;
import com.spring.jpadata.repository.pure.MemberJpaRepository;
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
        Long count = memberJpaRepository.getCount();
        assertThat(count).isEqualTo(2);
    
        //삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        Long deleteCount = memberJpaRepository.getCount();
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
        List<Member> overNineteen = memberJpaRepository.findByUsernameAndAgeGreaterThen("kim", 19);

        assertThat(overNineteen.size()).isEqualTo(3);
    }


    @Test
    @DisplayName("jpa 페이징처리 테스트")
    public void paging() {
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        Long count = memberJpaRepository.totalCount(age);

        assertThat(members.size()).isEqualTo(3);
        assertThat(count).isEqualTo(5);

        for (Member member : members) {
            System.out.println(member);
        }

    }

}