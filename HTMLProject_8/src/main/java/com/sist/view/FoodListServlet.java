package com.sist.view;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// Servlet => Server + let
// let => 가벼운 프로그램 => Applet / Midlet => 저장되는 메모리를 최소화
/*
   Servlet => 장점은 보안이 좋다 => .java가 아닌 .class을 배포
              단점 : HTML작성이 복잡하다 ( 문자열로 만들어야 해서 - out.write() )
                    => 수정시마다 컴파일해야 된다
              => HTML을 사용하지 않는다 (우리가 아직 JSP 안들어갔기 때문에 사용중)
              => 자바와 HTML을 연결 해주는 중계 역할이다 원래
              => JSP + Servlet 연동
   JSP => 사용이 편리 , 보안이 취약 => .jsp(그대로)
                 => 컴파일 없이 실행 => 스크립트
   M V C - Controller - Servlet
     - view => JSP
   Spring => 처리 => 웹 => Servlet
   ==============================
   Servlet 에서 연습할 부분들
    1. 페이징 (블록)
    2. Cookie
    3. Session
    4. 요청 => 응답 (주고 받기)
    
    HTML : 정적 (화면 UI만 출력)
     => 서버로 데이터 전송
        GET / POST / PUT / DELETE => RestFul (전송하는 방식)
         |     | => id,pwd,데이터가 많은 경우 => <form>
        URL?데이터 
   GET : 노출이 심하다 / 주로 단순 데이터 전송
                      페이지 / 번호 / 검색어 ...
         => 전송이 없는 경우 : GET이 default (시작할 때 항상 GET으로 화면 출력)
         
         JavaScript
         ==========
         ajax({
            type:POST
            url:사이트주소
            success:function(result)
            {
            }
         })
         Vue,React
         axios.get...then() -> 값 받는 것 => 주고 받고 동시에 해서 화면 깜빡이지 않는다
         axios.post
    ========================= + CSS로 모양잡는 
*/
import java.util.*;
import com.sist.dao.*;
@WebServlet("/FoodListServlet")
public class FoodListServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      // 1. 전송방식 (HTML,XML) => 브라우저에 통보
      response.setContentType("text/html;charset=UTF-8");
      // 2. 브라우저에서 읽어갈 메모리 위치를 확보 => out
      PrintWriter out=response.getWriter(); // out 에 올려주면 브라우저에서 읽어간다
      // 3. 사용자의 요청값을 받는다 
      String page=request.getParameter("page");
      if(page==null)
         page="1"; // default값 안만들어 놓으면 에러 발생
      int curpage=Integer.parseInt(page);
      // 4. 데이터베이스 연동 => 요청한 데이터를 가지고 온다 
      FoodDAO dao=FoodDAO.newInstance(); // 싱글턴 객체 생성
      List<FoodVO> list=dao.foodListData(curpage); // 1page. 이따 매개변수 page 받아서 요청받을 예정
      int totalpage=dao.foodTotalPage();
      // 쿠키 읽기
      List<FoodVO> cList=new ArrayList<FoodVO>();
      Cookie[] cookies=request.getCookies();
      if(cookies!=null)
      {
         for(int i=cookies.length-1;i>=0;i--)
         {
            if(cookies[i].getName().startsWith("food"))
            {
               String fno=cookies[i].getValue();
               FoodVO vo=dao.foodDetailData(Integer.parseInt(fno));
               cList.add(vo);
            }
         }
      }
            
      // 블록 나누기 **
      final int BLOCK=10;
      int startPage=((curpage-1)/BLOCK*BLOCK)+1; 
      // 1 => 현재 페이지 => 1~10
      //            ==> (1-1)/10*10 = 0 이니깐 뒤에 +1 해줘서 스타트페이지 1
      //            ==> 11-1/10*10+1 => 11
      int endPage=((curpage-1)/BLOCK*BLOCK)+BLOCK;
      // 10 (curpage가 2이더라도 1/BLOCK(=10) => 0 => 0*BLCOK => 0 +BLOCK해서 10
      
      if(endPage>totalpage) // 해당 페이지의 마지막 페이징 숫자가 진짜 totalpage보다 크면 totalpage가 endpage로 출력 (endPage는 50인데 실제 totalpage가 47이면 endpage 47) 
         endPage=totalpage; // 마지막 페이징은 수가 안맞을 수 있으니 이 코드 작성 
      // 5. 데이터를 HTML를 이용해서 출력
      out.write("<html>");
      out.write("<head>"); // CSS / JavaScript 가 없는 경우 생략 해도 된다
      out.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
      out.write("<style type=text/css>");
      out.write(".container{margin-top:50px}");
      // margin:10px 10px 10px 10px 
      //        top right bottom left
      out.write(".row{margin:0px auto;width:900px}"); // auto -> 가운데 정렬
      out.write("</style>");
      out.write("</head>");
      out.write("<body>"); // 화면 UI
      out.write("<div class=container>"); // container : 전체 영역 만들어주는 것 (wrapper 도 비슷)
      out.write("<div class=row>"); // 맛집 이미지. 내용이 달라질 떄 영역 구분 = row
      for(FoodVO vo:list)
      {
         out.write("<div class=\"col-md-3\">"); // 3줄. rowSize=12 로 해놨기 때문
         out.write("<div class=\"thumbnail\">");
         out.write("<a href=FoodBeforeServlet?fno="+vo.getFno()+">");
         out.write("<img src="+vo.getPoster()+" alt=\"Lights\" style=\"width:100%\">");
         out.write("<div class=\"caption\">");
         out.write("<p>"+vo.getName()+"</p>");
         out.write("</div>");
         out.write("</a>");
         out.write("</div>");
         out.write("</div>");
      }
      out.write("</div>");
      out.write("<div class=row>"); // 페이지 출력
      out.write("<div class=text-center>");
      out.write("<ul class=\"pagination\">");
//      out.write("<li><a href=\"#\">&lt;</a></li>");
//      out.write("<li><a href=\"#\">1</a></li>");
      if(startPage>1) // stratPage => 1,11,21... startPage=1 이면 &lt; 출력X
      {
         out.write("<li><a href=FoodListServlet?page="+(startPage-1)+">&lt;</a></li>");
          // 11page 에서 이전 누르면 => 10 선택되게 
      }
      // startPage 1,11,21 / endPage 10,20,30
      for(int i=startPage;i<=endPage;i++)
      {                          // 현재페이지면 파랑색 출력 active
         out.write("<li "+(i==curpage?"class=active":"")+"><a href=FoodListServlet?page="+i+">"+i+"</a></li>");
      }
      if(endPage<totalpage) // endPage 해당 페이징 중 마지막 번호 10,20,30...
      {
         out.write("<li><a href=FoodListServlet?page="+(endPage+1)+">&gt;</a></li>");
      }
//      out.write("<li><a href=\"#\">&gt;</a></li>");
      out.write("</ul>");
      out.write("</div>");
      out.write("</div>");
      out.write("<div class=row>"); // 최근 방문
      if(cList.size()!=0)
      {
         for(FoodVO vo:cList)
         {
            out.write("<a href=FoodDetailServlet?fno="+vo.getFno()+">");
            out.write("<img src="+vo.getPoster()+" style=\"width:100px;height:100px;margin-top:10px;margin-left:5px\">");
            out.write("</a>");
         }
      }
      else
      {
         out.write("<h3>방문한 기록이 없습니다</h3>");
      }
      out.write("</div>");
      out.write("</div>");
      out.write("</body>");
      out.write("</html>");
      
   }

}