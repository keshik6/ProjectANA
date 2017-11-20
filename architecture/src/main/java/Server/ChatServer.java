/* This code uses fundamental java network programming to code our local chat server.
* (loopback IP ranges from 127.0.0.0 to 127.255.255.255)*/

package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.*;
import Server.RunnableChatServer;
import java.lang.Runnable;
public class ChatServer {
    //Data fields
    public static ArrayList<Socket> connectedSockets = new ArrayList<>();  //To store the established TCP connection sockets
    public static ArrayList<String> connectedUsers = new ArrayList<>();    //To store the connected users

    public static void  start(){
        try{
            final int PORT = 444;                                          //0-1023 port numbers generally reserved for TCP/FTP connections
            ServerSocket SERVER = new ServerSocket(PORT);                   //Instantiate a server socket for a tcp connection

            System.out.println("Waiting for the class to begin...");

            while (true){
                Socket SOCKET = SERVER.accept();                            //A new socket is created to accept the Server Socket's connection
                connectedSockets.add(SOCKET);                               //Add the new socket to the list of connected sockets

                //Print a message in the server console with client connection details
                System.out.println("Client connected from: " + SOCKET.getLocalAddress().getHostAddress());

                RunnableChatServer r = new RunnableChatServer(SOCKET);
                Thread t = new Thread(r);
                t.start();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void AddUserName(Socket s) throws IOException{
        Scanner input = new Scanner(s.getInputStream());                    //Input to scanner is fed from the socket
        String userName = input.nextLine();                                 //The first line of the socket input will give the user name
        connectedUsers.add(userName);                                       //Add the user to the list of connected users

        //for-each loop to echo the status among all clients
        for (Socket i: connectedSockets){
            PrintWriter output = new PrintWriter(i.getOutputStream());
            output.println("$#u" + connectedUsers);                   //$#u is a prefix used to distinguish these messages
            output.flush();
        }
    }
}

