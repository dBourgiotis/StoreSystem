package serverstore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Worker {

    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    
    Worker( ServerSocket serverSocket ) {
        
        /* Connect Sockets */
        try {
            socket = serverSocket.accept();
        } catch ( Exception e) {
            System.err.println("Connection cannot be established!");
            System.exit( serverSocket.getLocalPort() );
        }
        
        /* Redirect Streams */
        try {
            out =
                new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        } catch ( Exception stream) {
            System.err.println("Client streams cannot be redirected!");
        } 
        
        /* Inform for connection status */
        sendMessage ( "Connection Established!" );
    }
    
    public void sendMessage ( String message ) { 
        System.out.println(">>" + message );
        out.println( message );
    }
    
}
