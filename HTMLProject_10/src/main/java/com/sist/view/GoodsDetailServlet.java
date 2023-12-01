package com.sist.view;

import java.io.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sist.dao.*;
@WebServlet("/GoodsDetailServlet")
public class GoodsDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		// ISO-8859_1 
		PrintWriter out=response.getWriter();
		// 클라이언트가 보낸 데이터 받기 
		// MainServlet?mode=5&no="+vo.getNo()+"&type=1
		// ================== ========================
		//   mode => 화면변경    DetailServlet
		String no=request.getParameter("no");
		String type=request.getParameter("type");
		// DAO연동 
		GoodsDAO dao=GoodsDAO.newInstance();
		GoodsVO vo=dao.goodsDetailData(Integer.parseInt(no),
				Integer.parseInt(type));
		
		// 화면 출력 
		out.write("<html>");
		out.write("<head>");
		out.write("<link rel=\"stylesheet\" href=\"css/style.css\">");
		out.write("<script type=text/javascript src=\"http://code.javascript\">");
		out.write("<script type=text/javascript>");
		out.write("let i=0;");
		// 자바 스크립트 => 자동 변환 함수 ==> let i=0 i:int
		// let a="aaa"  a:String / let d=10.5 => d:double
		// let o={} => o:Object, let k=[] k:Array
		out.write("</head>");
		out.write("<body>");
		String html="<div class=\"row\">"
				+ "		  <table class=\"table\">"
				+ "			  <tr>"
				+ "				  <td width=\"35%\" align=\"center\" rowspan=\"9\">"
				+ "					  <img src="+vo.getPoster()+" id=\"image\">"
				+ "				  </td>"
				+ "				  <td width=\"65%\" align=\"center\">"
				+ "					  <span id=\"title\">"
				+vo.getName()
				+ "					  </span>"
				+ "				  </td>"
				+ "			  </tr>"
				+ "			  <tr>"
				+ "				  <td width=\"65%\">"
				+ "					  <span id=\"sub\">"+vo.getSub()+"</span>"
				+ "				  </td>"
				+ "			  </tr>"
				+ "			  <tr>"
				+ "				  <td width=\"65%\">"
				+ "					  <span id=\"percent\">"+vo.getDiscount()+"%</span>&nbsp;"
				+ "					  <span id=\"price\">"+vo.getPrice()+"</span>"
				+ "					  <p>"
				+ "						  <del id=\"psub\">17,900원</del>"
				+ "					  </p>"
				+ "				  </td>"
				+ "			  </tr>"
				+ "			  <tr>"
				+ "				  <td width=\"65%\">"
				+ "					  <span id=\"label\">첫구매할인가</span>"
				+ "					  <span id=\"price2\">"+vo.getFprice()+"</span>"
				+ "				  </td>"
				+ "			  </tr>"
				+ "			  <tr>"
				+ "				  <td width=\"65%\">"
				+ "					  <span id=\"star\">★★★★★</span>&nbsp;"
				+ "					  <span id=\"bold\">4.5</span>"
				+ "					  <span id=\"count\">(299)</span>"
				+ "				  </td>"
				+ "			  </tr>"
				+ "			  <tr>"
				+ "				  <td width=\"65%\">"
				+ "					  <img src=\"https://recipe1.ezmember.co.kr/img/mobile/icon_delivery3.png\">"
				+ "					  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "					  <span id=\"delevery\">"+vo.getDelivery()+"</span>"
				+ "				  </td>"
				+ "			  </tr>"
				+ "			  <tr>"
				+ "				  <td width=\"65%\">"
				+ "					  <select id=\"sel\">"
				+ "						  <option>옵션 선택</option>"
				+ "					  </select>"
				+ "				  </td>"
				+ "			  </tr>"
				+ "			  <tr>"
				+ "				  <td width=\"65%\">"
				+ "					  <input type=\"button\" value=\"장바구니\" id=\"cart\">"
				+ "					  <input type=\"button\" value=\"바로구매\" id=\"buy\">"
				+ "				  </td>"
				+ "			  </tr>"
				+ "		  </table>"
				+ "	  </div>";
		out.write(html);

		out.write("<div style=\"height:30px\"><div>");
		out.write("<div class=row>");
		// 댓글 출력
		out.write("<table class=table>");
		out.write("<tr>");
		out.write("<td>");
		ReplyDAO rdao=ReplyDAO.newInstance();
		List<ReplyVO> list=rdao.replyListData(Integer.parseInt(type),Integer.parseInt(no));
		HttpSession session=request.getSession();
		String id=(String)session.getAttribute("id"); //session.getAttribute("id")=>오브젝트라 형변환을 해줘야함
		for(ReplyVO rvo:list)
		{
			out.write("<table class=table>");
			out.write("<tr>");
			out.write("<td class=text-left>");
			out.write("◑"+rvo.getName()+"&nbsp;("+rvo.getDbday()+")");
			out.write("</td>");
			out.write("<td class=text-right>");
			if(rvo.getId().equals(id)) // 본인이면 수정 삭제 가능
			{
				out.write("<span class=\"btn btn=-xs btn-success\" data-no="+rvo.getRno()+">수정</span>&nbsp;");
				out.write("<a href=ReplyDeleteServlet?rno="+rvo.getRno()+"&type="+type+"&no="+no+" class=\"btn btn=-xs btn-info\">삭제</a>");
			}
			out.write("</td>");
			out.write("</tr>");
			
			out.write("<tr>");
			//댓글 내용
			out.write("<td colspan=2>");
			out.write("<pre style=\"white-space:pre-wrap;background-color:white;border:none\">"+rvo.getMsg()+"</pre>");
			out.write("</td>");
			out.write("</tr>");
			out.write("<tr id=m"+rvo.getRno()+" class=updates style=\"display:none\">");
			out.write("<td colspan=2>");
			
			out.write("<form method=post action=ReplyUpdateServlet>");
			out.write("<input type=hidden name=gno value="+no+">");
			out.write("<input type=hidden name=typeno value="+type+">");
			out.write("<textarea name=msg rows=4 cols=60 style=\"float:left\">"+rvo.getMsg()+"</textarea>");
			out.write("<input type=submit value=\"댓글 수정\" style=\"width:100px;height:85px;background-color:blue;color:white\">"); // 공백이 들어가거나 문장이 길어지면 무조건 따음표 쓰기
			out.write("</form>");
			// 처리하는 서블릿(JSP) / 화면 출력 서블릿(JSP)
			// => 화면이 없는 (HTML) => 자체에서 처리 
			out.write("</td>");
			out.write("</tr>");
			out.write("</table>");
		}
		out.write("</td>");
		out.write("</tr>");
		out.write("</table>");
		
		//댓글 작성 
		if(id!=null) // 로그인이 되었다면 3개의 값을 보냄  gno,typeno,내용
		{
			out.write("<form method=post action=GoodsDetailServlet>");
			out.write("<input type=hidden name=gno value="+no+">");
			out.write("<input type=hidden name=typeno value="+type+">");
			out.write("<textarea name=msg rows=4 cols=60 style=\"float:left\"></textarea>");
			out.write("<input type=submit value=\"댓글 쓰기\" style=\"width:100px;height:85px;background-color:blue;color:white\">"); // 공백이 들어가거나 문장이 길어지면 무조건 따음표 쓰기
			out.write("</form>");
		}
		out.write("</div>");
		out.write("</body>");
		out.write("</html>");
		// => head , style , script => 닫기를 하지 않는 경우에는 흰색화면이다 
		
		
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 한글 변환
		try {
			req.setCharacterEncoding("UTF-8");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		String gno=req.getParameter("gno");
		String typeno=req.getParameter("typeno");
		String msg=req.getParameter("msg");
		
		HttpSession session=req.getSession();
		String id=(String)session.getAttribute("id");
		String name=(String)session.getAttribute("name");
		
		ReplyVO vo=new ReplyVO();
		vo.setId(id);
		vo.setName(name);
		vo.setMsg(msg);
		vo.setGno(Integer.parseInt(gno));
		vo.setTypeno(Integer.parseInt(typeno));
		
		//DAO 연동
		ReplyDAO dao=ReplyDAO.newInstance();
		dao.replyInsert(vo);
		
		//이동 (화면 이동)
		resp.sendRedirect("MainServlet?mode=5&no="+gno+"&type="+typeno);
		/*
		 * request : 클라이언트에 대한 정보
		 *           ip / port...
		 *           사용자 정보 전송 => 전송한 모든 정보는 request가 가지고 있음
		 * response : 응답 정보 / 헤더 정보
		 *            =======
		 *            | HTML => setContentType
		 *              화면 이동 정보 => sendRedirect()
		 * => JSP 동일
		 *    내장 객체 => request,response,session
		 *    
		 * resp.sendRedirect() => 특정 처리 후 또는 특정 조건일떄 지정한 페이지로 이동하고 싶은 경우 사용
		 */
	}

}