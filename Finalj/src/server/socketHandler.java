
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

import javax.print.DocFlavor.STRING;


public class socketHandler extends Thread {
	Socket incoming; // incoming msg
	Sql sql;
	Scanner scanner = new Scanner(System.in);// Create a Scanner object
	String type;
	socketHandler(Socket _in, Sql sql) {
		this.incoming = _in;
		this.sql = sql;
	}

	public void run() {
		System.out.println("run");
		try {
			// open input stream to get msg
			ObjectInputStream inFromClient = new ObjectInputStream(incoming.getInputStream());
			ObjectOutputStream oos = null;
			String responseMsg =""; // response string
			while (true) {
				// create obj from client msg
				HashMap<String, String> obj = (HashMap) inFromClient.readObject();
				System.out.println(obj.toString()); // get Object from client
				String func = obj.get("func");
				System.out.println(func);
				switch (func) { // check the func and to waht needed
					case "login": // login func that also check user type
						type = obj.get("userType") == "manager"? "manager": "Clients";
						ResultSet res = sql.selectQuery("name", type ,obj.get("username"));
						
						if (!res.first()) { // taking the first obj 
							responseMsg = "login failed";
						}
						else {
							String userID = res.getString("userid");
							if( res.getString("password").equals(obj.get("password"))){
								responseMsg = "login succsefully, "+userID;
							}
							else{
								responseMsg = "user login faild,";
							}
						}
						
						// send String to client
						oos = new ObjectOutputStream(incoming.getOutputStream());
						//write object to Socket
						oos.writeUTF(responseMsg);
						System.out.println("Message sent to the client is "+responseMsg);
						oos.flush();
						oos.close();
						return;

						case "passChange":
						// user pass change
							String col = "password";
							String newPass =  obj.get("password");
							String username = obj.get("username");
							// get the pass and user name from user and update sql 
							sql.update_statement( new String[] { "name",username}, col ,"Clients", newPass);
							
							// send String to client
							oos = new ObjectOutputStream(incoming.getOutputStream());
							//write object to Socket
							responseMsg = "update ok";
							oos.writeUTF(responseMsg);
							System.out.println("Message sent to the client is "+responseMsg);
							oos.flush();
							oos.close();
							break;
						case "getPayment":
						// get tannat paymat
							System.out.println("getPayment");
							String apartment = obj.get("apartment");
							ResultSet monthPaid = sql.selectQuery("apertment", "Clients" ,apartment);
							// check if there is results
							if (!monthPaid.first()) {
								responseMsg = "Apartment not found";
							}
							else{
								// make the repsoe str to client
								if( monthPaid.getString("monthPaid").length()>0){
									responseMsg = monthPaid.getString("monthPaid");
								}
								else{
									responseMsg = "not found any payments";
								}
							}
							// send String to client
							oos = new ObjectOutputStream(incoming.getOutputStream());
							//write object to Socket
							oos.writeUTF(responseMsg);
							System.out.println("Message sent to the client is "+responseMsg);
							oos.flush();
							oos.close();
							break;
						case "getTotalPayments":
							// get total payments
							System.out.println("getTotalPayments");
							ResultSet totalPayments = sql.selectAll("Clients");

							// check if there is results
							if (!totalPayments.first()) {
								responseMsg = "Apartments not found";
							}
							else{
								responseMsg = "";
								// loop on the result and create response str
								while(totalPayments.getRow() != 0){
									System.out.println(totalPayments.getRow());
									String aprtmentNum = totalPayments.getString("apertment");
									String monthsPaid = totalPayments.getString("monthPaid");
									responseMsg += "apertment: "+aprtmentNum+ " paid months: " +monthsPaid +"\n";
									totalPayments.next();
								}
							}
							// send String to client
							oos = new ObjectOutputStream(incoming.getOutputStream());
							//write object to Socket
							oos.writeUTF(responseMsg);
							System.out.println("Message sent to the client is "+responseMsg);
							oos.flush();
							oos.close();
							break;

						case "updatePayments":
						// update tanent payments
							System.out.println("insert apartment number to update the payment for");
							apartment = obj.get("apartment");
							col = "monthPaid";
							String month = obj.get("month");
							ResultSet paidMonth =  sql.selectQuery("apertment", "Clients", apartment);
						// check if there is results
							if( !paidMonth.first() ){
								responseMsg = "Apartment not found";
							}
							else{
								String allMonth = paidMonth.getString(col);
								String userID = paidMonth.getString("userId");
								String payPerMonth = paidMonth.getString("payPerMonth");
								String[] monthArr = allMonth.split(" ");
								boolean flag = true;
								// chekc if month is paid
								for(String m : monthArr){
									if( m.equals(month)){
										responseMsg = "Month allready paid.";
										flag = false;
									}
								}
								if( flag ){
									// insert to sql the mew month
									allMonth += " "+ month;
									sql.insert_statement("monthpaid", 
										new String[] { "userid","monthid","payAmount"}, 
										new String[] { userID, month, payPerMonth }
									);
									sql.update_statement( new String[] { "apertment",apartment}, col ,"Clients", allMonth);
									responseMsg = "Updated ok";
								}
							}
							// send String to client
							oos = new ObjectOutputStream(incoming.getOutputStream());
							//write object to Socket
							responseMsg = "update ok";
							oos.writeUTF(responseMsg);
							System.out.println("Message sent to the client is "+responseMsg);
							oos.flush();
							oos.close();
							break;
						
						case "sumAll":
						// sum all month paid 
							responseMsg = "";								
							for( int i = 1; i <= 12; i++){
								ResultSet sumSql = sql.sumQuery("payAmount", "monthpaid",  new String[] { "monthid", Integer.toString(i) } );
								if (!sumSql.first()) {
									responseMsg = "err";
									break;
								}
								String num = sumSql.getString("sum(payamount)") == null? "0" :  sumSql.getString("sum(payamount)");
								responseMsg += "month: "+ Integer.toString(i)+ " total income: "+ num + "\n";
							}
							// send String to client
							oos = new ObjectOutputStream(incoming.getOutputStream());
							//write object to Socket;
							oos.writeUTF(responseMsg);
							System.out.println("Message sent to the client is "+responseMsg);
							oos.flush();
							oos.close();
							break;
							
							case "getTenantPayments":
							// get tenat past payments
								String uname = obj.get("name");
								String uid = obj.get("userid");

								ResultSet myPaymentsSql = sql.selectQuery( "userId", "monthpaid", uid );
								// check if there is results
								if (!myPaymentsSql.first()) {
									responseMsg = "err";
									break;
								
								}
								// loop on the sql res and buile the response str
								while(myPaymentsSql.getRow() != 0){
									System.out.println(myPaymentsSql.getRow());
									String monthNum = myPaymentsSql.getString("monthid");
									String monthSum = myPaymentsSql.getString("payAmount");
									responseMsg += "month: "+monthNum+ " amount paid: " +monthSum +"\n";
									myPaymentsSql.next();
								}
								// send String to client
								oos = new ObjectOutputStream(incoming.getOutputStream());
								//write object to Socket
								oos.writeUTF(responseMsg);
								System.out.println("Message sent to the client is "+responseMsg);
								oos.flush();
								oos.close();
								break;
						
				}
							
				}
			
		} catch (IOException | SQLException |  ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}