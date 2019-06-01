
package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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
			DataOutputStream outToClient = new DataOutputStream(incoming.getOutputStream());
			String responseMsg="";
			while (true) {
				HashMap<String, String> obj = (HashMap) inFromClient.readObject(); // get Object from client
				System.out.println(obj.toString());
				String func = obj.get("func");
				switch (func) {
					case "login":
						ResultSet res = sql.selectQuery("name", "Clients",obj.get("username"));
						while (res.next()) {
							
							if( res.getString("password").equals(obj.get("password"))){
								responseMsg = "login succsefully";
							}
							else{
								responseMsg = "user login faild";
							}
							// send String to client
							System.out.println(responseMsg);
						
							return;
						}
						break;
						
						default:
							break;
					}
					
				outToClient.writeBytes("asdsadsa");
			}
		} catch (IOException | SQLException |  ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}