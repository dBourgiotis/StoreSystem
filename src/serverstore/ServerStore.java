/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverstore;


import Helpers.HandleHelper;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author dimbourgiotis
 */
public class ServerStore {
    
    
    final static int portNumber = 8000;
    final static int maxUsers = 2;

    
     public static void main(String[] args) throws Exception {
        HandleHelper myHelper = new HandleHelper();
        myHelper.loadMaster();
        myHelper.loadWorker();
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/info", new MyHandler());
        server.createContext("/store", new GetHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("The server is running");
    }
     
     

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response = " Put a key like /store/key=yourKey to get its value or put a key and its value like this /store/key=yourKey&value=yourValue to store it";
            System.out.println("Info Response");
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    static class GetHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            //edw arxika 8a psaxnei a tou dinei mono key kai 8a emfanizei to value diaforetika 8a to apo8hkeuei se ena apo ta hashmaps            
            String uri = t.getRequestURI().toString();
            String response = uri;
            String[] parts = uri.split("/");
            if(parts.length>3 || parts.length<3)
                response = "Wrong uri please go to /info for more information";
            else{
                if(parts[2].contains("&")){//if so, it is a put request
                    String[] subParts = parts[2].split("&");
                    if(subParts.length>2)
                        response = "Wrong uri please go to /info for more information";
                    else{
                        String[] key = subParts[0].split("=");
                        String[] value = subParts[1].split("=");
                        if( key.length !=2 || value.length != 2)
                            response = "Wrong uri please go to /info for more information";
                        else{
                            response = "Key = "+key[1]+", Value = "+value[1] ;
                            System.out.println("Put Request");
                            //*************************edw tha ginetai h apo8hkeush***********************
                        }
                    }
                }
                else if(parts[2].contains("=")){//get request
                    String[] subParts = parts[2].split("=");
                    if(subParts.length!=2)
                        response = "Wrong uri please go to /info for more information";
                    else{
                        response = "Key = "+subParts[1] ;
                            System.out.println("Get request");
                            //*************************edw tha ginetai  to load***********************                        
                    }
                }
                else{
                    response = "Wrong uri please go to /info for more information";
                }                
            }            
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    } 
    
    public static boolean  workerConnection() throws IOException{
        
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
        
        return true;
    }
}