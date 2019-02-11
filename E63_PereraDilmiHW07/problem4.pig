register /usr/lib/pig/piggybank.jar;
A = LOAD 'output_problem2.2/part-r-00000' USING PigStorage('\t') as ( 
cited:chararray, citingList:chararray); 
B = FOREACH A GENERATE cited, COUNT(TOKENIZE(citingList,',')) as citationCount;
C = ORDER B BY citationCount DESC; 
STORE C into 'output_problem4.6'; 
