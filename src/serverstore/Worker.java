package serverstore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Worker {

    Socket socket = null;
    
    Worker( ServerSocket serverSocket ) {
        /* Connect Sockets */
        try {
            socket = serverSocket.accept();
        } catch ( Exception e) {
            System.err.println("Connection cannot be established!");
        }
        /* Redirect Streams */
        try {
            PrintWriter clientOutput =
                new PrintWriter(socket.getOutputStream(), true);
            BufferedReader clientInput = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        } catch ( Exception stream) {
            System.err.println("Client streams cannot be redirected!");
        } 

        System.out.println(">> Connection Established! \n");
    }
}
