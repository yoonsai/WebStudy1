package com.sist.view;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sist.dao.*;

@WebServlet("/FoodListServlet")
public class FoodListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		// HTML을 모아서 전송 
		PrintWriter out=response.getWriter();
		// CategoryServlet에서 전송된 cno를 받는다 
		// FoodListServlet?cno=1
		String cno=request.getParameter("cno");
		// DataBase연동 
		FoodDAO dao=FoodDAO.newInstance();
		CategoryVO cvo=dao.categoryInfoData(Integer.parseInt(cno));
		out.write("<html>");// <html>
		out.write("<head>");
		// 외부 CSS => 파일에 공통 적용되는 CSS => 파일로 만들어서 처리
		out.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
		// 내부 CSS => 한개의 파일에서만 적용 
		out.write("<style type=text/css>");
		/*
		 *   bootstrap => 모든 내용 ==> class속성 
		 *   CSS의 기본 
		 *   1. * => 전체
		 *   2. 태그명 
		 *   3. id ====> 중복이 없는 태그 (한개만 변경)
		 *      #id명
		 *   4. class ===> 중복이 있는 태그 (여러개를 동시 변경)
		 *      .class명 
		 */
		out.write(".container{margin-top:50px}");
		out.write(".row{margin:0px auto;width:900px}");
		out.write("</style>");
		out.write("</head>");
		out.write("<body>");
		out.write("<div class=container>");
		out.write("<div class=jumbotron>");
		out.write("<h3 class=text-center>"+cvo.getTitle()+"</h3>");
		out.write("<h4 class=text-center>"+cvo.getSubject()+"</h4>");
		out.write("</div>");
		out.write("<div style=\"height:30px\"></div>");
		out.write("<div class=row>");
		out.write("<table class=table>");
		out.write("<tr>");
		out.write("<td>");
		// 목록 출력 위치 
		out.write("</td>");
		out.write("</tr>");
		out.write("</table>");
		out.write("</div>");
		out.write("</div>");
		out.write("</body>");
		out.write("</html>");
	}

}