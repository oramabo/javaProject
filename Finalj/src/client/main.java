package client;
import java.util.HashMap;
import java.util.Scanner;  // Import the Scanner class
public class main {
	public NetworkManger networkManger = new NetworkManger();
	public Tenant client;
	public Manager manager;

	public void login(){
		Scanner scanner = new Scanner(System.in);// Create a Scanner object
		String userName, pass;
		int userType , method;
		HashMap<String,String> dataToSend = new HashMap<>();

		do{
			System.out.println("1.login as manager\n 2. login as client");
			userType = Integer.parseInt( scanner.nextLine()); 
		}while(  userType != 1 && userType != 2);
		do{
			System.out.println("1.login\n 2. change password");
			method = Integer.parseInt( scanner.nextLine());
		}while(  method != 1 && method != 2);

		System.out.println("enter user name:\n");
		userName = scanner.nextLine();
		System.out.println(method == 1? "enter password:" : "enter new password");
		pass = scanner.nextLine();
		scanner.close(); // close the scanner

		dataToSend.put("func", method == 1? "login" : "passChange");
		dataToSend.put("username", userName);
		dataToSend.put("password", pass);

		networkManger.sendMsg(dataToSend);

		// if( res.get("code").equals("ok")){
		// 	menu(userType);
		// }
		// if( res.get("code").equals("passChanged")){
		// 	System.out.println("Password change! login again");
		// 	login();
		// }


	}
	public void menu(int userType){

	}


	public static void main(String[] args) {

		//Client log in screen
		new main().login();
		//---------------------

	}

}
