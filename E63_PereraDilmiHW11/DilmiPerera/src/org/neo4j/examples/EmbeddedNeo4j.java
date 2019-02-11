/**
	 * Licensed to Neo Technology under one or more contributor
	 * license agreements. See the NOTICE file distributed with
	 * this work for additional information regarding copyright
	 * ownership. Neo Technology licenses this file to you under
	 * the Apache License, Version 2.0 (the "License"); you may
	 * not use this file except in compliance with the License.
	 * You may obtain a copy of the License at
	 *
	 * http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing,
	 * software distributed under the License is distributed on an
	 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
	 * KIND, either express or implied. See the License for the
	 * specific language governing permissions and limitations
	 * under the License.
	 */
	

	package org.neo4j.examples;
	

	import java.io.File;
import java.io.IOException;
	



import java.util.Map;
import java.util.Map.Entry;

	import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;
	

	public class EmbeddedNeo4j
	{
	    private static final String DB_PATH = "DilmiPerera";
	

	    public String greeting;
	

	    // START SNIPPET: vars
	    GraphDatabaseService graphDb;
	    Node firstNode;
	    Node secondNode;
	    Node thirdNode;
	    Node fourthNode;
	    Node fifthNode;
	    Node sixthNode;
	    Node seventhNode;
	    Node eigthNode;
	    Node ninthNode;
	    Node tenthNode;
	    Relationship relationship1;
	    Relationship relationship2;
	    // END SNIPPET: vars
	

	    // START SNIPPET: createReltype
	    private static enum RelTypes implements RelationshipType
	    {
	        CITES
	    }
	    // END SNIPPET: createReltype
	
	 // START SNIPPET: createReltype
	    private static enum RelTypes2 implements RelationshipType
	    {
	        SEQUELOF
	    }
	    // END SNIPPET: createReltype

	    public static void main( final String[] args ) throws IOException
	    {
	    	System.out.println("Creating new object of class \"EmbeddedNeo4j\" called \"library\"");
	        EmbeddedNeo4j library = new EmbeddedNeo4j();
	        System.out.println("Creating Database using createdb method");
	        library.createDb();
	        /*System.out.println("Remove data from database");
	        library.removeData();
	        library.shutDown();
	        System.out.println("Delete database file");
	    	FileUtils.deleteRecursively( new File( DB_PATH ) );*/
	    }
	

	    void createDb() throws IOException
	    {
	        
	        FileUtils.deleteRecursively( new File( DB_PATH ) );
	

	        // START SNIPPET: startDb
	        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
	        registerShutdownHook( graphDb );
	        // END SNIPPET: startDb
	

	        // START SNIPPET: transaction
	        try ( Transaction tx = graphDb.beginTx() )
	        {
	            // Database operations go here
	            // END SNIPPET: transaction
	            // START SNIPPET: addData
	        	
	            firstNode = graphDb.createNode();
	            firstNode.setProperty( "title", "Book1" );
	            firstNode.setProperty( "Author", "Author1" );
	            firstNode.setProperty( "year", "2001" );
	            
	            secondNode = graphDb.createNode();
	            secondNode.setProperty( "title", "Book2" );
	            secondNode.setProperty( "Author", "Author2" );
	            secondNode.setProperty( "year", "2002" );
	            
	            thirdNode = graphDb.createNode();
	            thirdNode.setProperty( "title", "Book3" );
	            thirdNode.setProperty( "Author", "Author3" );
	            thirdNode.setProperty( "year", "2003" );
	            
	            fourthNode = graphDb.createNode();
	            fourthNode.setProperty( "title", "Book4" );
	            fourthNode.setProperty( "Author", "Author1" );
	            fourthNode.setProperty( "year", "2001" );
	            
	            fifthNode = graphDb.createNode();
	            fifthNode.setProperty( "title", "Book5" );
	            fifthNode.setProperty( "Author", "Author1" );
	            fifthNode.setProperty( "year", "2005" );
	            
	            sixthNode = graphDb.createNode();
	            sixthNode.setProperty( "title", "Book6" );
	            sixthNode.setProperty( "Author", "Author6" );
	            sixthNode.setProperty( "year", "2001" );
	            
	            seventhNode = graphDb.createNode();
	            seventhNode.setProperty( "title", "Book7" );
	            seventhNode.setProperty( "Author", "Author1");
	            seventhNode.setProperty("year", "2007")  ;
	            
	            eigthNode = graphDb.createNode();
	            eigthNode.setProperty( "title", "Book8" );
	            eigthNode.setProperty( "Author", "Author8" );
	            eigthNode.setProperty( "year", "2008" );
	            
	            ninthNode = graphDb.createNode();
	            ninthNode.setProperty( "title", "Book9" );
	            ninthNode.setProperty( "Author", "Author1" );
	            ninthNode.setProperty( "year", "2001" );
	            
	            tenthNode = graphDb.createNode();
	            tenthNode.setProperty( "title", "Book10" );
	            tenthNode.setProperty( "Author", "Author1" );
	            tenthNode.setProperty( "year", "2001" );
	

	            relationship1 = firstNode.createRelationshipTo( secondNode, RelTypes.CITES );
	            relationship1.setProperty( "type", "cites" );
	            relationship2 = seventhNode.createRelationshipTo( fifthNode, RelTypes2.SEQUELOF );
	            relationship2.setProperty( "type", "sequel of" );
	            // END SNIPPET: addData
	            
	            
	            System.out.println("***************************Query Database***************************");
	            String rows="";
	            	      Result result = graphDb.execute( "match (n) return n.title, n.Author,n.year;" ) ;
	            	
	            	    while ( result.hasNext() )
	            	    {
	            	        Map<String,Object> row = result.next();
	            	        for ( Entry<String,Object> column : row.entrySet() )
	            	        {
	            	            rows += column.getKey() + ": " + column.getValue() + "; ";
	            	        }
	            	        rows += "\n";
	            	    }
	            	
	            System.out.println(rows);
	        
	            /*
	           
	           	System.out.print( "(:Book "+"{title: \""+firstNode.getProperty( "title" )+"}"+
	           			"{Author: \""+firstNode.getProperty( "Author" )+"}"+
           				"{year: \""+firstNode.getProperty( "year" )+"})" );
	            System.out.print( " - ["+relationship1.getProperty( "type" ) +"] -> ");
	            System.out.print( "(:Book "+"{title: \""+secondNode.getProperty( "title" )+"}"+
		        		"{Author: \""+secondNode.getProperty( "Author" )+"}"+
		        		"{year: \""+secondNode.getProperty( "year" )+"} )" );
	            
	            System.out.println();
	            
	            System.out.print( "(:Book "+"{title: \""+thirdNode.getProperty( "title" )+"}"+
		        		"{Author: \""+thirdNode.getProperty( "Author" )+"}"+
		        		"{year: \""+thirdNode.getProperty( "year" )+"} -" );
	            
	            System.out.println();
	            
	            System.out.print( "(:Book "+"{title: \""+fourthNode.getProperty( "title" )+"}"+
		        		"{Author: \""+fourthNode.getProperty( "Author" )+"}"+
		        		"{year: \""+fourthNode.getProperty( "year" )+"} -" );
	            
	            System.out.println();
	            
	            System.out.print( "(:Book "+"{title: \""+seventhNode.getProperty( "title" )+"}"+
		        		"{Author: \""+seventhNode.getProperty( "Author" )+"}"+
		        		"{year: \""+seventhNode.getProperty( "year" )+"})" );
		        System.out.print( " - ["+relationship2.getProperty( "type" ) +"] -> ");
		        System.out.print( "(:Book "+"{title: \""+fifthNode.getProperty( "title" )+"}"+
			        	"{Author: \""+fifthNode.getProperty( "Author" )+"}"+
			        	"{year: \""+fifthNode.getProperty( "year" )+"} )" );
		        
		        System.out.println();
	            
	            System.out.print( "(:Book "+"{title: \""+sixthNode.getProperty( "title" )+"}"+
		        		"{Author: \""+sixthNode.getProperty( "Author" )+"}"+
		        		"{year: \""+sixthNode.getProperty( "year" )+"} -" );
	            
	            System.out.println();
	            
	            System.out.print( "(:Book "+"{title: \""+eigthNode.getProperty( "title" )+"}"+
		        		"{Author: \""+eigthNode.getProperty( "Author" )+"}"+
		        		"{year: \""+eigthNode.getProperty( "year" )+"} -" );
	            
	            System.out.println();
	            
	            System.out.print( "(:Book "+"{title: \""+ninthNode.getProperty( "title" )+"}"+
		        		"{Author: \""+ninthNode.getProperty( "Author" )+"}"+
		        		"{year: \""+ninthNode.getProperty( "year" )+"} -" );
	            
	            System.out.println();
	            
	            System.out.print( "(:Book "+"{title: \""+tenthNode.getProperty( "title" )+"}"+
		        		"{Author: \""+tenthNode.getProperty( "Author" )+"}"+
		        		"{year: \""+tenthNode.getProperty( "year" )+"} -" );
	*/
/*
	            // START SNIPPET: readData
	            System.out.print( firstNode.getProperty( "title" )+"\t" );
	            System.out.print( relationship1.getProperty( "type" ) +"\t");
	            System.out.println( secondNode.getProperty( "title" ) );
	            // END SNIPPET: readData
	

	            greeting = ( (String) firstNode.getProperty("title" ) )
	                       + ( (String) relationship1.getProperty( "type" ) )
	                       + ( (String) secondNode.getProperty(  "title"  ) );
	            
	            
	         // START SNIPPET: readData
	            System.out.print( seventhNode.getProperty( "title" ) +"\t");
	            System.out.print( relationship2.getProperty( "type" ) +"\t");
	            System.out.print( fifthNode.getProperty( "title" ) );
	            // END SNIPPET: readData
	

	            greeting = ( (String) seventhNode.getProperty("title" ) )
	                       + ( (String) relationship2.getProperty( "type" ) )
	                       + ( (String) fifthNode.getProperty(  "title"  ) );
	*/

	            // START SNIPPET: transaction
	            tx.success();
	        }
	        // END SNIPPET: transaction
	    }
	

	    void removeData()
	    {
	        try ( Transaction tx = graphDb.beginTx() )
	        {
	            // START SNIPPET: removingData
	            // let's remove the data
	        	System.out.println("Remove relationship between Book1 and Book2");
	            firstNode.getSingleRelationship( RelTypes.CITES, Direction.OUTGOING ).delete();
	            System.out.println("Remove relationship between Book5 and Book7");
	            seventhNode.getSingleRelationship( RelTypes2.SEQUELOF, Direction.OUTGOING ).delete();
	            System.out.println("Remove Book1");
	            firstNode.delete();
	            System.out.println("Remove Book2");
	            secondNode.delete();
	            System.out.println("Remove Book3");
	            thirdNode.delete();
	            System.out.println("Remove Book4");
	            fourthNode.delete();
	            System.out.println("Remove Book5");
	            fifthNode.delete();
	            System.out.println("Remove Book6");
	            sixthNode.delete();
	            System.out.println("Remove Book7");
	            seventhNode.delete();
	            System.out.println("Remove Book8");
	            eigthNode.delete();
	            System.out.println("Remove Book9");
	            ninthNode.delete();
	            System.out.println("Remove Book10");
	            tenthNode.delete();
	            System.out.println();
	            
	            System.out.println("***************************Query Database***************************");
	            String rows="";
	            	      Result result = graphDb.execute( "match (n) return n;" ) ;
	            	
	            	    while ( result.hasNext() )
	            	    {
	            	        Map<String,Object> row = result.next();
	            	        for ( Entry<String,Object> column : row.entrySet() )
	            	        {
	            	            rows += column.getKey() + ": " + column.getValue() + "; ";
	            	        }
	            	        rows += "\n";
	            	    }
	            	
	            System.out.println(rows);
	            // END SNIPPET: removingData
	

	            tx.success();
	        }
	    }
	

	    void shutDown()
	    {
	        System.out.println();
	        System.out.println( "Shutting down database ..." );
	        // START SNIPPET: shutdownServer
	        graphDb.shutdown();
	        // END SNIPPET: shutdownServer
	    }
	

	    // START SNIPPET: shutdownHook
	    private static void registerShutdownHook( final GraphDatabaseService graphDb )
	    {
	        // Registers a shutdown hook for the Neo4j instance so that it
	        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	        // running application).
	        Runtime.getRuntime().addShutdownHook( new Thread()
	        {
	            @Override
	            public void run()
	            {
	                graphDb.shutdown();
	            }
	        } );
	    }
	    // END SNIPPET: shutdownHook
	}

