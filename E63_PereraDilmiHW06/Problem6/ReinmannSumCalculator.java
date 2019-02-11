package edu.hu.bigdata;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
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
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class ReinmannSumCalculator extends Configured implements Tool {
    
    public static class MapClass extends MapReduceBase
        implements Mapper<Text, Text, Text, Text> {
    	
        public void map(Text key, Text value,
                        OutputCollector<Text, Text> output,
                        Reporter reporter) throws IOException {
        	double deltaX = (10.0-1.0)/(10000-1);
        	try{
        		double x=Double.parseDouble(value.toString());
        		double fx = (1.0/x)*deltaX;
        		output.collect(new Text("Reinmann_sum"), new Text(String.valueOf(fx)));
        	}
        	catch(NumberFormatException e){
        		//output.collect(new Text("Reinmann_sum"), new Text("0"));
        	}
            
        }
    }
    
    public static class Reduce extends MapReduceBase
        implements Reducer<Text,Text,Text,Text>
    {
        
        public void reduce(Text key, Iterator<Text> values,
                           OutputCollector<Text, Text>output,
                           Reporter reporter) throws IOException {
                           
            double reinmann_sum=0.0;
            while (values.hasNext()) {
            	reinmann_sum+= Double.parseDouble(values.next().toString());
            }
            
            output.collect(key, new Text(String.valueOf(reinmann_sum)));
        }
    }
    
    public int run(String[] args) throws Exception {
        Configuration conf = getConf();
        JobConf job = new JobConf(conf, ReinmannSumCalculator.class);
        Path in = new Path(args[0]);
        Path out = new Path(args[1]);
        FileInputFormat.setInputPaths(job, in);
        FileOutputFormat.setOutputPath(job, out);        
        job.setJobName("ReinmannSumCalculator");
        job.setMapperClass(MapClass.class);
        job.setReducerClass(Reduce.class);
        job.setMapOutputKeyClass(Text.class); 
		job.setMapOutputValueClass(Text.class); 
        job.setInputFormat(KeyValueTextInputFormat.class);
        job.setOutputFormat(TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        
        JobClient.runJob(job);
        
        return 0;
    }
    
    public static void main(String[] args) throws Exception {
    	
        int res = ToolRunner.run(new Configuration(), 
                                 new ReinmannSumCalculator(), 
                                 args);
        System.exit(res);
    }
}
