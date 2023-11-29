package com.sist.dao;
import java.util.*;
import java.sql.*;
public class MusicDAO {
	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	public MusicDAO()
	   {
	      try
	      {
	         Class.forName("oracle.jdbc.driver.OracleDriver");
	      }catch(Exception ex) {}
	   }
	   // 오라클 연결
	   public void getConnection()
	   {
	      try
	      {
	         conn=DriverManager.getConnection(URL,"hr","happy");
	      }catch(Exception ex) {}
	   }
	   // 오라클 닫기
	   public void disConnection()
	   {
	      try
	      {
	         if(ps!=null)ps.close();
	         if(conn!=null)conn.close();
	         
	      }catch(Exception ex) {}
	   }
	   public List<MusicVO> musicListData()
	   {
		   ArrayList<MusicVO> list=new ArrayList<MusicVO>();
		   try {
			   getConnection();
			   String sql="SELECT title,poster,album,singer "
			   		+ "FROM music "
			   		+ "ORDER BY mno ASC";
			   ps=conn.prepareStatement(sql);
			   ResultSet rs=ps.executeQuery();
			   while(rs.next())
			   {
				   MusicVO vo=new MusicVO();
				   vo.setTitle(rs.getString(1));
				   vo.setPoster(rs.getString(2));
				   vo.setAlbum(rs.getString(3));
				   vo.setSinger(rs.getString(4));
				   list.add(vo);
			   }
		   }catch(Exception e)
		   {
			   e.getStackTrace();
		   }finally {
			   disConnection();
		   }
		   return list;
	   }
}
