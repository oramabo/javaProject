package client;

import java.io.*; 
import java.net.*;
import java.util.HashMap;
import java.util.HashMap;

public class NetworkManger {

    public Socket clientSocket;
    // constractor
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
            // set new conncetion
            clientSocket = new Socket("127.0.0.1",3000);
        
        final String[] message =new String[1];
        // ioen thread to send and recive msgs
        Thread msg = new Thread(){
            @Override
            public void run(){
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
                    return;
                }   
                catch (Exception e)  {
                    e.printStackTrace();
                }
            }
        };
        msg.start();
        try {
            // wait for thread to finish and return the server response
            msg.join();
            return message[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
    } catch (IOException e) {
        e.printStackTrace();
    } 
        return "err";
        
    }
}


