
package server;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class socketHandler extends Thread {
	Socket incoming;
	Sql sql;
	socketHandler(Socket _in, Sql sql) {
		this.incoming = _in;
		this.sql = sql;
	}

	public void run() {
		int sum = 0;
		try {
			ObjectInputStream inFromClient = new ObjectInputStream(incoming.getInputStream());
			DataOutputStream outToClient = new DataOutputStream(incoming.getOutputStream());
			while (true) {
				Object obj = inFromClient.readObject(); // get Object from client
				//send String to client
				outToClient.writeBytes("d");
			}
		} catch (IOException | ClassNotFoundException e) {

		}

	}
}