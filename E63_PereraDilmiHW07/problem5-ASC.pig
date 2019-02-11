register /usr/lib/pig/piggybank.jar;
A = LOAD 'output_problem4.6/part-r-00000' USING PigStorage('\t') as ( 
cited:chararray, citationCount:int); 
B = GROUP A BY citationCount;
C = FOREACH B GENERATE $0 as countC, $1.cited as citations;
D = FOREACH C GENERATE countC, COUNT(citations) as citationFreq;
E = ORDER D BY countC ASC;
STORE E into 'output_problem5.12';
