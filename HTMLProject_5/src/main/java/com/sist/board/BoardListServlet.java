package com.sist.board;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sist.dao.*;
/**
 * Servlet implementation class BoardListServlet
 */
@WebServlet("/BoardListServlet")
public class BoardListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       // GET / POST => 통합 (service())
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// HTML로 변환 => 브라우저 전송
		response.setContentType("text/html;charset=UTF-8");
		/*
		 *  request (요청) => 사용자가 보낸 값
		 *     => getParameter("태그의 name속성")
		 *  response (응답)
		 *     => 어떤 형태로 실행
		 *        html,xml,json
		 *             === ====
		 *                  text/plain => VueJS,ReactJS
		 *             text/xml => AJAX
		 *        === text/html
		 *     => setContentType()
		 *     => sendRedirect() => 화면이동
		 *        ============== GET
		 *     => 화면 전송
		 *        ======= <a> : GET
		 *        ======= <form> : GET/POST
		 *  
		 */
		/*
		 *  데이터를 전송
		 *  => 전송없이 수행 => GET (주로 HTML 화면 출력)
		 *  => GET => DEFAULT => doGet
		 *  
		 *  <input> = 사용자가 입력해서 서버로 전송
		 */
		//전송해야되는 브라우저 설정
		PrintWriter out=response.getWriter();
		//사용자로부터 페이지를 받기 => request
		//BoardListServlet?page=1 => GET(단순한건 get방식,보안필요x)
		String page=request.getParameter("page");
		if(page==null) // 페이지를 안넘겼을때
			page="1";
		int curpage=Integer.parseInt(page);
		
		// servlet => jsp
		// jsp=실행시에 servlet으로 변경
		//데이터 받기
		BoardDAO dao=BoardDAO.newInstance();
		List<BoardVO> list=dao.boardListData(curpage);
		int totalpage=dao.boardTotalPage();
		// HTML
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=table.css>"); //link = css를 불러오는 태그
		out.println("</head>");
		out.println("<body>");
		out.println("<center>");
		out.println("<h1>자유게시판</h1>");
		/*
		 *  태그 (106page)
		 *  ---
		 *  inline 속성
		 *   <span> : 가로 분할 (부분적으로 css사용)
		 *   <input>, <img>, <a>, 글자 태그 : <b>,<font>
		 *   옆으로 출력 ----- ---- ----
		 *  block 속성
		 *   <div> : 세로 분할 => layout
		 *   <h1>~<h6>
		 *   목록 출력 : <table>, <ul>, <ol>
		 *   
		 *   <table>
		 *      |
		 *    <tr>
		 *      |
		 *    ------
		 *    |    |
		 *   <th>  <td>
		 *   
		 */
		out.println("<table class=table_content width=800>");
		out.println("<tr>");
		out.println("<td>");
		out.println("<a href=BoardInsertServlet>새글</a>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		
		out.println("<table class=table_content width=800>");
		out.println("<tr height=50>");
		out.println("<th width=10% align=center>번호</th>");
		out.println("<th width=45% align=center>제목</th>");
		out.println("<th width=15% align=center>이름</th>");
		out.println("<th width=20% align=center>작성일</th>");
		out.println("<th width=10% align=center>조회수</th>");
		out.println("</tr>");
		for(BoardVO vo:list)
		{
			out.println("<tr height=50>");
			out.println("<td width=10% align=center>"+vo.getNo()+"</td>");
			out.println("<td width=45%><a href=BoardDetailServlet?no="+vo.getNo()+">"+vo.getSubject()+"</a></td>");
			out.println("<td width=15% align=center>"+vo.getName()+"</td>");
			out.println("<td width=20% align=center>"+vo.getRegdate()+"</td>");
			out.println("<td width=10% align=center>"+vo.getHit()+"</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		out.println("<table class=table_content width=800>");
		out.println("<tr>");
		out.println("<td align=left>");
		out.println("<select name=fs>");
		out.println("<option value=name>이름</option>");
		out.println("<option value=subject>제목</option>");
		out.println("<option value=content>내용</option>");
		out.println("</select>");
		out.println("<input type=text name=ss sixe=20>");
		out.println("<input type=button value=검색>");
		out.println("</td>");
		out.println("<td align=right>");
		out.println("<a href=BoardListServlet?page="+(curpage>1?curpage-1:curpage)+">이전</a>&nbsp;");
		out.println(curpage+" page / "+totalpage+" pages");
		out.println("<a href=BoardListServlet?page="+(curpage<totalpage?curpage+1:curpage)+">다음</a>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</center>");
		out.println("</body>");
		out.println("</html>");

		
	}

}
