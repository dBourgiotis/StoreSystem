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
            ServerSocket serverSocket = new ServerSocket(portNumber, maxUsers);
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
