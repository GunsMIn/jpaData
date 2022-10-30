# ✨Spring_STUDY
✔jpaData를 이용한 프로젝트  레파지토리입니다.✔
<hr>
<b>📝파일명 : 프로젝트 진행 내용</b><br>
- MemberRepository 안터페이스: JpaRepository<>를 상속받은 interface가 jpaData repository이다  <br>
<hr>
<b>🎈학습 내용</b><br>
<b>-tdd시 주의점</b> :  언제 실행해도 동일한 결과가 나오게끔 구성해야함. 또한 결과값이 없을때는 해당 exception을 확인하는 Assertions.assertTrows메소드를 사용해야한다
     각각의 테스트에서 공통으로 사용하는 부분은 메소드 위에 @BeforEach를 붙여주어서 @Test가 실행되기전에 각각 실행되게 만들어준다..<br>
