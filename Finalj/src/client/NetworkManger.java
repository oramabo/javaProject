package client;

import java.io.*; 
import java.net.*;
import java.util.HashMap;
import java.util.HashMap;

public class NetworkManger {

    public Socket clientSocket;
    NetworkManger() 
    {   
        try {
            clientSocket = new Socket("127.0.0.1",3000);
        } catch (IOException e) {
            e.printStackTrace();
        } //call the server
    }

    public String sendMsg(HashMap<String,String> data){
        try {
            clientSocket = new Socket("127.0.0.1",3000);
        
        final String[] message =new String[1];
        Thread msg = new Thread(){
            @Override
            public void run(){
                System.out.println("sendMsg");
                OutputStream os;
                ObjectOutputStream mapOutputStream = null;  
                try {
                    // OutputStream where to send the map in case of network you get it from the Socket instance.
                    os = clientSocket.getOutputStream();
                    mapOutputStream = new ObjectOutputStream(os);
                    mapOutputStream.writeObject(data);

                    //Get the return message from the server
                    ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                    message[0] = ois.readUTF();
                    //System.out.println(message);
                    return;
                }   
                catch (Exception e)  {
                    //TODO: handle exception
                }finally{
                   // Thread.currentThread().interrupt();
                }
            }
        };
        msg.start();
        try {
            msg.join();
            System.out.println("after thread "+message[0]);
            return message[0];
        } catch (Exception e) {
            //TODO: handle exception
        }
    } catch (IOException e) {
        e.printStackTrace();
    } 
        return "err";
        
    }
}


