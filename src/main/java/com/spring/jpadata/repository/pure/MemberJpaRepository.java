package com.spring.jpadata.repository.pure;

import com.spring.jpadata.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

//순수 jpa 레퍼지토리
@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager em;

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
}
