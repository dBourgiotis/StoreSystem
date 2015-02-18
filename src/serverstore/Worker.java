package serverstore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Worker {

    private final String serverIP = "127.0.0.1"; // ip of master
    private final int serverPort = 8000;         // port number of master
    private static Socket connectionToServer = null; 

    private String status = "Incomplete";
    private BufferedReader in;
    private BufferedReader stdIn;
    private PrintWriter out;

    
    Worker( ServerSocket serverSocket) {
        
        try {
            connectionToServer = new Socket( serverIP, serverPort);
        } catch ( Exception c ) {
            System.err.println("Couldn't connect to server!");
        }
        
        try { /* Redirect streams */
            in = new BufferedReader( /* Get Input from server*/
                    new InputStreamReader( connectionToServer.getInputStream()));
            stdIn = new BufferedReader( /* Redirect server's input to your default System.in */
                    new InputStreamReader(System.in));
            out = new PrintWriter( connectionToServer.getOutputStream(), true);
        } catch ( Exception streams) {
            
        }
        
        /* Print Info */
        System.out.println("Connection Established!");
        System.out.println(">>  IP: " + serverIP );
        System.out.println(">>Port: " + serverPort );
        System.out.println("");
        
        status = "ok";
    }
    
    public void getMessage() {
 
        try {
            System.out.println(">> " + in.readLine());       
        } catch ( Exception m ) { 
            System.err.println("Message cannot be read!"); 
        }

    }
    
    public void sendMessage() {
           
//        try {
//            System.out.println(">> " + in.readLine());       
//        } catch ( Exception m ) { 
//            System.err.println("Message cannot be sent!"); 
//        }        
    }
    
    public void close() {
        try {
            connectionToServer.close();
        } catch (Exception close) {
            System.err.println("Connection didn't close correctly!");
        }
    }  

    public String getStatus() { return status; }
    
} // class Connection
