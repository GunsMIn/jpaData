# ✨Spring_STUDY
✔jpaData를 이용한 프로젝트  레파지토리입니다.✔
![image](https://user-images.githubusercontent.com/104709432/198950229-e233789c-39a7-4005-b934-d6e7a21f1b0f.png)
![querydsl](https://user-images.githubusercontent.com/104709432/207634404-5ee0d368-0432-4ec2-ad04-9e0ad7562a89.jpg)

<br>
<img src="https://img.shields.io/badge/Java-E34F26?style=flat&logo=Java&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=Spring Boot&logoColor=white"/></a>
<img src="https://img.shields.io/badge/JUnit5-25A162?style=flat&logo=JUnit5&logoColor=white"/></a>
<hr>
<b>📝파일명 : 프로젝트 진행 내용</b><br>
- MemberRepository 안터페이스: JpaRepository<>를 상속받은 interface가 jpaData repository이다  <br>
- JpaRepository 인터페이스: 공통 CRUD 제공  제네릭은 <엔티티 타입, 식별자 타입> 설정 <br>
<BR><BR><BR>

## 스프링 데이터 JPA 기본메소드<BR>
  <summary>save(S)</summary> : 새로운 엔티티는 저장하고 이미 있는 엔티티는 병합한다. <br>
  <summary>delete(T)</summary> : 엔티티 하나를 삭제한다. 내부에서 EntityManager.remove() 호출 <br>
  <summary>findById(ID)</summary> : 엔티티 하나를 조회한다. 내부에서 EntityManager.find() 호출 <br>
  <summary>getOne(ID)</summary> : 엔티티를 프록시로 조회한다. 내부에서 EntityManager.getReference() 호출 <br>
  <summary>findAll(…)</summary> : 모든 엔티티를 조회한다. 정렬( Sort )이나 페이징( Pageable ) 조건을 파라미터로 제공할 수
있다 <br>
 <summary>save(S)</summary> : 새로운 엔티티는 저장하고 이미 있는 엔티티는 병합한다. <br>

<BR><BR><BR>
## @Query로 DTO조회하기
![image](https://user-images.githubusercontent.com/104709432/198951170-dc62477d-2dc3-435c-983f-a2c79655c89b.png)
<BR><BR><BR>
## 스프링 데이터 JPA가 제공하는 쿼리 메소드 기능
<summary>조회:</summary> find…By ,read…By ,query…By get…By, 
예:) findHelloBy 처럼 ...에 식별하기 위한 내용(설명)이 들어가도 된다.
<summary>COUNT</summary>: count…By 반환타입 long
<summary>EXISTS</summary>: exists…By 반환타입 boolean
<summary>삭제</summary>: delete…By, remove…By 반환타입 long
<summary>DISTINCT</summary>: findDistinct, findMemberDistinctBy
<summary>LIMIT</summary>: findFirst3, findFirst, findTop, findTop3

<BR><BR><BR>
## 벌크연산 쿼리 메소드
![image](https://user-images.githubusercontent.com/104709432/199170208-3ddb81b8-1c9c-4225-bb6e-15285c9216f2.png)
<summary>@Modifying: </summary>em.excuteUpdate()를 실행해주는 어노테이션 
<summary>벌크 연산의 주의점: </summary>벌크 연산은 영속성 컨텍스트를 무시하고 실행하기 때문에, 영속성 컨텍스트에 있는 엔티티의 상태와
DB에 엔티티 상태가 달라질 수 있다. 따라서 벌크연산을 한 것에서 조회를 하고싶다면 반드시 em.clear를해주어야한다. 
em.clear를 대신해주는것은  @Modifying(clearAutomatically = true)이다.

<BR><BR><BR>
## 패치 조인(Lazy연관관계의 N+1 문제 해결)
<summary><b>JPQL에서의 FETCH JOIN:</b> </summary> 예를 들어서 MEMBER 클래스와 TEAM클래스가 @ManyToOne 다 : 1 관계이다. 그러면 lazy로 설정했을것이다.<br>
왜냐하면  쿼리 n+1 문제가 발생하기 때문이다. 이것을 해결하기위해서는 첫번째로 jpql로 다음과같이 해결할수있다<br>
@Query("select m from Member m left join fetch m.team")<br>
이 어노테이션을 사용하여 member를 조회할 때 team을 한번에 같이 조회하게된다.<br> jpql의 문법이 복잡해지면 @EntiityGraph라는 어노테이션을 사용하면 더 편리한데 다음과 같다.
스프링 데이터 JPA는 JPA가 제공하는 엔티티 그래프 기능을 편리하게 사용하게 도와준다. 이 기능을
사용하면 JPQL 없이 페치 조인을 사용할 수 있다.
<img width="1350" alt="캡처" src="https://user-images.githubusercontent.com/104709432/199195665-4bd7e872-8cfa-45c7-8ed6-044265af0414.PNG">

  
<hr>
<b>🎈학습 내용</b><br>
<b>-tdd시 주의점</b> :  언제 실행해도 동일한 결과가 나오게끔 구성해야함. 또한 결과값이 없을때는 해당 exception을 확인하는 Assertions.assertTrows메소드를 사용해야한다
     각각의 테스트에서 공통으로 사용하는 부분은 메소드 위에 @BeforEach를 붙여주어서 @Test가 실행되기전에 각각 실행되게 만들어준다..<br>
