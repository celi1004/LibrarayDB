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
	
	        String path = "C:/Users/pjpp8/MySQL/mysql-sandboxes/3310/mysql-files/Student.csv"; //���ϰ�� �� ���ϸ�
	
	
	                String sql = "load data infile '"+ path +
	
	                "' INTO TABLE Student " +
	
	                "CHARACTER SET euckr " + //���ڵ�
	
	                "FIELDS TERMINATED BY ',' " + // ',' �������� �Ľ�
	
	                "LINES TERMINATED BY '\n'";
	                
	                //���γ�
	
	        boolean result = stmt.execute(sql);
	        
	        path = "C:/Users/pjpp8/MySQL/mysql-sandboxes/3310/mysql-files/BookList.csv"; //���ϰ�� �� ���ϸ�
        	
        	
	        sql = "load data infile '"+ path +
	
	                "' INTO TABLE BookList " +
	
	                "CHARACTER SET euckr " + //���ڵ�
	
	                "FIELDS TERMINATED BY ',' " + // ',' �������� �Ľ�
	
	                "LINES TERMINATED BY '\n'";
	                
	                //���γ�
	
	       result = stmt.execute(sql);
	       
	       path = "C:/Users/pjpp8/MySQL/mysql-sandboxes/3310/mysql-files/CheckedList.csv"; //���ϰ�� �� ���ϸ�
       	
       	
	        sql = "load data infile '"+ path +
	
	                "' INTO TABLE CheckedList " +
	
	                "CHARACTER SET euckr " + //���ڵ�
	
	                "FIELDS TERMINATED BY ',' " + // ',' �������� �Ľ�
	
	                "LINES TERMINATED BY '\n'";
	                
	                //���γ�
	
	       result = stmt.execute(sql);
	                
	        path = "C:/Users/pjpp8/MySQL/mysql-sandboxes/3310/mysql-files/ReserveList.csv"; //���ϰ�� �� ���ϸ�
	            	
	            	
	        sql = "load data infile '"+ path +
	
	                "' INTO TABLE ReserveList " +
	
	                "CHARACTER SET euckr " + //���ڵ�
	
	                "FIELDS TERMINATED BY ',' " + // ',' �������� �Ľ�
	
	                "LINES TERMINATED BY '\n'";
	                
	                //���γ�
	
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
