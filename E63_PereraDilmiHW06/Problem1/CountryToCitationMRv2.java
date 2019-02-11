package edu.hu.bigdata;

import java.io.IOException;
//import java.util.Iterator;  -- OLD
import java.lang.InterruptedException; // NEW

//import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

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

public class CountryToCitationMRv2 extends Configured implements Tool { 

	public static class CountryToCitationMapper extends Mapper<Object, Text, Text, Text> {

		
		//public void map(Text key, Text value, Context context)
			//	throws IOException, InterruptedException {
			//context.write(value, key);
		//}
		
		public void map(Object key, Text value, Context context
                ) throws IOException, InterruptedException {
		  String set[] = value.toString().split(",");
		  String country=set[4].replace("\"", "");
		  if(country.equals("US")){country=set[4].replace("\"", "")+"-"+set[5].replace("\"", "");}
		  String citation = set[0];
		  context.write(new Text(country), new Text(citation));
		}
	} 

	/*public static class CountryToCitationReducer extends Reducer<Text, Text, Text, IntWritable> {

		public void reduce(Text key, Iterable<Text> values, Context context) 
			throws IOException, InterruptedException {

			int count = 0;
			for (Text val : values) { // Iterable allows for looping
				count++;
			}
			context.write(key, new IntWritable(count));
		}
	} */
	 public static class CountryToCitationReducer 
     extends Reducer<Text,Text,Text,Text> {
		  public void reduce(Text key, Iterable<Text> values, 
		                     Context context
		                     ) throws IOException, InterruptedException {
			  String csv = "";
			  while (values.iterator().hasNext()) {
				  if (csv.length() > 0) csv += ",";
				  csv += values.iterator().next().toString();
			  }
		    
		    context.write(key, new Text(csv));
		  }
	 }

	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		Path in = new Path(args[0]);
		Path out = new Path(args[1]);

		//conf.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator", ",");
		Job job = new Job(conf, "CountryToCitationMRv2");
		job.setJarByClass(CountryToCitationMRv2.class);

		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, out);
		job.setMapperClass(CountryToCitationMapper.class);
		job.setReducerClass(CountryToCitationReducer.class);
		job.setMapOutputKeyClass(Text.class); 
		job.setMapOutputValueClass(Text.class); 
		//job.setInputFormatClass(KeyValueTextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		boolean success = job.waitForCompletion(true);
		if (success)
			return 0;
		else 
			return 1;
	} 

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new CountryToCitationMRv2(),
				args);
		System.exit(res);
	} 

}
