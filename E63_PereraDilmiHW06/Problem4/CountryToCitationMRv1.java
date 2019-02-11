package edu.hu.bigdata;
import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.KeyValueTextInputFormat;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import edu.hu.bigdata.InverterCounterMRv1.InverterCounterMapper;
import edu.hu.bigdata.InverterCounterMRv1.InverterCounterReducer;

public class CountryToCitationMRv1 extends Configured implements Tool {
    
    public static class CountryToCitationMapper extends MapReduceBase
        implements Mapper<Text, Text, Text, Text> {
        
        public void map(Text key, Text value,
                        OutputCollector<Text, Text> output,
                        Reporter reporter) throws IOException {
        	String set[] = value.toString().split(",");
        	String country=set[3].replace("\"", "");
    	  	if(country.equals("US")){country=set[3].replace("\"", "")+"-"+set[4].replace("\"", "");}
            output.collect(new Text(country), key);
        }
    } 
    
    public static class CountryToCitationReducer extends MapReduceBase
        implements Reducer<Text, Text, Text, Text> {
        
        public void reduce(Text key, Iterator<Text> values,
                           OutputCollector<Text, Text> output,
                           Reporter reporter) throws IOException {
                           
            String csv = "";
            while (values.hasNext()) {
				  if (csv.length() > 0) csv += ",";
				  csv += values.next().toString();
			  }
            output.collect(key, new Text(csv));
        }
    }
    
    public static class InverterCounterMapper extends MapReduceBase
    implements Mapper<Text, Text, IntWritable, Text> {
    
    public void map(Text key, Text value,
                    OutputCollector<IntWritable, Text> output,
                    Reporter reporter) throws IOException {
                
        output.collect(new IntWritable(value.toString().split(",").length), key);
    }
} 

public static class InverterCounterReducer extends MapReduceBase
    implements Reducer<IntWritable, Text, IntWritable, IntWritable> {
    
    public void reduce(IntWritable key, Iterator<Text> values,
                       OutputCollector<IntWritable, IntWritable> output,
                       Reporter reporter) throws IOException {
                       
        int count = 0;
        while (values.hasNext()) {
            values.next();
            count++;
        }
        output.collect(key, new IntWritable(count));
    }
}  

    
    public int run(String[] args) throws Exception {
        Configuration conf = getConf();      
        Path in = new Path(args[0]);
        Path out = new Path(args[1]);
        Path temp = new Path("chain-temp");

        
        JobConf job = new JobConf(conf, CountryToCitationMRv1.class);    
        FileInputFormat.setInputPaths(job, in);
        FileOutputFormat.setOutputPath(job, temp);        
        job.setJobName("CountryToCitationMRv1");
        job.setMapperClass(CountryToCitationMapper.class);
        job.setReducerClass(CountryToCitationReducer.class);   
        job.setInputFormat(KeyValueTextInputFormat.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
        job.setOutputFormat(TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        //job.setOutputValueClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.set("key.value.separator.in.input.line", ","); 
        
        
        
        RunningJob runningJob = JobClient.runJob(job);      
        boolean success = runningJob.isSuccessful();
		if (success){
			JobConf job2 = new JobConf(conf, CountryToCitationMRv1.class);    
	        FileInputFormat.setInputPaths(job2, temp);
	        FileOutputFormat.setOutputPath(job2, out);        
	        job2.setJobName("InverterCounterMRv1");
	        job2.setMapperClass(InverterCounterMapper.class);
	        job2.setReducerClass(InverterCounterReducer.class);   
	        job2.setInputFormat(KeyValueTextInputFormat.class);
			job2.setMapOutputKeyClass(IntWritable.class);
			job2.setMapOutputValueClass(Text.class);
	        job2.setOutputFormat(TextOutputFormat.class);
	        job2.setOutputKeyClass(IntWritable.class);
	        //job.setOutputValueClass(Text.class);
	        job2.setOutputValueClass(IntWritable.class);
	        job2.set("key.value.separator.in.input.line", ","); 
	        
	        runningJob = JobClient.runJob(job2); 
	        success = runningJob.isSuccessful();
	        
	        if(success)
	        	return 0;
	    		else 
    			return 1;
	          
	        }
		
			
		else 
			return 1;
    }  
    public static void main(String[] args) throws Exception { 
        int res = ToolRunner.run(new Configuration(), new CountryToCitationMRv1(), args);       
        System.exit(res);
    }
}
