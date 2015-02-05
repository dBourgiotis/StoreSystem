/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverstore;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.util.HashMap;
/**
 *
 * @author dimbourgiotis
 */
public class ServerStore {

    private HashMap workerHas = new HashMap();//saves keys and values
    private HashMap masterHas = new HashMap();//saves keys and keyLocations
    private static int flag = 1;// for dividing save actions in all the workers
    
     public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/store", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
     
     

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response = "Put a key to get its value or put a key and its value to store it";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    public boolean addKeyValue(String key , String value){
        if(masterHas.containsKey(key)){
            return false;//this key is already saved
        }else{
            switch(flag){
                case 1:{
                    workerHas.put(key, value);
                    masterHas.put(key, flag );// anti gia flag tha bei se string to Url
                    flag = 2;
                    return true;
                }
                case 2:{
                    //connect to socket and send the key value
                    masterHas.put(key, flag );// anti gia flag tha bei se string to Url
                    flag = 3;
                    return true;
                }
                case 3:{
                    //connect to socket and send the key value
                    masterHas.put(key, flag );// anti gia flag tha bei se string to Url
                    flag = 1;
                    return true;
                }
                default:{
                    return false;
                }                    
            }                        
        } 
    }
    
}
