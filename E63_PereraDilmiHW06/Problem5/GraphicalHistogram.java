package edu.hu.bigdata;

import java.io.IOException;
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

public class GraphicalHistogram extends Configured implements Tool {
    
    public static class MapClass extends MapReduceBase
        implements Mapper<Text, Text, Text, IntWritable> {
        
        
        
        public void map(Text key, Text value,
                        OutputCollector<Text, IntWritable> output,
                        Reporter reporter) throws IOException {
             try{
            	 int count = Integer.parseInt(key.toString());
            	 int bucket = (int)(count-(count%20))/20;
            	 //if(count>0){
            		 output.collect(new Text(String.valueOf((bucket*20)+1)+"-"+String.valueOf(bucket*20+20)), new IntWritable(Integer.parseInt(value.toString())));
            	 //}
            }
             catch(NumberFormatException e){
            	 //skip
             }
            
            
        }
    }
    
    public static class Reduce extends MapReduceBase
        implements Reducer<Text,IntWritable,Text,Text>
    {
        
        public void reduce(Text key, Iterator<IntWritable> values,
                           OutputCollector<Text, Text>output,
                           Reporter reporter) throws IOException {
                           
            int no_patents_cited = 0;
            while (values.hasNext()) {
                no_patents_cited += values.next().get();
            }
            int numOfStars=(int) (Math.log10(no_patents_cited+1)*4);
            String stars="";
            for(int i=0;i<numOfStars;i++){
            	stars+="*";
            }
            
            output.collect(key, new Text(stars));
        }
    }
    
    public int run(String[] args) throws Exception {
        Configuration conf = getConf();
        JobConf job = new JobConf(conf, GraphicalHistogram.class);
        Path in = new Path(args[0]);
        Path out = new Path(args[1]);
        FileInputFormat.setInputPaths(job, in);
        FileOutputFormat.setOutputPath(job, out);        
        job.setJobName("GraphicalHistogram");
        job.setMapperClass(MapClass.class);
        job.setReducerClass(Reduce.class);
        job.setMapOutputKeyClass(Text.class); 
		job.setMapOutputValueClass(IntWritable.class); 
        job.setInputFormat(KeyValueTextInputFormat.class);
        job.setOutputFormat(TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        
        JobClient.runJob(job);
        
        return 0;
    }
    
    public static void main(String[] args) throws Exception { 
        int res = ToolRunner.run(new Configuration(), 
                                 new GraphicalHistogram(), 
                                 args);
        System.exit(res);
    }
}