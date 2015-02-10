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
        ServerSocket serverSocket = null;
        Socket clientSocket;
        Worker worker;

        try { 
            serverSocket = new ServerSocket( portNumber, maxUsers);
        } catch ( Exception ServerSocket) {
            System.err.println("Server Socket cannot be created!");
            System.exit(portNumber);
        } 
        
        try {
            printServerInfo(portNumber, maxUsers);    
        } catch(Exception serverInfo) {
            System.err.println("Server's Info cannot be shown!");
        }
        
        worker = new Worker( serverSocket );
        
            //
            //programm logic goes here
            //
        
        serverSocket.close();
        
    } // main
    
    private static void printServerInfo( int portNumber, int maxUsers) {
        System.out.println("Status Server: Ready!");
        System.out.println("  IP:"  );
        System.out.println("Port:"  );
    }
} // class
