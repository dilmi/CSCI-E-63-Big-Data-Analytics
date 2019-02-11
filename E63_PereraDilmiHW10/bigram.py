import string
from pyspark import SparkConf, SparkContext

conf = SparkConf().setMaster("local").setAppName("YourApp")
sc = SparkContext(conf = conf)
file = sc.textFile("file:/home/cloudera/input/all-bible")
counts = file.flatMap(lambda line: ['{0} {1}'.format(i[0], i[1]) for i in zip(line.split()[:-1], line.split()[1:]) if not i[0].endswith('.')]).map(lambda word: (word.encode('utf-8','ignore').translate(None, string.punctuation).lower(), 1)).reduceByKey(lambda a, b: a + b)
counts.saveAsTextFile("file:/home/cloudera/output2.0")




