package Library;
import java.util.Scanner;

import Library.Transaction.*;

public class Main {

	public static void main(String[] args) {
		
		Transaction trans = new Transaction();
		while(true) {
			
			System.out.println("Welcome to libary!");
			System.out.println("\n--------Select the option number(<0> to exit)---------");
			System.out.println("  1. Search for books\r\n" + 
					"  2. Borrow a book\r\n" + 
					"  3. Reserve a book\r\n" + 
					"  4. Return a book\r\n" + 
					"  5. My page\r\n" + 
					"  6. Management");
			System.out.print("\n >> ");
			Scanner sc = new Scanner(System.in);
			int func = sc.nextInt();
			if (func==0) {
				break;
			}
			switch(func) {
			case 1: //search for books
				while(true) {
					clearScreen();
					System.out.println("\n--------Search with..---------");
					System.out.println("  1. Book name\r\n" + 
							"  2. Publisher\r\n" + 
							"  3. Author\r\n" + 
							"  4. Book number");
					System.out.println("\nPlease Enter <0> to go main screen");
					System.out.print(" >> ");
					try {
						func = sc.nextInt();
					}
					catch (Exception e){
						System.out.println(e.getMessage());
						break;
					}
					if(func == 0 ) break;
					trans.searchBookList(func);
				}
				break;
				
			case 2: //Borrow book
				clearScreen();
				trans.checkBook();
				break;
				
			case 3: //Return book
				clearScreen();
				trans.reserveBook();
				break;
				
			case 4: //Return book
				clearScreen();
				System.out.println("\nEnter the information to return the book (<0> to return main screen)");
				System.out.print("   > book number: ");
				int book_num = sc.nextInt();
				if(book_num == 0) break;
				System.out.print("   > student id: ");
				int id = sc.nextInt();
				if(id == 0) break;
				trans.returnBook(book_num, id);
				break;
				
			case 5: //Mypage
				clearScreen();
				System.out.println("------------------Mypage!-----------------");
				System.out.print(" > Please Enter your student ID: ");
				int student_id = sc.nextInt();
				while(true) {
					clearScreen();
					System.out.println("\n--------Select the option number---------");
					System.out.println("  1. Show check list\r\n" + 
							"  2. Show reserve list\r\n" + 
							"  3. Check overdue fee\r\n" +
							"  4. Pay overdue fee");
					System.out.println("\nPlease Enter <0> to go main screen");
					System.out.print(" >> ");
					int func_1 = sc.nextInt();
					if(func_1==0) break;
					switch(func_1) {
					case 1:
						trans.checklist(student_id);
						break;
					case 2:
						trans.reservelist(student_id);
						break;
					case 3:
						trans.overduelist(student_id);
						break;
					case 4:
						trans.payoverduefee(student_id);
						break;
					default:
						System.out.println("Wrong option number!");
						break;
					}
				}
				break;
				
			case 6: //Management
				while(true) {
					clearScreen();
					System.out.println("\n--------Select the function number---------");
					System.out.println("  1. Add a book to List\r\n" + 
							"  2. Modify a book's infomation\r\n" + 
							"  3. Delete a book from List");
					System.out.println("\nPlease Enter <0> to go main screen");
					System.out.print(" >> ");
					func = sc.nextInt();
					if(func == 0 ) break;
					switch(func) {
					case 1:
						trans.addbook();
						break;
					case 2:
						trans.modifybook();
						break;
					case 3:
						trans.deletebook();
						break;
					default:
						System.out.println("Wrong option number!");
						break;
					}
				}
				break;
			default:
				System.out.println("Wrong option number!");
				break;
			}
			clearScreen();
		}
		trans.closecon();
	}
	
	public static void clearScreen() {
	    for (int i = 0; i < 80; i++)
	      System.out.println("");
	}
}
