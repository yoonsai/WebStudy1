package com.sist.view;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.sist.dao.*;

@WebServlet("/GoodsAllServlet")
public class GoodsAllServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		// 사용자 요청한 값을 받는다 
		// JSP => page (this)
		String page=request.getParameter("page");
		if(page==null)
			page="1";
		int curpage=Integer.parseInt(page);
		// DAO연동 
		GoodsDAO dao=GoodsDAO.newInstance();
		List<GoodsVO> list=dao.goodsListData(curpage, 1);
		int totalpage=dao.goodsTotalPage(1);
		final int BLOCK=10;
		int startPage=((curpage-1)/BLOCK*BLOCK)+1;
		int endPage=((curpage-1)/BLOCK*BLOCK)+BLOCK;
		// totalpage=33 ==> 40(33)
		if(endPage>totalpage)
			endPage=totalpage;
		/*
		 *    1 2 ....   10   31 32 33 ... 40
		 *    |           |
		 *   srartPage   endPage
		 *   =========
		 *   curpage => 1~10 ==> startPage=1
		 */
		out.write("<html>");
		out.write("<body>");
		out.write("<div class=row>");
		out.write("<h1 class=text-center>전체상품</h1>");
		for(GoodsVO vo:list)
		{
			String s=vo.getName();
			if(s.length()>30)
			{
				s=s.substring(0,30)+"...";
				vo.setName(s);
			}
			else
			{
				vo.setName(s);
			}
		}
		for(GoodsVO vo:list)
		{
			   out.write("<div class=\"col-md-3\">");
			   out.write("<div class=\"thumbnail\">");
			   out.write("<a href=MainServlet?mode=5&no="+vo.getNo()+"&type=1>");
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
		   if(startPage>1)// startPage => 1 11 21 31...
		   {
		     out.write("<li><a href=MainServlet?page="+(startPage-1)+"&mode=1>&lt;</a></li>");
		     // 11page ==> 이전 ==> 10
		   }
		   for(int i=startPage;i<=endPage;i++)
		   {
		     out.write("<li "+(i==curpage?"class=active":"")+"><a href=MainServlet?page="+i+"&mode=1>"+i+"</a></li>");
		   }
		   if(endPage<totalpage)// 10 20 30..
		   {
		     out.write("<li><a href=MainServlet?page="+(endPage+1)+"&mode=1>&gt;</a></li>");
		   }
		   out.write("</ul>");
		   out.write("</div>");
		out.write("</body>");
		out.write("</html>");
	}

}