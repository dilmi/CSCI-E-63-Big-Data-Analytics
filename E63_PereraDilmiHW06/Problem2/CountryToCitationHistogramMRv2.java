package edu.hu.bigdata;

import java.io.IOException;
//import java.util.Iterator;  -- OLD
import java.lang.InterruptedException; // NEW

//import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;

//import org.apache.hadoop.mapred.FileInputFormat; -- OLD
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat; // NEW

//import org.apache.hadoop.mapred.FileOutputFormat; -- OLD
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat; // NEw

//import org.apache.hadoop.mapred.JobClient; -- OLD
//import org.apache.hadoop.mapred.JobConf; -- OLD
import org.apache.hadoop.mapreduce.Job; // NEW

//import org.apache.hadoop.mapred.KeyValueTextInputFormat; -- OLD
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat; // NEW

//import org.apache.hadoop.mapred.MapReduceBase;
//import org.apache.hadoop.mapred.Mapper;  -- OLD
import org.apache.hadoop.mapreduce.Mapper; // NEW

//import org.apache.hadoop.mapred.OutputCollector; -- OLD

//import org.apache.hadoop.mapred.Reducer; -- OLD
import org.apache.hadoop.mapreduce.Reducer; // NEW

//import org.apache.hadoop.mapred.Reporter; -- OLD
//import org.apache.hadoop.mapred.TextOutputFormat; -- OLD
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat; // NEW
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class CountryToCitationHistogramMRv2 extends Configured implements Tool { 

	public static class InverterCounterMapper1 extends Mapper<Text, Text, IntWritable, Text> {

		public void map(Text key, Text value, Context context)
				throws IOException, InterruptedException {
			String citationList[]=value.toString().split(",");
			context.write(new IntWritable(citationList.length), key);
		}
	} 

	public static class HistogramReducer1 extends Reducer<Text, Text, IntWritable, IntWritable> {

		public void reduce(IntWritable key, Iterable<Text> values, Context context) 
			throws IOException, InterruptedException {

			int count = 0;
			while (values.hasNext()) {
                values.next();
                count++;
            }
			
			context.write(key, new IntWritable(count));
		}
	} 

	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		Path in = new Path(args[0]);
		Path out = new Path(args[1]);

		//conf.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator", ",");
		Job job = new Job(conf, "CountryToCitationHistogram");
		job.setJarByClass(CountryToCitationHistogramMRv2.class);

		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, out);
		job.setMapperClass(InverterCounterMapper1.class);
		job.setReducerClass(HistogramReducer1.class);
		job.setMapOutputKeyClass(IntWritable.class); 
		job.setMapOutputValueClass(Text.class); 
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(IntWritable.class);
		
		boolean success = job.waitForCompletion(true);
		if (success)
			return 0;
		else 
			return 1;
	} 

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new CountryToCitationHistogramMRv2(),
				args);
		System.exit(res);
	} 

}
