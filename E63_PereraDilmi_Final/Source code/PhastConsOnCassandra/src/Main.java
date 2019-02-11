import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.cassandra.exceptions.InvalidRequestException;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;




public class Main{
	public static void main(String args[]){
		PhastConsRetriever pcr = new PhastConsRetriever();
		
		FileReader fr;
		FileWriter fw;
		try {
			 fr = new FileReader(args[0]);
			 fw = new FileWriter(args[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Input file not found");
			return;
		}
		
		try (
                BufferedReader reader = new BufferedReader(fr);
                CsvListReader csvReader = new CsvListReader(reader, CsvPreference.STANDARD_PREFERENCE);
                BufferedWriter writer = new BufferedWriter(fw);
                CsvListWriter csvWriter = new CsvListWriter(writer, CsvPreference.STANDARD_PREFERENCE);
				
				)
            {
				List<String> line;
				pcr.init();
				
                //Read mutations data, retrieve conservation scores and write to file. 
                
                while ((line = csvReader.read()) != null)
                {
                	double score=0;
                	String chrom=line.get(0);
                	int start=Integer.parseInt(line.get(1));
                	int end=Integer.parseInt(line.get(2));
            		//System.out.println(chrom +"\t"+base);
            		score=pcr.getScore(chrom, start,end);
            		line.add(String.valueOf(score));
                	
                	csvWriter.write(line);
                }
                pcr.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
		
		
	}
	
	
}