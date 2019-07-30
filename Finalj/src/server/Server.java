package server;
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String argv[]) throws Exception {
        ServerSocket s = null;
        Sql sql = new Sql(); // create sql object

        try {
            s = new ServerSocket(10000);
            sql.ConectingToSQL(); // connecting to the sql database 

        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

        while (true) {
            Socket incoming = null;

            try {
                incoming = s.accept();

            } catch (IOException e) {
                System.out.println(e);
                continue;
            }

            new socketHandler(incoming, sql).start(); // send the ip of the client with the sql object 

        }
    }
}
