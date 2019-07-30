
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class socketHandler extends Thread {
	Socket incoming;
	Sql sql;

	socketHandler(Socket _in, Sql sql) {
		this.incoming = _in;
		this.sql = sql;
	}

	public void run() {
		System.out.println("run");
		try {
			ObjectInputStream inFromClient = new ObjectInputStream(incoming.getInputStream());
			ObjectOutputStream oos = null;
			String responseMsg ="";
			while (true) {
				HashMap<String, String> obj = (HashMap) inFromClient.readObject();
				System.out.println(obj); // get Object from client
				System.out.println(obj.toString());
				String func = obj.get("func");
				//String type = obj.get("userType") == "manager"? "manager": "Clients";
				System.out.println(func);
				switch (func) {
					case "login":
						ResultSet res = sql.selectQuery("name", "Clients" ,obj.get("username"));
						if (!res.first()) {
							responseMsg = "login failed";
						}
						else {
							if( res.getString("password").equals(obj.get("password"))){
								responseMsg = "login succsefully";
							}
							else{
								responseMsg = "user login faild";
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
							String col = "password";
							String newPass =  obj.get("password");
							String username = obj.get("username");
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
							System.out.println("getPayment");
							String apartment = obj.get("apartment");
							ResultSet monthPaid = sql.selectQuery("apertment", "Clients" ,apartment);
							if (!monthPaid.first()) {
								responseMsg = "Apartment not found";
							}
							else{
								if( monthPaid.getString("monthPaid").length()>0){
									responseMsg = monthPaid.getString("monthPaid");
								}
								else{
									responseMsg = "not found any payments";
								}
							}
						case "getTotalPayments":
							System.out.println("getTotalPayments");
							apartment = obj.get("apartment");
							ResultSet totalPayments = sql.selectMonthPaidQuery();

							if (!totalPayments.first()) {
								responseMsg = "Apartment not found";
							}
							else{
								responseMsg = ("apartment number "+apartment+": "+ totalPayments);
							}
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
							
					}
			
		} catch (IOException | SQLException |  ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}