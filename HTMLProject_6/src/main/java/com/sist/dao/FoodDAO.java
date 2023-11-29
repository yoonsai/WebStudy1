package com.sist.dao;
import java.util.*;
import java.sql.*;// Connection , ResultSet , PreparedStatement
import javax.sql.*; // 데이터베이스 정보 => DataSource
import javax.naming.*; //  문자열로 객체를 찾는 경우 => Context 
// 이름으로 찾기 
/*
 *   lookup() => getBean() (Spring) => 객체 찾기 
 *               =>  클래스 관리자 (생성 ~ 소멸) 
 *               =>  자바기반 => Kotlin
 *               selectList() => SQL  
 *               => SQL관리자 ==> Mapper 
 */
// JDBC => DBCP => ORM (2차프로젝트 : MyBatis / 3차프로젝트 : JPA)
//         ==== 1차 프로젝트 
/*
 *   JDBC : 단점 => 속도가 느리다 (연결 = 해제 반복)
 *                            ==== 오라클 연결시 소모되는 시간이 많다 
 *          => 연결시간 줄이는 프로그램 
 *             ====== 미리 Connection을 오라클 연결한 다음에 수행 
 *                        =====================
 *                         저장 => POOL
 *          => Connection : 톰캣에 의해 관리 
 *   DBCP => DataBase Connection Pool
 *           => 연결 (getConnection) 
 *           => MyBatis / JPA => DBCP
 *   1. 등록 : server.xml => 보안이 뛰어나다 
 *   1) Connection을 연결 
 *      => driverName , username , password , url 
 *      => Connection을  몇개 설정 => maxIdle
 *         => 인원이 많아지면 어떻게 할까? => maxActive
 *         => 반환 => 재사용 => 기다리는 시간 => maxWait => 음수면 무한정
 *   동작 => 생성된 Connection 객체를 POOL에 저장 
 *                                ---- 저장 장소  
 *   1. 미리 생성된 Connection객체를 POOL안에서 얻어온다 
 *      => POOL (JNDI) => 이름으로 탐색기를 만들어 놓은 것 
 *               Java Naming Directory Interface 
 *               => 루트 (C드라이버) => java://comp/env
 *                                   ================
 *                                     jdbc/oracle
 *      Context init=new InitialContext();
 *        => 탐색기를 연다 
 *      Context c=(Context)init.lookup("java://comp/env");
 *      DataSource ds=(DataSource)c.lookup("jdbc/oracle");
 *      Connection conn=ds.getConnection()
 *   2. conn 사용 
 *   3. conn 사용후에 반환 => POOL
 *   => 1. Connection의 생성 횟수를 줄일 수 있다 (관리)
 *   => 2. 오라클 연결 시간을 줄인다 
 *   => 3. Connection 생성을 제한할 수 있기 때문에 
 *         웹 서버가 쉽게 무너지지 않는다 
 *      4. 웹프로그램에서 DB 연결에 기본이다 
 *   =========================
 *    new Connection() false => true
 *   =========================
 *    new Connection() false
 *   =========================
 *    new Connection() false 
 *   =========================
 */
public class FoodDAO {
    private Connection conn;
    private PreparedStatement ps;
    private static FoodDAO dao; // 싱글턴 => 객체 1개 
    // 메모리 누수 현상 방지 => GC
    // 1. 미리 생성된 connection주소 얻기
    public void getConnection()
    {
    	try
    	{
    		Context init=new InitialContext();
    		// POOL연결 
    		Context c=(Context)init.lookup("java://comp/env");
    		// DataBase의 정보를 얻어온다 
    		DataSource ds=(DataSource)c.lookup("jdbc/oracle");
    		// 연결 
    		conn=ds.getConnection();
    	}catch(Exception ex) {}
    }
    // 2. 반환 => 재사용 
    public void disConnection()
    {
    	// 반환 => ps,conn.close() 
    	// => commons-dbcp , commons-pool
    	try
    	{
    		if(ps!=null) ps.close();
    		if(conn!=null) conn.close();
    	}catch(Exception ex) {}
    }
    // 3. 싱글턴 
    // newInstance() , getInstance() => Singleton
    // => 메소드 호출 : 사용자가 보내준 URL에 따라 자동 호출 => invoke()
    // => 메소드마다 구분 (인덱스) => 자바에서 어노테이션 
    public static FoodDAO newInstance()
    {
    	if(dao==null)
    		dao=new FoodDAO();
    	return dao;
    }
    // ================== 공통 사항 
    // 4. 기능 처리 : SQL
    public List<CategoryVO> categoryListData(int cno)
    {
    	List<CategoryVO> list=new ArrayList<CategoryVO>();
    	try
    	{
    		// Connection의 주소 얻기
    		getConnection();
    		int start=0,end=0;
    		// 믿고보는 맛집 리스트 
    		if(cno==1)
    		{
    			start=1;
    			end=12;
    		}
    		// 지역별 맛집 리스트
    		else if(cno==2)
    		{
    			start=13;
    			end=18;
    		}
    		// 메뉴별 맛집 리스트 
    		else if(cno==3)
    		{
    			start=19;
    			end=30;
    		}
    		
    		String sql="SELECT cno,title,poster,subject "
    				  +"FROM food_category "
    				  +"WHERE cno BETWEEN ? AND ?";
    		ps=conn.prepareStatement(sql);
    		ps.setInt(1, start);
    		ps.setInt(2,end);
    		ResultSet rs=ps.executeQuery();
    		while(rs.next())
    		{
    			CategoryVO vo=new CategoryVO();
    			vo.setCno(rs.getInt(1));
    			vo.setTitle(rs.getString(2));
    			vo.setPoster(rs.getString(3));
    			vo.setSubject(rs.getString(4));
    			list.add(vo);
    		}
    		rs.close();
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally {
			// 반환 
    		disConnection();
		}
    	return list;
    }
    // 카테고리별 맛집 출력 
    public CategoryVO categoryInfoData(int cno)
    {
    	CategoryVO vo=new CategoryVO();
    	try
    	{
    		// 주소얻기 
    		getConnection();
    		// SQL문장 제작 
    		String sql="SELECT title,subject "
    				  +"FROM food_category "
    				  +"WHERE cno="+cno;
    		// SQL문장 전송 
    		ps=conn.prepareStatement(sql);
    		// 결과값을 받는다 
    		ResultSet rs=ps.executeQuery();
    		rs.next();
    		
    		// vo에 저장 => 브라우저로 전송 
    		vo.setTitle(rs.getString(1));
    		vo.setSubject(rs.getString(2));
    		rs.close();
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		// 반환 
    		disConnection();
    	}
    	return vo;
    }
    // 카테고리별 맛집 읽기 
    /*
     *   https://mp-seoul-image-production-s3.mangoplate.com/495395_1681611742493259.jpg?fit=around|512:512#crop=512:512;*,*#output-format=jpg#output-quality=80
     *   ^https://mp-seoul-image-production-s3.mangoplate.com/495395_1681611751822597.jpg?fit=around|512:512#crop=512:512;*,*#output-format=jpg#output-quality=80^https://mp-seoul-image-production-s3.mangoplate.com/495395_1681611753020504.jpg?fit=around|512:512#crop=512:512;*,*#output-format=jpg#output-quality=80^https://mp-seoul-image-production-s3.mangoplate.com/495395_1681611754030500.jpg?fit=around|512:512#crop=512:512;*,*#output-format=jpg#output-quality=80^https://mp-seoul-image-production-s3.mangoplate.com/495395_1681611755024181.jpg?fit=around|512:512#crop=512:512;*,*#output-format=jpg#output-quality=80
     */
    public List<FoodVO> foodListData(int cno)
    {
    	List<FoodVO> list=new ArrayList<FoodVO>();
    	try
    	{
    		getConnection();
    		String sql="SELECT fno,cno,poster,name,score,"
    				  +"address,phone,type "
    				  +"FROM food_house "
    				  +"WHERE cno="+cno;
    		ps=conn.prepareStatement(sql);
    		ResultSet rs=ps.executeQuery();
    		while(rs.next())
    		{
    			FoodVO vo=new FoodVO();
    			vo.setFno(rs.getInt(1));
    			vo.setCno(rs.getInt(2));
    			String poster=rs.getString(3);
    			poster=poster.substring(0,poster.indexOf("^"));
    			vo.setPoster(poster);
    			vo.setName(rs.getString(4));
    			vo.setScore(rs.getDouble(5));
    			String addr=rs.getString(6);
    			addr=addr.substring(0,addr.indexOf("지번"));
    			vo.setAddress(addr.trim());
    			vo.setPhone(rs.getString(7));
    			vo.setType(rs.getString(8));
    			list.add(vo);
    		}
    		rs.close();
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		disConnection();
    	}
    	return list;
    }
    
}




