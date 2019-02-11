import string
from pyspark import SparkConf, SparkContext
conf = SparkConf().setMaster("local").setAppName("YourApp")
sc = SparkContext(conf = conf)
file = sc.textFile("hdfs://localhost:8020/user/cloudera/input/all-bible")
counts = file.flatMap(lambda line: line.encode('utf-8','ignore').translate(None, string.punctuation).lower().split()).map(lambda word: (word, 1)).reduceByKey(lambda a, b: a + b)
counts.saveAsTextFile("hdfs://localhost:8020/user/cloudera/output1.3333")
