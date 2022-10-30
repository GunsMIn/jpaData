package com.spring.jpadata.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity @Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={"id","username","age"}) //@ToString은 가급적 내부 필드만(연관관계 없는 필드만!
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private Integer age;

    //member는 team 하나에만 소속될 수 있겠지? 그래서 @ManyToOne
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }
    public Member(String username,Integer age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, Integer age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) { // team이 null일때는 무시하려고!
            changeTeam(team);
        }
    }


    /*연관관계 메소드*/
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}