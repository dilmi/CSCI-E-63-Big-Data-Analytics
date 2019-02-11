/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tcgadataanalysispipeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 *
 * @author Dilmi
 */
public class DataSetReader {
    static int numofVars = 0;
    static String varSeperator = ",";
    
    

       
    public static String[][] readDataSet(String link, int numOfVariables, String variableSeperator){
        numofVars = numOfVariables;
        varSeperator = variableSeperator;
        String []lines = readLines(link);
        String[][] data = extractVariables(lines);
        return data;
     }
    public static String[][] readDataSet(String link, int numOfVariables, String variableSeperator, int start, int stop){
        numofVars = numOfVariables;
        varSeperator = variableSeperator;
        String []lines = readLines(link , start, stop);
        String[][] data = extractVariables(lines);
        return data;
     }
    
    //Reads the text file line by line and outputs a String array of lines
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
    public static String[] readLines(String link,int start, int stop){
        FileReader fr = null;
        BufferedReader br = null;
        int lineCounter = 0;
        String text[] = new String[200];
        int counter = 0;
        try {
            fr = new FileReader(new File(link));
            br = new BufferedReader(fr);
            String line = "";
            while(lineCounter<=stop ){
                line = br.readLine();
                if(!line.equals("")){
                    if(lineCounter>=start){
                       text[counter] = line; 
                       counter++;
                    }
                     lineCounter++;                   
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
        
        return text;
     }


    //Varables are extracted from the data and given as a double array of strings
    public static String[][] extractVariables(String[] lines) {
        String[][] data = new String[lines.length][numofVars];
        String line;
        for(int i = 0 ; i < lines.length ; i++) {
            line = lines[i];
            data [i] = seperateVaraiables(line);
        }
        return data;
    }

    //Variables are extracted from a given line
    public static String[] seperateVaraiables(String line){
        String []variables = new String[numofVars];
        String temp;
        int i = 0;
        while(true){
            if(line.indexOf(varSeperator)!= -1){
            variables[i] = line.substring(0, line.indexOf(varSeperator)).trim();
            temp = line. substring(line.indexOf(varSeperator)+1).trim();
            line = temp;
            i++;
            }
             else{
                variables[i] = line;
                break;
             }
        }

        return variables;
    }
    
}
