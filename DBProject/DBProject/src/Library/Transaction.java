package Library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;



public class Transaction {
	static String url = "jdbc:mysql://localhost:3310/projectDB?characterEncoding=UTF-8&serverTimezone=UTC";
	static String id = "root";
	static String pw = "jinyoung22!m";
	static String driver = "com.mysql.cj.jdbc.Driver";

	private Connection con;
	private PreparedStatement pstmt;
	private PreparedStatement pstmt2;
	private ResultSet rset;
	private ResultSet rset2;

	String s1_1 = "SELECT * FROM BookList WHERE book_name like ?";
	String s1_2 = "SELECT * FROM BookList WHERE publisher like ?";
	String s1_3 = "SELECT * FROM BookList WHERE author like ?";
	String s1_4 = "SELECT * FROM BookList WHERE book_num = ?";
	String s1_5 = "SELECT count(*) FROM BookList WHERE book_name like ?";
	String s1_6 = "SELECT count(*) FROM BookList WHERE publisher like ?";
	String s1_7 = "SELECT count(*) FROM BookList WHERE author like ?";
	String s1_8 = "SELECT count(*) FROM BookList WHERE book_num = ?";

	String s2_1 = "INSERT INTO BookList VALUES (?,?,?,?,?)";
	String s2_2 = "UPDATE BookList SET book_name=?, publisher=?, author=? WHERE book_num=?";
	String s2_3 = "DELETE FROM BookList WHERE book_num=?";
	String s2_4 = "UPDATE BookList SET can_borrow=? WHERE book_num=?";

	String s3_1 = "INSERT INTO CheckedList (checker_id,book_num) VALUES (?,?)";
	String s3_2 = "UPDATE CheckedList SET return_date=? WHERE checker_id=? and book_num=? and return_date is NULL";

	String s4_1 = "INSERT INTO ReserveList (reserver_id, book_num) VALUES (?,?)";
	String s4_2 = "SELECT * FROM ReserveList WHERE reserver_id = ? and book_num=?";
	String s4_3 = "DELETE FROM ReserveList WHERE id = ?";

	String s5_1 = "SELECT * FROM CheckedList WHERE checker_id = ?";
	String s5_2 = "SELECT * FROM ReserveList WHERE reserver_id = ?";
	String s5_3 = "SELECT count(*) FROM CheckedList WHERE checker_id = ?";
	String s5_4 = "SELECT count(*) FROM ReserveList WHERE reserver_id = ?";

	String s6_1 = "SELECT count(*) FROM CheckedList WHERE checker_id = ? and return_date is NULL and date_format(check_date, '%Y-%m-%d')<?";
	String s6_2 = "SELECT * FROM CheckedList WHERE checker_id = ? and return_date is NULL and date_format(check_date, '%Y-%m-%d')<?";
	String s6_3 = "UPDATE CheckedList SET overdue_fee=? WHERE id=?";

	Scanner scn = new Scanner(System.in);

	public static void clearScreen() {
		for (int i = 0; i < 80; i++)
			System.out.println("");
	}

	public static void printBookList(ResultSet rs, int page, int full) throws SQLException {
		// Ensure we start with first row
		clearScreen();
		System.out.printf("Current Page: %d/%d\n\n", page, full);
		rs.absolute((page - 1) * 10 );

		System.out.print("\tBook num\t\t Book name\t\t\t Publisher\t\t\t Author\t\t\t   etc.\n");
		System.out.print("-----------------------------------------------------------------------------------------------------------------------------------------\n");
		while (rs.next()) {
			// Retrieve by column name
			int book_num = rs.getInt("book_num");
			String book_name = rs.getString("book_name");
			String publisher = rs.getString("publisher");
			String author = rs.getString("author");
			boolean can_borrow = rs.getBoolean("can_borrow");

			// Display values
			System.out.print("\t"+book_num+"\t");
			System.out.printf("    %-25s\t",book_name);
			System.out.printf("\t%-20s\t",publisher);
			System.out.printf("\t%-20s\t",author);
			if (can_borrow) {
				System.out.println("   Available");
			} else {
				System.out.println(" Not Available");
			}

			int rowNum = rs.getRow();
			if (rowNum == (page) * 10 || rs.isLast()) {
				break;
			}
		}
		System.out.println();
	}

	public static void printCheckedList(ResultSet rs, int page, int full) throws SQLException {
		// Ensure we start with first row
		clearScreen();
		System.out.printf("Current Page: %d/%d\n\n", page, full);
		rs.absolute((page - 1) * 10 );

		while (rs.next()) {
			// Retrieve by column name
			int id = rs.getInt("id");
			String book_num = rs.getString("book_num");
			String check_date = rs.getString("check_date");
			String return_date = rs.getString("return_date");

			// Display values
			System.out.print("Id: " + id);
			System.out.print(", Book num: " + book_num);
			System.out.print(", Checked date: " + check_date);
			if (return_date != null) {
				System.out.println(",  Return date: " + return_date);
			} else {
				System.out.println(",  Before return");
			}

			int rowNum = rs.getRow();
			if (rowNum == (page) * 10 || rs.isLast()) {
				break;
			}
		}
		System.out.println();
	}

	public static void printReserveList(ResultSet rs, int page, int full) throws SQLException {
		// Ensure we start with first row
		clearScreen();
		System.out.printf("Current Page: %d/%d\n\n", page, full);
		rs.absolute((page - 1) * 10 );

		while (rs.next()) {
			// Retrieve by column name
			int id = rs.getInt("id");
			String book_num = rs.getString("book_num");
			String reserve_date = rs.getString("reserve_date");

			// Display values
			System.out.printf("ID: %06d",id);
			System.out.print(", Book num: " + book_num);
			System.out.println(", Reserved date: " + reserve_date);

			int rowNum = rs.getRow();
			if (rowNum == (page) * 10 || rs.isLast()) {
				break;
			}
		}
		System.out.println();
	}

	public static void printOverdueList(ResultSet rs, int page, int full) throws SQLException {
		// Ensure we start with first row
		clearScreen();
		System.out.printf("Current Page: %d/%d\n\n", page, full);
		rs.absolute((page - 1) * 10 );

		while (rs.next()) {
			// Retrieve by column name
			int id = rs.getInt("id");
			String book_num = rs.getString("book_num");
			String check_date = rs.getString("check_date");
			String overdue_fee = rs.getString("overdue_fee");

			// Display values
			System.out.printf("ID: %06d", id);
			System.out.print(", Book num: " + book_num);
			System.out.print(", Checked date: " + check_date);
			System.out.println(", Overdue fee: " + overdue_fee);

			int rowNum = rs.getRow();
			if (rowNum == (page) * 10 || rs.isLast()) {
				break;
			}
		}
		System.out.println();
	}

	// T1-1 °Ë»öonly
	public void searchBookList(int func)   {

		try {
			con.setAutoCommit(false);

			int rowCount = 0;

			if (func == 1) {

				System.out.print("Input book name: ");
				String book_name = scn.nextLine();
				book_name = "%"+book_name+"%";
				pstmt2 = con.prepareStatement(s1_5);
				pstmt2.setString(1, book_name);
				rset2 = pstmt2.executeQuery();
				rset2.next();
				rowCount = rset2.getInt(1);
				if (rowCount > 0) {
					pstmt = con.prepareStatement(s1_1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
					pstmt.setString(1, book_name);
					rset = pstmt.executeQuery();
				} else {
					throw new Exception("No matched information!");
				}

			} else if (func == 2) {

				System.out.print("Input publisher: ");
				String publisher = scn.nextLine();

				publisher = "%"+publisher+"%";
				pstmt2 = con.prepareStatement(s1_6);
				pstmt2.setString(1, publisher);
				rset2 = pstmt2.executeQuery();
				rset2.next();
				rowCount = rset2.getInt(1);
				if (rowCount > 0) {
					pstmt = con.prepareStatement(s1_2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
					pstmt.setString(1, publisher);
					rset = pstmt.executeQuery();
				} else {
					throw new Exception("No matched information!");
				}
			} else if (func == 3) {

				System.out.print("Input author: ");
				String author = scn.nextLine();

				author = "%"+author+"%";
				pstmt2 = con.prepareStatement(s1_7);
				pstmt2.setString(1, author);
				rset2 = pstmt2.executeQuery();
				rset2.next();
				rowCount = rset2.getInt(1);
				if (rowCount > 0) {
					pstmt = con.prepareStatement(s1_3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
					pstmt.setString(1, author);
					rset = pstmt.executeQuery();
				} else {
					throw new Exception("No matched information!");
				}
			} else if (func == 4) {

				System.out.print("Input book number: ");
				int book_num = scn.nextInt();
				
				pstmt2 = con.prepareStatement(s1_8);
				pstmt2.setInt(1, book_num);
				rset2 = pstmt2.executeQuery();
				rset2.next();
				rowCount = rset2.getInt(1);
				if (rowCount > 0) {
					pstmt = con.prepareStatement(s1_4, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
					pstmt.setInt(1, book_num);
					rset = pstmt.executeQuery();
				} else {
					throw new Exception("No matched information!");
				}
			} else {
				throw new Exception("Wrong function number!");
			}

			int total_page = (int) Math.ceil((double)rowCount / 10.0);;
			printBookList(rset, 1, total_page);
			while (true) {
				System.out.print("\n\nEnter page number to move(<0> to exit) > ");
				int page = scn.nextInt();
				scn.nextLine();
				if (page == 0) {
					break;
				}
				else if(page>total_page) {
					System.out.println("\nExceed the total page number!");
					continue;
				}
				printBookList(rset, page, total_page);
			}
			con.commit();
			
		} catch (Throwable e) {
			System.out.println(e.getMessage());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex) {
				}
			}
		}
	}

	// T2-1 º¯°æ
	public void addbook()   {
		try {
			con.setAutoCommit(false);

			pstmt = con.prepareStatement(s2_1);

			System.out.print("Input book number: ");
			int book_num = scn.nextInt();
			scn.nextLine();
			System.out.print("Input book name: ");
			String book_name = scn.nextLine();
			System.out.print("Input publisher: ");
			String publisher = scn.nextLine();
			System.out.print("Input author: ");
			String author = scn.nextLine();

			pstmt.setInt(1, book_num);
			pstmt.setString(2, book_name);
			pstmt.setString(3, publisher);
			pstmt.setString(4, author);
			pstmt.setInt(5, 1);

			pstmt.executeUpdate();

			con.commit();

			System.out.println("Success to add!");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
			
		} catch (Throwable e) {
			System.out.println(e.getMessage());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex) {
				}
			}
		}
	}

	// T3-1 È¥ÇÕ, b,e
	public void modifybook()   {
		try {
			con.setAutoCommit(false);

			pstmt = con.prepareStatement(s1_4);

			System.out.print("Input book number to modify: ");
			int book_num = scn.nextInt();
			
			clearScreen();

			pstmt.setInt(1, book_num);

			rset = pstmt.executeQuery();

			String book_name = new String();
			String publisher = new String();
			String author = new String();
			boolean can_borrow;

			while (rset.next()) {
				// Retrieve by column name
				book_name = rset.getString("book_name");
				publisher = rset.getString("publisher");
				author = rset.getString("author");
				can_borrow = rset.getBoolean("can_borrow");

				System.out.print("\nBook name: " + book_name);
				System.out.print(", Publisher: " + publisher);
				System.out.print(", Author: " + author);
				if (can_borrow) {
					System.out.println(",  Available");
				} else {
					System.out.println(",  Not Available");
				}
			}

			System.out.println("\n--------Select number to modify---------");
			System.out.println("  1. Book name\r\n" + "  2. Publisher\r\n" + "  3. Author");
			System.out.println("\nPlease Enter <0> to go main screen");
			System.out.print("\n >> ");
			int func = scn.nextInt();
			scn.nextLine();
			if (func == 0)
				throw new Exception("Stop to modify the book information!");

			switch (func) {
			case 1:
				System.out.print("New book name: ");
				book_name = scn.nextLine();
				break;
			case 2:
				System.out.print("New publisher: ");
				publisher = scn.nextLine();
				break;
			case 3:
				System.out.print("New author: ");
				author = scn.nextLine();
				break;
			}

			pstmt = con.prepareStatement(s2_2);
			pstmt.setInt(4, book_num);
			pstmt.setString(1, book_name);
			pstmt.setString(2, publisher);
			pstmt.setString(3, author);

			pstmt.executeUpdate();

			con.commit();
			
			System.out.println("Success to modify!");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
			
		} catch (Throwable e) {
			System.out.println(e.getMessage());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex) {
				}
			}
		}
	}

	// T2-2 º¯°æ
	public void deletebook()   {
		try {
			con.setAutoCommit(false);

			pstmt = con.prepareStatement(s2_3);

			System.out.print("Input book number to delete: ");
			int book_num = scn.nextInt();

			pstmt.setInt(1, book_num);

			int s = pstmt.executeUpdate();
			
			if (s==0) {
				throw new Exception("No row to delete!");
			}

			con.commit();

			System.out.println("Success to delete!");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
			
		} catch (Throwable e) {
			System.out.println(e.getMessage());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex) {
				}
			}
		}
	}

	// T3-2 È¥ÇÕ, a~e
	public void checkBook()   {
		try {
			con.setAutoCommit(false);

			System.out.print("Input book name to borrow: ");
			String book_name = scn.nextLine();
			book_name = "%"+book_name+"%";
			// SELECT count(*) FROM BookList WHERE book_name = ?
			pstmt = con.prepareStatement(s1_5);
			pstmt.setString(1, book_name);
			rset = pstmt.executeQuery();
			rset.next();
			int rowCount = rset.getInt(1);;
			if (rowCount>0) {}
			else
				throw new Exception("No information matching the book name!");

			if (rowCount > 1) {
				// SELECT * FROM BookList WHERE book_name = ?
				pstmt = con.prepareStatement(s1_1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				pstmt.setString(1, book_name);
				rset = pstmt.executeQuery();

				int total_page = (int) Math.ceil((double)rowCount / 10.0);;
				printBookList(rset, 1, total_page);
				while (true) {
					System.out.println("\nEnter <0> to select book number");
					System.out.print("Enter page number to move > ");
					int page = scn.nextInt();
					if (page == 0) {
						break;
					}
					else if(page>total_page) {
						System.out.println("\nExceed the total page number!");
						continue;
					}
					printBookList(rset, page, total_page);
				}

				System.out.print("Input book number to borrow: ");
				int book_num = scn.nextInt();
				
				//s1_8 = "SELECT count(*) FROM BookList WHERE book_num = ?";
				pstmt = con.prepareStatement(s1_8);
				pstmt.setInt(1, book_num);
				rset = pstmt.executeQuery();
				rset.next();
				int book_i = rset.getInt(1);
				if(book_i!=1) {
					throw new Exception("No book matching!");
				}
				
				// SELECT * FROM BookList WHERE book_num = ?
				pstmt = con.prepareStatement(s1_4);
				pstmt.setInt(1, book_num);
				rset = pstmt.executeQuery();
			} else {
				//s1_5 = "SELECT count(*) FROM BookList WHERE book_name = ?";
				pstmt = con.prepareStatement(s1_5);
				pstmt.setString(1, book_name);
				rset = pstmt.executeQuery();
				rset.next();
				int book_i = rset.getInt(1);
				if(book_i!=1) {
					throw new Exception("No book matching!");
				}
				
				// SELECT * FROM BookList WHERE book_name like ?
				pstmt = con.prepareStatement(s1_1);
				pstmt.setString(1, book_name);
				rset = pstmt.executeQuery();
			}

			int book_num = 0;
			String publisher = new String();
			String author = new String();
			String book_name_2 = new String();
			boolean can_borrow;
			
			while (rset.next()) {
				book_num = rset.getInt("book_num");
				book_name_2 = rset.getString("book_name");
				publisher = rset.getString("publisher");
				author = rset.getString("author");
				can_borrow = rset.getBoolean("can_borrow");
				if (!can_borrow) {
					throw new Exception("Can't borrow!");
				}
			}

			System.out.print("Input your student ID: ");
			int id = scn.nextInt();

			// SELECT * FROM ReserveList WHERE reserver_id = ? and book_num=?
			pstmt2 = con.prepareStatement(s4_2);
			pstmt2.setInt(1, id);
			pstmt2.setInt(2, book_num);
			rset2 = pstmt2.executeQuery();
			while (rset2.next()) {
				// Retrieve by column name
				System.out.println("You have reservation information!");
				int reserve_id = rset.getInt("id");
				String reserve_date = rset.getString("reserve_date");

				System.out.print("Reserve ID: " + reserve_id);
				System.out.print(", Book name: " + book_name_2);
				System.out.print(", Publisher: " + publisher);
				System.out.print(", Author: " + author);
				System.out.print(", Reserved date: " + reserve_date);

				// DELETE FROM ReserveList WHERE id = ?
				pstmt = con.prepareStatement(s4_3);
				pstmt.setInt(1, reserve_id);
				pstmt.executeUpdate();

			}

			// INSERT INTO CheckedList (checker_id,book_num,check_date) VALUES (?,?,?)
			pstmt = con.prepareStatement(s3_1);
			pstmt.setInt(1, id);
			pstmt.setInt(2, book_num);
			pstmt.executeUpdate();

			// s2_4 = "UPDATE BookList can_borrow=? WHERE book_num=?";
			pstmt2 = con.prepareStatement(s2_4);
			pstmt2.setBoolean(1, false);
			pstmt2.setInt(2, book_num);
			pstmt2.executeUpdate();

			con.commit();

			System.out.println("\nSuccess to borrow book, " + book_name_2);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}
			
		} catch (Throwable e) {
			System.out.println(e.getMessage());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex) {
				}
			}
		}
	}

	// T3-3 È¥ÇÕ, b,e
	public void reserveBook()   {

		try {
			con.setAutoCommit(false);
			
			System.out.print("Input book name to reserve: ");
			String book_name = scn.nextLine();

			book_name = "%"+book_name+"%";
			// SELECT count(*) FROM BookList WHERE book_name = ?
			pstmt = con.prepareStatement(s1_5);
			pstmt.setString(1, book_name);
			rset = pstmt.executeQuery();
			rset.next();
			int rowCount = rset.getInt(1);
			if (rowCount>0) {}
			else
				throw new Exception("No information matching the book name!");

			if (rowCount > 1) {
				// SELECT * FROM BookList WHERE book_name = ?
				pstmt = con.prepareStatement(s1_1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				pstmt.setString(1, book_name);
				rset = pstmt.executeQuery();

				int total_page = (int) Math.ceil((double)rowCount / 10.0);;
				printBookList(rset, 1, total_page);
				while (true) {
					System.out.println("\nEnter <0> to select book number");
					System.out.print("Enter page number to move > ");
					int page = scn.nextInt();
					if (page == 0) {
						break;
					}
					else if(page>total_page) {
						System.out.println("\nExceed the total page number!");
						continue;
					}
					printBookList(rset, page, total_page);
				}

				System.out.print("\nInput book number to reserve: ");
				int book_num = scn.nextInt();
				
				//s1_8 = "SELECT count(*) FROM BookList WHERE book_num = ?";
				pstmt = con.prepareStatement(s1_8);
				pstmt.setInt(1, book_num);
				rset = pstmt.executeQuery();
				rset.next();
				int book_i = rset.getInt(1);
				if(book_i!=1) {
					throw new Exception("No book matching!");
				}
				
				// SELECT * FROM BookList WHERE book_num = ?
				pstmt = con.prepareStatement(s1_4);
				pstmt.setInt(1, book_num);
				rset = pstmt.executeQuery();
			} else {
				
				//s1_5 = "SELECT count(*) FROM BookList WHERE book_name = ?";
				pstmt = con.prepareStatement(s1_5);
				pstmt.setString(1, book_name);
				rset = pstmt.executeQuery();
				rset.next();
				int book_i = rset.getInt(1);
				if(book_i!=1) {
					throw new Exception("No book matching!");
				}
				
				// SELECT * FROM BookList WHERE book_name = ?
				pstmt = con.prepareStatement(s1_1);
				pstmt.setString(1, book_name);
				rset = pstmt.executeQuery();
			}

			int book_num = 0;
			String book_name_2 = new String();
			String publisher = new String();
			String author = new String();

			while (rset.next()) {
				book_num = rset.getInt("book_num");
				publisher = rset.getString("publisher");
				author = rset.getString("author");
			}

			System.out.print("Input your student ID: ");
			int id = scn.nextInt();

			// SELECT * FROM ReserveList WHERE reserver_id = ? and book_num=?
			pstmt2 = con.prepareStatement(s4_2);
			pstmt2.setInt(1, id);
			pstmt2.setInt(2, book_num);
			rset2 = pstmt2.executeQuery();
			if (rset2.next())
				throw new Exception("You already have reservation!");
			else {
				// INSERT INTO ReserveList (reserver_id, book_num, reserve_date) VALUES (?,?,?)
				pstmt = con.prepareStatement(s4_1);
				pstmt.setInt(1, id);
				pstmt.setInt(2, book_num);
				pstmt.executeUpdate();
			}

			con.commit();

			System.out.println("\nSuccess to reserve book");
			System.out.print(" >> Book name: " + book_name_2);
			System.out.print(", Publisher: " + publisher);
			System.out.print(", Author: " + author);
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}
			
		} catch (Throwable e) {
			System.out.println(e.getMessage());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex) {
				}
			}
		}
	}

	// T2-3 º¯°æ
	public void returnBook(int book_num, int id)   {
		try {
			// s2_4 = "UPDATE BookList can_borrow=? WHERE book_num=?";
			con.setAutoCommit(false);
			
			pstmt = con.prepareStatement(s2_4);
			pstmt.setInt(1, 1);
			pstmt.setInt(2, book_num);
			int s = pstmt.executeUpdate();
			if(s==0) {
				throw new Exception("No rows matching the book number!");
			}

			// s3_2 = "UPDATE CheckedList SET return_date=? WHERE checker_id=? and
			// book_num=? and return_date is NULL";
			pstmt2 = con.prepareStatement(s3_2);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String format_time = format.format(System.currentTimeMillis());
			pstmt2.setString(1, format_time);
			pstmt2.setInt(2, id);
			pstmt2.setInt(3, book_num);
			s = pstmt2.executeUpdate();
			if(s==0) {
				throw new Exception("No checked information!");
			}

			con.commit();

			System.out.println("Success to return book");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
			
		} catch (Throwable e) {
			System.out.println(e.getMessage());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex) {
				}
			}
		}
	}

	// T1-2 °Ë»öonly
	public void checklist(int id)   {
		try {
			con.setAutoCommit(false);

			// s5_3 = "SELECT count(*) FROM CheckedList WHERE checker_id = ?";
			pstmt2 = con.prepareStatement(s5_3);
			pstmt2.setInt(1, id);
			rset2 = pstmt2.executeQuery();
			rset2.next();
			int rowCount = rset2.getInt(1);
			if (rowCount > 0) {
				// s5_1 = "SELECT * FROM CheckedList WHERE checker_id = ?"
				pstmt = con.prepareStatement(s5_1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				pstmt.setInt(1, id);
				rset = pstmt.executeQuery();
			} else {
				throw new Exception("No matching information!");
			}

			int full_page = (int) Math.ceil((double)rowCount / 10.0);
			if(full_page == 0) full_page = 1;
			printCheckedList(rset, 1, full_page);
			while (true) {
				System.out.print("\n\nEnter page number to move(<0> to exit) > ");
				int page = scn.nextInt();
				if (page == 0) {
					break;
				} else if (page > full_page) {
					System.out.println("\nExceed the total page number!");
					continue;
				}
				printCheckedList(rset, page, full_page);
			}

			con.commit();

		} catch (Throwable e) {
			System.out.println(e.getMessage());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex) {
				}
			}
		}
	}

	// T1-3 °Ë»öonly
	public void reservelist(int id)   {
		try {
			con.setAutoCommit(false);

			// s5_4 = "SELECT count(*) FROM ReserveList WHERE reserver_id = ?";
			pstmt2 = con.prepareStatement(s5_4);
			pstmt2.setInt(1, id);
			rset2 = pstmt2.executeQuery();
			rset2.next();
			int rowCount = rset2.getInt(1);
			if (rowCount > 0) {
				// s5_2 = "SELECT * FROM ReserveList WHERE reserver_id = ?";
				pstmt = con.prepareStatement(s5_2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				pstmt.setInt(1, id);
				rset = pstmt.executeQuery();
			} else {
				throw new Exception("No matching information!");
			}

			int total_page = (int) Math.ceil((double)rowCount / 10.0);
			printReserveList(rset, 1, total_page);
			while (true) {
				System.out.print("\n\nEnter page number to move(<0> to exit) > ");
				int page = scn.nextInt();
				if (page == 0) {
					break;
				} else if (page > total_page) {
					System.out.println("\nExceed the total page number!");
					continue;
				}
				printReserveList(rset, page, total_page);
			}

			con.commit();

		} catch (Throwable e) {
			System.out.println(e.getMessage());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex) {
				}
			}
		}
	}

	// T3-4 È¥ÇÕ
	public void overduelist(int id)   {
		try {
			con.setAutoCommit(false);

			// s6_1 = "SELECT count(*) FROM CheckedList WHERE checker_id = ? and
			// return_date=NULL and check_date<str_to_date(?,'%Y-%m-%d %H:%m:%s')";
			pstmt2 = con.prepareStatement(s6_1);
			pstmt2.setInt(1, id);

			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, -14);
			String format_time = format.format(cal.getTime());
			pstmt2.setString(2, format_time);

			rset2 = pstmt2.executeQuery();
			rset2.next();
			int rowCount = rset2.getInt(1);
			if (rowCount > 0) {
				// s6_2 = "SELECT * FROM CheckedList WHERE checker_id = ? and return_date is NULL
				// and check_date<?";
				pstmt = con.prepareStatement(s6_2);
				pstmt.setInt(1, id);
				pstmt.setString(2, format_time);
				rset = pstmt.executeQuery();
			} else {
				throw new Exception("No overdue list!");
			}

			int current_fee = 0;

			// calculate overdue_fee
			while (rset.next()) {
				int overdue_fee = rset.getInt("overdue_fee");
				int check_id = rset.getInt("id");
				int book_num = rset.getInt("book_num");
				Date check_date = rset.getDate("check_date");
				
				// s6_3 = "UPDATE CheckedList SET overdue_fee=? WHERE id=?";
				pstmt2 = con.prepareStatement(s6_3);
				int fee_toadd = 0;
				long diff = date.getTime()- check_date.getTime();
				fee_toadd = (int) (diff / (24 * 60 * 60 * 1000));
				pstmt2.setInt(1, fee_toadd * 200);
				current_fee += fee_toadd * 200;
				pstmt2.setInt(2, check_id);
				int s = pstmt2.executeUpdate();
				if(s==0) {
					throw new Exception("Fail to update overdue fee!");
				}
			}

			con.commit();
			System.out.printf("Current overdue fee: %d\n", current_fee);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}

		} catch (Throwable e) {
			System.out.println(e.getMessage());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex) {
				}
			}
		}
	}

	// T3-5 È¥ÇÕ
	public void payoverduefee(int id)   {
		try {
			con.setAutoCommit(false);

			// s6_1 = "SELECT count(*) FROM CheckedList WHERE checker_id = ? and
			// return_date=NULL and check_date<?";
			pstmt2 = con.prepareStatement(s6_1);
			pstmt2.setInt(1, id);

			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, -14);
			String format_time = format.format(cal.getTime());
			pstmt2.setString(2, format_time);

			rset2 = pstmt2.executeQuery();
			rset2.next();
			int rowCount = rset2.getInt(1);
			if (rowCount > 0) {
				// s6_2 = "SELECT * FROM CheckedList WHERE checker_id = ? and return_date=NULL
				// and check_date<?";
				pstmt = con.prepareStatement(s5_1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				pstmt.setInt(1, id);
				rset = pstmt.executeQuery();
			} else {
				throw new Exception("No overdue fee information!");
			}

			int total_page = (int) Math.ceil((double)rowCount / 10.0);;
			printOverdueList(rset, 1, total_page);
			while (true) {
				System.out.print("\nEnter page number to move(<0> to exit) > ");
				int page = scn.nextInt();
				if (page == 0) {
					break;
				} else if (page > total_page) {
					System.out.println("\nExceed the total page number!");
					continue;
				}
				printOverdueList(rset, page, total_page);
			}

			System.out.print("Input check ID to pay the overdue fee (<0> to exit) > ");
			int check_id = scn.nextInt();
			if (check_id==0) {
				throw new Exception("Stop to pay!");
			}

			//s6_3 = "UPDATE CheckedList overdue_fee=? WHERE id=?";
			pstmt = con.prepareStatement(s6_3);
			pstmt.setInt(1, 0);
			pstmt.setInt(2, check_id);
			pstmt.executeUpdate();

			con.commit();
			
			System.out.println("Success to pay!");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}

		} catch (Throwable e) {
			System.out.println(e.getMessage());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex) {
				}
			}
		}
	}

	public Transaction() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, id, pw);

		} catch (ClassNotFoundException e) {
			System.out.println("Fail to load driver");
		} catch (SQLException e) {
			System.out.println("Fail to connect DB");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Fail to transaction: " + e.getMessage());
		}
	}
	
	public void closecon() {
		try {
			rset.close();
			rset2.close();
			pstmt.close();
			pstmt2.close();
			con.close();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
