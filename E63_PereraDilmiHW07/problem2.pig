register /usr/lib/pig/piggybank.jar;
DEFINE EXTRACT org.apache.pig.piggybank.evaluation.string.RegexExtractAll();
A = LOAD 'input_problem2.1' USING PigStorage(',') as ( 
citing:int, cited:int); 
B = GROUP A BY cited;
C = FOREACH B GENERATE $0, $1.citing;
STORE C into 'output_problem2.2';
