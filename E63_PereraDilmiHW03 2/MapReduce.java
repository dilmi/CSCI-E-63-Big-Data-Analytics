/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapreduce;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author dilmiperera
 */
public class MapReduce {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        manager("/Users/dilmiperera/Downloads/temp.txt");
        // TODO code application logic here
    }
    public static String[] readLines(String link){
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList <String> text = new ArrayList <String>();
        try {
            fr = new FileReader(new File(link));
            br = new BufferedReader(fr);
            String line = "";
            while(true ){
                line = br.readLine();
                if(!line.equals("")){
                text.add(line);
                }
                else
                    break;
            }
        }
        catch (Exception ex) {}
        finally {
            try {
                br.close();
                fr.close();
            } catch (IOException ex) {
            }
        }
        String []lines = new String[text.size()];
        text.toArray(lines);
        return lines;
     }  
    
    
    public static void manager(String file) {
        String lines[]=readLines(file);
        Hashtable<String, String[]> toReduce=new Hashtable<>();
        System.out.println("read");
        for (int i = 0; i < lines.length; i++) {
            
            String key=String.valueOf(i+1);
            String value=lines[i].replace(".", " ").replace(",", " ").replace("/", " ").toLowerCase();
            String combinerOutput[][]=combiner(map(key,value));
            for (int j = 0; j < combinerOutput.length; j++) {
                if(i==0){
                    String temp[]=new String[1];
                    temp[0]=combinerOutput[j][1];
                    toReduce.put(combinerOutput[j][0], temp);
                }
                else if(combinerOutput[j][0]!=null&&toReduce.containsKey(combinerOutput[j][0])){
                    String temp[]=toReduce.get(combinerOutput[j][0]);
                    String temp2[]=new String[temp.length+1];
                    for (int k = 0; k < temp.length; k++) {
                        temp2[k]=temp[k];
                    }
                    temp2[temp.length]=combinerOutput[j][1];
                    toReduce.put(combinerOutput[j][0], temp2);
                }
                else if(combinerOutput[j][0]!=null){
                    String temp[]=new String[1];
                    temp[0]=combinerOutput[j][1];
                    toReduce.put(combinerOutput[j][0], temp);
                }                
            }
            
        }
        
        String [][] finalOutput=new String[toReduce.size()][2];
        int count=0;
        String keys[]=toReduce.keySet().toArray(new String[0]);
        System.out.println(""+toReduce.size());
        for (int i = 0; i < finalOutput.length; i++) {
            String key=keys[i];
            String values[]=toReduce.get(key);
            
                    String temp[]=reduce(key,values);
                    finalOutput[count][0]=temp[0];
                    finalOutput[count][1]=temp[1];
            System.out.println(""+finalOutput[count][0]+"\t"+finalOutput[count][1]);
            count++;
        }
////        while(toReduce.keys().hasMoreElements()){
////            
////            
////        }
        
        
    }
    
    public static String[][] map(String key, String value) {
        
        String interkeys[]=value.split(" ");
        String interkeysandvalues[][] = new String [interkeys.length][2];
        for (int i = 0; i < interkeysandvalues.length; i++) {
            interkeysandvalues[i][0]=interkeys[i].trim();
            interkeysandvalues[i][1]="1";
//            System.out.println(interkeysandvalues[i][0]+"\t"+interkeysandvalues[i][1]);
        }
//        System.out.println(""+interkeys[0]);
        return interkeysandvalues;
    }
    
    public static String[][] combiner(String[][] interkeysandvalues) {
        LinkedHashSet keys = new LinkedHashSet();
                
        for (int i = 0; i < interkeysandvalues.length; i++) {
            keys.add(interkeysandvalues[i][0]);
        }
        
//        System.out.println(""+keys.size());
        
        
        
        String uniqKeys[]=(String[]) keys.toArray(new String[0]);
        String output[][] = new String [uniqKeys.length][2];
//        System.out.println(""+uniqKeys.length);
        for (int i = 0; i < uniqKeys.length; i++) {
            output[i][0]=uniqKeys[i];
            int count=0;
            for (int j = 0; j < interkeysandvalues.length; j++) {
                if(output[i][0].equals(interkeysandvalues[j][0])){
                    count+=Integer.parseInt(interkeysandvalues[j][1]);
                }
            }
            output[i][1]=String.valueOf(count);
        }
        
        
        
        return output;
        
    }
    
    public static String[] reduce(String key, String values[]) {
        String output[]=new String[2];
        int count=0;
        for (int i = 0; i < values.length; i++) {
            count+=Integer.parseInt(values[i]);
        }
        output[0]=key;
        output[1]=String.valueOf(count);
//        System.out.println(""+count);
        return output;
        
    }
    
}
