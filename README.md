# ✨Spring_STUDY
✔jpaData를 이용한 프로젝트  레파지토리입니다.✔
![image](https://user-images.githubusercontent.com/104709432/198950229-e233789c-39a7-4005-b934-d6e7a21f1b0f.png)
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
- save(S) : 새로운 엔티티는 저장하고 이미 있는 엔티티는 병합한다. <br>

<BR><BR><BR>
## @Query로 DTO조회하기
![image](https://user-images.githubusercontent.com/104709432/198951170-dc62477d-2dc3-435c-983f-a2c79655c89b.png)
<hr>
<b>🎈학습 내용</b><br>
<b>-tdd시 주의점</b> :  언제 실행해도 동일한 결과가 나오게끔 구성해야함. 또한 결과값이 없을때는 해당 exception을 확인하는 Assertions.assertTrows메소드를 사용해야한다
     각각의 테스트에서 공통으로 사용하는 부분은 메소드 위에 @BeforEach를 붙여주어서 @Test가 실행되기전에 각각 실행되게 만들어준다..<br>
