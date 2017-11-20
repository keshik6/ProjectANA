/* This code is concerned with creating a ChatServer that implements the runnable interface since
it is required for running multithreads during implementation
 */

package Server;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.lang.Runnable;

public class RunnableChatServer implements Runnable {
    //Data fields
    Socket SOCKET;
    private Scanner input;
    private PrintWriter output;
    private String message = "";

    public RunnableChatServer(Socket s){
        this.SOCKET = s;
    }

    public void checkConnection() throws IOException{
        if (!SOCKET.isConnected()){
            for (Socket s: ChatServer.connectedSockets){
                if (s == SOCKET){
                    ChatServer.connectedSockets.remove(s);
                }
            }

            //Echo disconnection message to all clients
            for (Socket s: ChatServer.connectedSockets){
                PrintWriter tempOutput = new PrintWriter(s.getOutputStream());
                tempOutput.println(s.getLocalAddress().getHostName() + " disconnected");
                tempOutput.flush();

                //Echo disconnection message to the server console
                System.out.println(s.getLocalAddress().getHostName() + " disconnected");
            }
        }


    }

    @Override
    public void run() {
        try{
            try{
                input = new Scanner(SOCKET.getInputStream());
                output = new PrintWriter(SOCKET.getOutputStream());

                while (true){
                    checkConnection();

                    if (!input.hasNext()){
                        return;
                    }

                    //Get the client message and echo it in the server console
                    message = input.nextLine();
                    System.out.println("Client said: " + message);

                    //Echo the message to all the clients
                    for (Socket s: ChatServer.connectedSockets){
                        PrintWriter tempOut = new PrintWriter(s.getOutputStream());
                        tempOut.println(message);
                        tempOut.flush();
                        //Echo the event to the server console
                        System.out.println("Sent to: " + s.getLocalAddress().getHostName());
                    }
                }
            }
            finally {
                SOCKET.close();
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
