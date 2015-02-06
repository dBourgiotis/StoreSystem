/* // Intializations
// 1) to programma syndeetai me tous workers
// 2) ftiaxnei thyra gia http requests
// // Logic (WHILE)
// 3) Perimenei request key
// 4) Syllegei plhrofories ap tous workers
// 5) Epistrefei value
// // (END_OF_WHILE) Terminate connections
// 6) Kleinei ta connections twn streams kai twn socket
// 7) Termatizei
*/

package serverstore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerStore {

    public static void main(String[] args) throws Exception {
        
        final int portNumber = 8000;
        final int maxUsers = 2;

        try ( 
            ServerSocket serverSocket = new ServerSocket( portNumber, maxUsers);
            //printConnectionInfo(portNumber, maxUsers);    
            Socket clientSocket = serverSocket.accept();
                
            PrintWriter clientOutput =
                new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader clientInput = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        ) { 
            clientOutput.println("Server: Connection Established! \n");
            System.out.println(">> Connection Established! \n");
            //
            //programm logic goes here
            //
            serverSocket.close();
        }
        
    } // main
        
} // class
