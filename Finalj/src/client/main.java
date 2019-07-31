	package client;
	import java.util.HashMap;
	import java.util.Scanner;  // Import the Scanner class
	public class main {
		public NetworkManger networkManger = new NetworkManger();
		public Tenant client;
		public Manager manager;
		String userId;
		String userName;
		Scanner scanner = new Scanner(System.in);// Create a Scanner object

		public void login(){
			String pass;
			int userType , method;
			HashMap<String,String> dataToSend = new HashMap<>();

			do{
				System.out.println("1.login as manager\n2. login as client");
				userType = Integer.parseInt( scanner.nextLine()); 
			}while(  userType != 1 && userType != 2);
			do{
				System.out.println("1.login\n2. change password");
				method = Integer.parseInt( scanner.nextLine());
			}while(  method != 1 && method != 2);

			System.out.println("enter user name:\n");
			userName = scanner.nextLine();
			System.out.println(method == 1? "enter password:" : "enter new password");
			pass = scanner.nextLine();
			//scanner.close(); // close the scanner

			dataToSend.put("func", method == 1? "login" : "passChange");
			dataToSend.put("userType", userType == 1? "manager" : "client");
			dataToSend.put("username", userName);
			dataToSend.put("password", pass);

			String[] response = networkManger.sendMsg(dataToSend).split(",");
			
			if( response[0].equals("login succsefully")){
				userId = response[1];
				menu(1);
				// menu(userType);
			}
			if( response.equals("passChanged")){
				System.out.println("Password change! login again");
				login();
			}


		}
		public void menu(int userType){
			String chosenFuncc;
			//Scanner scanner = new Scanner(System.in);// Create a Scanner object
			System.out.println("Choose one of the options below:\n");
			switch(userType){
				case 1:
					// manager
					System.out.println("1. show payments for specific tanent");
					System.out.println("2. show total payments for building");
					System.out.println("3. update payment by apartment number, month, and payment amount");
					System.out.println("4. show total payments for building by months.");
					
					chosenFuncc = scanner.nextLine();
					int chosenFunc = Integer.parseInt( chosenFuncc );
					if(chosenFunc < 0 || chosenFunc > 4){
						menu(1);
						return ;
					}
					handleManagerSelection(chosenFunc);
					break;

				case 2:
					// client
					System.out.println("1. See all the payments paid by me");
				
					chosenFunc = Integer.parseInt( scanner.nextLine());
					if(chosenFunc != 1){
						menu(2);
						return ;
					}
					handleClientSelection(chosenFunc);
					break;	
			}
			scanner.close();
		}
	public void handleClientSelection(int method ){

		// switch(method){
		// 	case 1:
		// 		dataToSend.put("func", "getTenantPayments");
				
		// 		dataToSend.put("username", userName);
		// 		String response = networkManger.sendMsg(dataToSend);
		// 		System.out.println("months paid : " + response);
		// 		menu(2);
		// 		break;
		// }


	}
	public void handleManagerSelection(int method ){

		HashMap<String,String> dataToSend = new HashMap<>();
		//canner scanner = new Scanner(System.in);// Create a Scanner object
		switch(method){
			case 1:
				dataToSend.put("func", "getPayment");
				System.out.println("Enter apartment number");
				String apartNumber = scanner.nextLine();
				dataToSend.put("apartment", apartNumber);
				String response = networkManger.sendMsg(dataToSend);
				System.out.println("month paid : " + response);
				break;
			case 2:
				dataToSend.put("func", "getTotalPayments");
				response = networkManger.sendMsg(dataToSend);
				System.out.println("getTotalPayments\n" + response);
				break;
			case 3:
				dataToSend.put("func", "updatePayments");
				System.out.println("Enter apartment number");
				String apartNum = scanner.nextLine();
				System.out.println("Enter month in number");
				String month = scanner.nextLine();
				dataToSend.put("apartment", apartNum);
				dataToSend.put("month", month);
				response = networkManger.sendMsg(dataToSend);
				System.out.println("updatePayments\n" + response);
				break;
			case 4:
				dataToSend.put("func", "sumAll");
				response = networkManger.sendMsg(dataToSend);
				System.out.println("sumAll\n" + response);
				break;
		}
		menu(1);
	}

		public static void main(String[] args) {

			//Client log in screen
			new main().login();
			//ew main().handleManagerSelection(1);
			//---------------------

		}

	}
