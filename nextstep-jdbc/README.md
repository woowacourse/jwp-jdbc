# 나만의 JDBC 라이브러리 구현

> JDBC - Java DataBase Connectivity

## JDBC 라이브러리 요구사항

* JDBC에 대한 공통 라이브러리를 만들어 개발자가 다음 3가지 구현에만 집중하도록 해야 한다.
    * SQL 쿼리
    * 쿼리에 전달할 인자
    * SELECT 구문의 경우 조회한 데이터를 추출
    
* SQLException을 런타임 Exception으로 변환해 try/catch 절로 인해 소스 코드의 가독성을 헤치지 않도록 해야 한다.

## JDBC 라이브러리 & 개발자가 구현할 부분

| 작업 |	JDBC 라이브러리 |	개발자가 구현할 부분 |
|---|:---:|:---:|
|Connection 생성 및 close | O | X |
|SQL 문	| X | O |
|Statement 생성 및 close | O | X |
|ResultSet 생성 및 close | O | X |
|SQL 문에 전달할 값 | X | O |
|ResultSet에서 데이터 추출 | X | O |
|SQL 문에 인자 setting | O | X |
|트랜잭션 관리 | O | X |

## 라이브러리 구현 미션 단계

* 1단계
    * 리팩토링 과정에서 테스트는 slipp 모듈 > src/test/java에 있는 slipp.dao.UserDaoTest 클래스를 활용
    * 앞의 JDBC 실습 과정에 있는 회원목록, 개인정보수정 실습을 진행하지 않으면 UserDaoTest는 실패
    * 테스트 코드가 성공하도록 회원목록과 개인정보수정 실습을 진행한 후 UserDao에 대한 리팩토링 실습을 진행
    
* 2단계
    * UserDao에서 발생하는 중복을 제거하기 위한 라이브러리는 nextstep-jdbc 모듈 > src/main/java에 있는 nextstep.jdbc.JdbcTemplate 에 구현

## 미션 진행 중 요구사항

* 라이브러리를 구현하기 위해 리팩토링을 진행하는 모든 과정에서 컴파일 에러가 발생하지 않도록 연습