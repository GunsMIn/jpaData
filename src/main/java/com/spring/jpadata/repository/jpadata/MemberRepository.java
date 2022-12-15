package com.spring.jpadata.repository.jpadata;

import com.spring.jpadata.dto.MemberDto;
import com.spring.jpadata.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

            /**여기서 중요한 점 ! 인터페이스는 여러개를 상속 받을 수 있다.*/
public interface MemberRepository extends JpaRepository<Member,Long> ,MemberRepositoryCustom{
    //<타입,pk매팽타입>
    //쿼리 메소드
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //@Query 쿼리
    //쿼리메소드에서 파라미터가 길어지면 너무 가독성이 떨어질수도있다.
    //메소드 이름이 복잡해진다 . 그러면 쿼리를 직접정의해서 사용하고 메소드이름을 단순하게 사용하자!
    @Query("select m from Member m where m.username =:username and m.age=:age")
    List<Member> findUser(@Param("username") String username,@Param("age") int age);


    //이름만 가지고오고싶을때
    @Query("select m.username from Member m")
    List<String> findUsername();

    //dto로 조회할 때는 new operation을 사용해야한다
    @Query("select new com.spring.jpadata.dto.MemberDto(m.id,m.username,t.name)" +
            "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    //컬렉션 파라미터 바인딩 -> in절 사용
    @Query("select m from Member m where m.username in :names") // -> 이렇게 하면 컬렉션을 파라미터 바인딩 가능!
    List<Member> findByNames(@Param("names") Collection<String> names);


    @Modifying(clearAutomatically = true) //@Modifying 이것이 jpa에서 .excuteUpdate() 를 해준다.
    @Query("update Member m set m.age = m.age+1 where m.age >=:age")
    int bulkAgePlus(@Param("age") int age);

    //clearAutomatically = true 속성은 벌크성 쿼리가 실행한다음에  em.clear()를 해준다.(영속성 컨텍스트 clear)

    //유연한 반환 타입
    List<Member> findByUsername(String username); // 컬렉션 반환

    //Member findByUsername(String username); // 단건조회

    Optional<Member> findOptionalByUsername(String name); // Optional 반환

}
