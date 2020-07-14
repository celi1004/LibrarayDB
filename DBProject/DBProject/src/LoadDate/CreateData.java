package LoadDate;

import java.awt.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CreateData {

/*
Student
 -id int
 -name char
 
BookList
 -book_num int(pk)
 -book_name char
 -publisher char
 -author char
 -can_borrow boolean
 
CheckedlList
 -id int(pk)
 -checker_id int(foriegn key)
 -book_num int(foriegn key)
 -check_date datetime
 -return_date datetime
 -overdue_fee int

ReserveList
 -id int(pk)
 -reserver_id int(foriegn key)
 -book_num  int(foriegn key)
 -reserve_date datetime
*/
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		createStudent();
		createBookList();
		createCheckedList();
		createReserveList();
	}

	static String[] student_id = new String[100000];
	
	public static void createStudent() {
		
		String[] list = new String[100000];
		String temp = new String();
		String id_temp = new String();
		int[] id = {2016, 2017, 2018, 2019};
		
		for(int i=0; i<100000; i++) {
	
			id_temp = Integer.toString(id[randomInt(4)]) + String.format("%05d", i); //id
			student_id[i] =new String(id_temp);//book_num
			temp = id_temp;
			temp += ",";
			temp += randomStr(10); //name
			
			list[i] = new String(temp);
		}
		
		createCSV(list, "Student", "C:/Users/pjpp8/MySQL/mysql-sandboxes/3310/mysql-files/");
	}
	
	static String[] book_number = new String[100000];
	
	public static void createBookList() {
		
		String[] list = new String[100000];
		String temp = new String();
		String book_num_temp = new String();
		String[] name = {"book", "name", "bn", "computer", "science", ""};
		String[] publish_pre = {"red", "pearson", "oxford", "ybm", "cambridge", "happy", "fun"};
		String[] and = {" ", "&", ""};
		String[] publish_suf = {"publish", "brick", "build", "logman", "future","education","press","learning", "university"};
		String[] author_list = {"Anabel", "Anastasia", "Adam", "Jacob", "Mason", "William", "Jayden", "Noah", "Michael", "Ethan", "Alexander", "Aiden", "Daniel", "Andrew", "Tyler"};
		for(int i=0; i<100000; i++) {
			
			book_num_temp = Integer.toString(randomIntLength(3)) + String.format("%05d", i);
			book_number[i] =new String(book_num_temp);//book_num
			temp = book_num_temp;
			temp += ",";
			temp += name[randomInt(6)]+randomStr(5)+name[randomInt(6)]; //bookname
			temp += ",";
			temp += publish_pre[randomInt(7)]+and[randomInt(3)]+publish_suf[randomInt(9)]; //publisher
			temp += ",";
			temp += author_list[randomInt(15)]+randomStr(3); //author
			temp += ",";
			
			if (randomInt(2)==0) {
				temp += "0";
			}
			else {
				temp+="1";
			}
			
			list[i] = new String(temp);
		}
		
		createCSV(list, "BookList", "C:/Users/pjpp8/MySQL/mysql-sandboxes/3310/mysql-files/");
	}
	
	public static void createCheckedList() {
		
		String[] list = new String[250000];
		String temp = new String();
		
		for(int i=0; i<250000; i++) {
			
			temp = Integer.toString(i+1);
			temp += ",";
			temp += student_id[randomInt(100000)]; //checker_id
			temp += ",";
			temp += book_number[randomInt(100000)]; //book_num
			temp += ",";
			Date date = randomDate(-6,0);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String format_time = format.format(date.getTime());
			temp += format_time; //check_date
			temp += ",";
			if(randomInt(2)==1) {
				Calendar returndate = Calendar.getInstance();
				returndate.setTime(date);
				returndate.add(Calendar.DATE, randomInt(90));
				SimpleDateFormat format_2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String format_time_2 = format_2.format(returndate.getTime());
				temp += format_time_2; //return_date
			}
			else {
				temp += "\\N";
			}
			temp += ", 0";
			
			list[i] = new String(temp);
		}
		
		createCSV(list, "CheckedList", "C:/Users/pjpp8/MySQL/mysql-sandboxes/3310/mysql-files/");
	}
	
	public static void createReserveList() {
		
		String[] list = new String[250000];
		String temp = new String();
		
		for(int i=0; i<250000; i++) {
			
			temp = Integer.toString(i+1);
			temp += ",";
			temp += student_id[randomInt(100000)]; //reserver_id
			temp += ",";
			temp += book_number[randomInt(100000)]; //book_num
			temp += ",";
			Date date = randomDate(-4,0);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String format_time = format.format(date.getTime());
			temp += format_time; //reserve_date
			
			list[i] = new String(temp);
		}
		
		createCSV(list, "ReserveList", "C:/Users/pjpp8/MySQL/mysql-sandboxes/3310/mysql-files/");
	}
	
	public static String randomStr(int length) {
		
		Random rnd = new Random();
		String randomStr = new String();
		
		for(int i=0; i<rnd.nextInt(length)+5; i++) {
			int rindex = rnd.nextInt(2);
			if(rindex==0) {
				randomStr += (char)((int)(rnd.nextInt(26))+97);
			}
			else {
				randomStr += (char)((int)(rnd.nextInt(26))+65);
			}
		}
		return randomStr;
	}
	
	public static int randomInt(int range) {
		
		Random rnd = new Random();
		
		return rnd.nextInt(range);
	}
	
	public static int randomIntLength(int length) {
		
		Random rnd = new Random();
		int temp = 0;
		
		for(int i=0; i<length; i++) {
			temp += Math.pow(10, length-i-1)*(randomInt(9)+1);
		}
		return temp;
	}
	
	public static Date randomDate(int start, int end) {
		
		Date tempDate = new Date();
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(tempDate);
		startDate.add(Calendar.MONTH, start);
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(tempDate);
		endDate.add(Calendar.MONTH, end);
		
		long random = ThreadLocalRandom.current().nextLong(startDate.getTimeInMillis(), endDate.getTimeInMillis());
	    Date date = new Date(random);
		
		return date;
	}


	public static  int createCSV(String[] list, String title, String filepath) {
		int resultCount = 0;
		try {
			FileWriter fw = new FileWriter(filepath+"/"+title+".csv");
			
			for(String dom : list) {
				fw.append(dom);
				fw.append('\n');
				resultCount++;
			}
			
			fw.flush();
			fw.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return resultCount;
	}
}
