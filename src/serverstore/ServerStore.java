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

    private static HashMap workerHas = new HashMap();//saves keys and values
    private static HashMap masterHas = new HashMap();//saves keys and keyLocations
    private static int flag = 1;// for dividing save actions in all the workers
    
     public static void main(String[] args) throws Exception {
        loadMaster();
        loadWorker();
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
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    static class GetHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            //edw arxika 8a psaxnei a tou dinei mono key kai 8a emfanizei to value diaforetika 8a to apo8hkeuei se ena apo ta hashmaps
            String response = "";
            String uri = t.getRequestURI().toString();
            String[] parts = uri.split("/");
            if(parts.length>3)
                response = "Wrong uri please go to /info for more information";
            else{
                if(parts[2].contains("&")){//if so, it is a put request
                    String[] subParts = parts[2].split("&");
                    if(subParts.length>2)
                        response = "Wrong uri please go to /info for more information";
                    else{
                        String[] key = subParts[0].split("=");
                        String[] value = subParts[1].split("=");
                        if( key.length >2 || value.length > 2)
                            response = "Wrong uri please go to /info for more information";
                        else{
                            response = "Key = "+key[1]+", Value = "+value[1] ;
                            //*************************edw tha ginetai h apo8hkeush***********************
                        }
                    }
                }
                else if(parts[2].contains("=")){
                    String[] subParts = parts[2].split("=");
                    if(subParts.length>2)
                        response = "Wrong uri please go to /info for more information";
                    else{
                        String[] key = subParts[0].split("=");
                        if( key.length >2 )
                            response = "Wrong uri please go to /info for more information";
                        else{
                            response = "Key = "+key[1] ;
                            //*************************edw tha ginetai  to load***********************
                        }
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
    
    public void saveInFileMaster(String key, String node ){
        File yourFile = new File("master.txt");
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
            FileWriter fw = new FileWriter("master.txt",true); //the true will append the new data
            fw.write(key+"~"+node+"\n");//appends the string to the file
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }

    }
    
    
    public static void loadWorker(){
        File yourFile = new File("worker.txt");
        ArrayList<String> temp = new ArrayList<String>();
        if(yourFile.exists()){
            try {
                Scanner scan = new Scanner(yourFile);                
                while (scan.hasNextLine()) {
                       temp.add(scan.nextLine());
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ServerStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(!temp.isEmpty()){
            for(int i = 0 ; i< temp.size(); i ++){
                String string = temp.get(i);
                String[] parts = string.split("~");
                String key = parts[0];
                String value = parts[1];
                workerHas.put(key, value);
            }
        }
    }
    
    public static void loadMaster(){
        File yourFile = new File("master.txt");
        ArrayList<String> temp = new ArrayList<String>();
        if(yourFile.exists()){
            try {
                Scanner scan = new Scanner(yourFile);                
                while (scan.hasNextLine()) {
                       temp.add(scan.nextLine());
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ServerStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(!temp.isEmpty()){
            for(int i = 0 ; i< temp.size(); i ++){
                String string = temp.get(i);
                String[] parts = string.split("~");
                String key = parts[0];
                String value = parts[1];
                masterHas.put(key, value);
            }
        }
    }
    
}
