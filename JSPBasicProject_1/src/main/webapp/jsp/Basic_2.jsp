<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8"%>
<%--
   113
   ----
   1. 자바/HTML의 분리
   = 자바를 코딩하는 방법 *스크립트릿
     1) <% %> : 스크립트릿
         일반 메소드 안에 코딩 => 변수(지역변수), 메소드 호출, 제어문, 연산자
            => _jspService()안에 첨부
            => 자바와 동일
            => ;
            => <% 
                  여기서 주석
                  /* 자바만주석
                  */
               %>
     2) <%! %> : 선언문 => 메소드 제작, 멤버변수
          => 클래스 영역에 설정
     3) out.write() => 자바데이터 출력
        ===========
          String name="";
          <%= name%> : => out.write(=====);
          

     1. JSP => 데이터 읽기
               => 데이터베이스 연동
     2. HTML 제작
     3. 필요한 위치에 for/if를 이용해서 화면 출력
     4. CSS를 적용
     5. 동적으로 변경 : JavaScript
--%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>