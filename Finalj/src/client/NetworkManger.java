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
            clientSocket = new Socket("127.0.0.1", 10000);
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        } //call the server
    }

    public void listen(){
        Thread listen = new Thread( ()->{
            System.out.println("listen");
            String modifiedSentence;
            BufferedReader inFromServer;
            try {
                inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println(inFromServer.readLine());
                    while(true)
                    {
                        modifiedSentence = inFromServer.readLine(); // getting from server
                        if(modifiedSentence != null)
                        {
                            System.out.println("FROM : " + modifiedSentence);// printing in the console	
                        }    
                    }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        listen.start();
    }


    public void sendMsg(HashMap<String,String> data){
        Thread msg = new Thread(()->{
            System.out.println("sendMsg");
            OutputStream os;
            ObjectOutputStream mapOutputStream = null;  
            try {
                os = clientSocket.getOutputStream();// OutputStream where to send the map in case of network you get it from the Socket instance.
                mapOutputStream = new ObjectOutputStream(os);
                mapOutputStream.writeObject(data);
                return;
            }   
            catch (Exception e)  {
                //TODO: handle exception
            }finally{
                Thread.currentThread().interrupt();
            }
        });
        msg.start();
    }
}
