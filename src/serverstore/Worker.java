package serverstore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Worker {

    private Socket socket = null;
    private PrintWriter clientOutput = null;
    private BufferedReader clientInput = null;
    
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
            clientOutput =
                new PrintWriter(socket.getOutputStream(), true);
            clientInput = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        } catch ( Exception stream) {
            System.err.println("Client streams cannot be redirected!");
        } 
        /* Inform for connection status */
        System.out.println(">> Connection Established! \n");
    }
}
