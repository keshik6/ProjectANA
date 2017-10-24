package Chat_Client;

import java.io.PrintWriter;
import java.net.Socket;

public class ChatClientGUI{
    public static ChatClient CHATCLIENT;
    public static String userName = "Anonymous";
    public static String[] onlineUsers;
    public static String chatMessages = "";


    public static void main(String[] args){
        Connect();
    }

    public static void Connect(){
        try{
            final int PORT = 444;
            final String HOST = "localhost";
            Socket SOCKET = new Socket(HOST, PORT);
            System.out.println("You are connected to: " + HOST);

            CHATCLIENT = new ChatClient(SOCKET);

            PrintWriter output = new PrintWriter(SOCKET.getOutputStream());
            output.println(userName);
            output.flush();

            Thread t = new Thread(CHATCLIENT);
            t.start();

        }
        catch(Exception e){
            System.out.println(e);
            System.out.println("Server not responding");
            System.exit(0);
        }
    }
}