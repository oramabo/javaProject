package client;
import java.util.HashMap;
import java.util.Scanner;  // Import the Scanner class
public class main {
	public NetworkManger networkManger = new NetworkManger();
	public Tenant client;
	public Manager manager;

	public  void login(){
		Scanner scanner = new Scanner(System.in);// Create a Scanner object
		String userName, pass, method, userType;
		HashMap<String,String> dataToSend = new HashMap<>();
		System.out.println("1.login as manager\n 2. login as client\n");
		userType = scanner.nextLine(); 
		System.out.println("1.login\n 2. change password\n");
		method = scanner.nextLine();
		System.out.println("enter user name:\n");
		userName = scanner.nextLine();
		System.out.println(method.equals("1")? "enter password:" : "enter new password");
		pass = scanner.nextLine();
		scanner.close(); // close the scanner

		dataToSend.put("func",method.equals("1")? "login" : "passChange");
		dataToSend.put("username", userName);
		dataToSend.put("password", pass);
		HashMap<String,String> res =  networkManger.sendMsg(dataToSend);
		if( res.get("code").equals("ok")){
			if(userType.equals("1")){
				manager = new Manager(res.get("name"), res.get("id"),Integer.parseInt( res.get("timeInJob") ));
			}else{
				client = new Tenant(res.get("name"), res.get("id"),res.get("apertmentNum"),Integer.parseInt( res.get("payPerMonth") ));
			}
		}
		if( res.get("code").equals("passChanged")){
			System.out.println("Password change! login again");
			login();
		}


	}
	public static void main(String[] args) {

		//Client log in screen

		//---------------------

	}

}
