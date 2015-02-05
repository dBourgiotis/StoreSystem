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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
                    masterHas.put(key, flag );
                    flag = 2;
                    return true;
                }
                case 2:{
                    //connect to socket and send the key value
                    masterHas.put(key, flag );
                    flag = 3;
                    return true;
                }
                case 3:{
                    //connect to socket and send the key value
                    masterHas.put(key, flag );
                    flag = 1;
                    return true;
                }
                default:{
                    return false;
                }                    
            }                        
        } 
    }
    
    public String getValue(String key){
        //exei ginei elegxos prin thn klhsh gia to an uparxei
        String value = new String();
        String node = masterHas.get(key).toString();
        if (node.equals("1")){
            value = workerHas.get(key).toString();
        }else if(node.equals("2")){
            //connect and get the value from there
        }else if(node.equals("3")){
            //connect and get the value from there
        }else
            value="false";
        //
        return value;
    }
    
    public void saveInFileWorker(String key, String value ){
        File yourFile = new File("worker.txt");
        if(!yourFile.exists()) {
            try {
                yourFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(ServerStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        try { 
            FileOutputStream oFile = new FileOutputStream(yourFile, false);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            FileWriter fw = new FileWriter("worker.txt",true); //the true will append the new data
            fw.write(key+"~"+value+"\n");//appends the string to the file
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }

    }    
}
