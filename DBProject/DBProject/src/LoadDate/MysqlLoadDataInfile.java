package LoadDate;

import java.io.File;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.Statement;

   

public class MysqlLoadDataInfile {

	static String url = "jdbc:mysql://localhost:3310/projectDB?characterEncoding=UTF-8&serverTimezone=UTC";
	
	static String id = "root";
	
	static String pw = "jinyoung22!m";
	
	static String driver = "com.mysql.cj.jdbc.Driver";
	
	protected Connection conn;

   

	public static void LoadDataInfileExcute() throws Exception {
	
	    Connection conn = null;
	
	    Statement stmt = null;
	
	    Class.forName(driver); 
	
	    try {
	
	        conn = DriverManager.getConnection(url, id, pw);
	
	        stmt = conn.createStatement();
	
	        String path = "C:/Users/pjpp8/MySQL/mysql-sandboxes/3310/mysql-files/Student.csv"; //파일경로 및 파일명
	
	
	                String sql = "load data infile '"+ path +
	
	                "' INTO TABLE Student " +
	
	                "CHARACTER SET euckr " + //인코딩
	
	                "FIELDS TERMINATED BY ',' " + // ',' 기준으로 파싱
	
	                "LINES TERMINATED BY '\n'";
	                
	                //라인끝
	
	        boolean result = stmt.execute(sql);
	        
	        path = "C:/Users/pjpp8/MySQL/mysql-sandboxes/3310/mysql-files/BookList.csv"; //파일경로 및 파일명
        	
        	
	        sql = "load data infile '"+ path +
	
	                "' INTO TABLE BookList " +
	
	                "CHARACTER SET euckr " + //인코딩
	
	                "FIELDS TERMINATED BY ',' " + // ',' 기준으로 파싱
	
	                "LINES TERMINATED BY '\n'";
	                
	                //라인끝
	
	       result = stmt.execute(sql);
	       
	       path = "C:/Users/pjpp8/MySQL/mysql-sandboxes/3310/mysql-files/CheckedList.csv"; //파일경로 및 파일명
       	
       	
	        sql = "load data infile '"+ path +
	
	                "' INTO TABLE CheckedList " +
	
	                "CHARACTER SET euckr " + //인코딩
	
	                "FIELDS TERMINATED BY ',' " + // ',' 기준으로 파싱
	
	                "LINES TERMINATED BY '\n'";
	                
	                //라인끝
	
	       result = stmt.execute(sql);
	                
	        path = "C:/Users/pjpp8/MySQL/mysql-sandboxes/3310/mysql-files/ReserveList.csv"; //파일경로 및 파일명
	            	
	            	
	        sql = "load data infile '"+ path +
	
	                "' INTO TABLE ReserveList " +
	
	                "CHARACTER SET euckr " + //인코딩
	
	                "FIELDS TERMINATED BY ',' " + // ',' 기준으로 파싱
	
	                "LINES TERMINATED BY '\n'";
	                
	                //라인끝
	
	       result = stmt.execute(sql);
	
	    } catch(Exception e){
	
	        System.out.println("e: " + e);
	
	    } finally {
	
	        conn.close();
	
	        stmt.close();
	
	    }
	}

    public static void main(String[] args){
    	
        try{

            LoadDataInfileExcute();

        } catch(Exception e){

            System.out.println("me: " + e);
        }
    }
}
