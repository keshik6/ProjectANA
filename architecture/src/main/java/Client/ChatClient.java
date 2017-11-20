package Client;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatClient implements Runnable {
    //Data fields
    public Socket SOCKET;
    private Scanner input;
    Scanner sendMesaage = new Scanner(System.in);
    PrintWriter output;

    //Constructor
    public ChatClient(Socket s){
        this.SOCKET = s;
    }

    @Override
    public void run() {
        try{
            try{
                input = new Scanner(SOCKET.getInputStream());
                output = new PrintWriter(SOCKET.getOutputStream());
                output.flush();
                CheckStream();
            }
            finally {
                SOCKET.close();
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void CheckStream(){
        while (true){
            Recieve();
        }
    }

    public void Recieve(){
        if (input.hasNext()){
            String message = input.nextLine();
            if (message.contains("$#u")){
                String tempMessage = message.substring(3,message.length());
                String[] currentUsers = tempMessage.split(", ");

                //Append to the clientUsers list
                ChatClientGUI.onlineUsers = currentUsers;

            }
            else{
                ChatClientGUI.chatMessages += message + ",";
                System.out.print("Chat History: " + ChatClientGUI.chatMessages);
            }
        }
    }

    public void Send(String s){
        output.println(ChatClientGUI.userName + " : " + s);
        output.flush();
    }

    public void Disconnect() throws IOException{
        output.println(ChatClientGUI.userName + " has disconnected");
        output.flush();
        SOCKET.close();
        System.exit(0);
    }
}

