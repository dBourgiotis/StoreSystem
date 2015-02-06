/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import serverstore.ServerStore;

/**
 *
 * @author dimbourgiotis
 */
public  class HandleHelper {
    
    static HashMap workerHas = new HashMap();//saves keys and values
    static HashMap masterHas = new HashMap();//saves keys and keyLocations
    static int flag = 1;// for dividing save actions in all the workers
    
    public static boolean addKeyValue(String key , String value){
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
    
    public static String getValue(String key){
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
    
    public static void saveInFileWorker(String key, String value ){
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
    
    public static void saveInFileMaster(String key, String node ){
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
