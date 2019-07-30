
package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import java.io.OutputStream;

import com.mysql.jdbc.ResultSetImpl;

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
				HashMap<String, String> obj = (HashMap) inFromClient.readObject(); // get Object from client
				System.out.println(obj.toString());
				String func = obj.get("func");
				String type = obj.get("userType") == "manager"? "manager": "Clients";
				switch (func) {
					case "login":
						ResultSet res = sql.selectQuery("name", type ,obj.get("username"));
						if (!res.first()) {
							responseMsg = "user login faild";
						}
						else {
							while (res.next()) {
								if( res.getString("password").equals(obj.get("password"))){
									responseMsg = "login succsefully";
								}
								else{
									responseMsg = "user login faild";
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
						default:
							break;
					}
			}
		} catch (IOException | SQLException |  ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}