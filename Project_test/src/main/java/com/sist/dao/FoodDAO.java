package com.sist.dao;
import java.util.*;
import java.sql.*;
public class FoodDAO {
	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@211.238.142.113:1521:XE";
	private static FoodDAO dao;
	public FoodDAO()
	{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void getConnection()
	{
		try {
			conn=DriverManager.getConnection(URL,"hr","happy");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void disConnection()
	{
		try {
			if(conn!=null) conn.close();
			if(ps!=null) ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static FoodDAO newInstance()
	{
		if(dao==null)
			dao=new FoodDAO();
		
		return dao;
	}
	public List<FoodVO> foodListData(int page)
	{
		List<FoodVO> list=new ArrayList<FoodVO>();
		try {
			getConnection();
			int rowSize=20;
			int start=(rowSize*page)-(rowSize-1); //20*2-(20-1) 40-18=21
	        int end=rowSize*page;
			String sql="SELECT fno,poster,title,menu,addr,num "
					+ "FROM (SELECT fno,poster,title,menu,addr,rownum as num "
					+ "FROM (SELECT /*+ INDEX_ASC(food food_fno_pk)*/fno,poster,title,menu,addr "
					+ "FROM food))"
			        +"WHERE num BETWEEN ? AND ?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, start);
	        ps.setInt(2, end);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				FoodVO vo=new FoodVO();
				vo.setFno(rs.getInt(1));
				vo.setPoster(rs.getString(2));
				vo.setTitle(rs.getString(3));
				vo.setMenu(rs.getString(4));
				vo.setAddr(rs.getString(5));
				list.add(vo);
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally {
			disConnection();
		}
		return list;
	}
}
