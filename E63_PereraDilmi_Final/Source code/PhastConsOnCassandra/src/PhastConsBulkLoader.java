/**
 * 
 */

/**
 * @author Dilmi
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;
import org.apache.cassandra.config.Config;
import org.apache.cassandra.exceptions.InvalidRequestException;
import org.apache.cassandra.io.sstable.CQLSSTableWriter;

/*
 * Usage: java bulkload.BulkLoad
 */
public class PhastConsBulkLoader {
  
    /** Default output directory */
	public static final String homeDir="/home/dilmi/data/PhastCons/";
	//
	///Users/dilmiperera/Desktop/BigDataAnalytics/FinalProject/
    public static final String DEFAULT_OUTPUT_DIR =homeDir+"sstabledata";

     /** Keyspace name */
    public static final String KEYSPACE = "dev";
    /** Table name */
    public static final String TABLE = "phastConsBulkLoad";

    /**
     * Schema for bulk loading table.
     * It is important not to forget adding keyspace name before table name,
     * otherwise CQLSSTableWriter throws exception.
     */
    public static final String SCHEMA = String.format("CREATE TABLE %s.%s (" +
                                                          "chrom varchar, " +
                                                          "base int, " +
                                                          "score double, " +
                                                          "PRIMARY KEY (chrom, base) " +
                                                      ") WITH CLUSTERING ORDER BY (base ASC)", KEYSPACE, TABLE);

    /**
     * INSERT statement to bulk load.
     * It is like prepared statement. You fill in place holder for each data.
     */
    public static final String INSERT_STMT = String.format("INSERT INTO %s.%s (" +
                                                               "chrom , base , score" +
                                                           ") VALUES (" +
                                                               "?, ?, ?" +
                                                           ")", KEYSPACE, TABLE);

    public static void bulkload(String[] args)
    {
        
        // magic!
        Config.setClientMode(true);

        // Create output directory that has keyspace and table name in the path
        File outputDir = new File(DEFAULT_OUTPUT_DIR + File.separator + KEYSPACE + File.separator + TABLE);
        if (!outputDir.exists() && !outputDir.mkdirs())
        {
            throw new RuntimeException("Cannot create output directory: " + outputDir);
        }

        // Prepare SSTable writer
        CQLSSTableWriter.Builder builder = CQLSSTableWriter.builder();
        // set output directory
        builder.inDirectory(outputDir)
               // set target schema
               .forTable(SCHEMA)
               // set CQL statement to put data
               .using(INSERT_STMT);
        CQLSSTableWriter writer = builder.build();

        
        	FileReader fr;
            
            try
            {
                fr = new FileReader(homeDir+"chr1.phastCons46way.placental.wigFix.reduced.csv");
                //
                
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

            try (
                BufferedReader reader = new BufferedReader(fr);
                CsvListReader csvReader = new CsvListReader(reader, CsvPreference.STANDARD_PREFERENCE)
            )
            {
                

                csvReader.getHeader(false);

                // Write to SSTable while reading data
                List<String> line;
                while ((line = csvReader.read()) != null)
                {
                    // We use Java types here based on
                    // http://www.datastax.com/drivers/java/2.0/com/datastax/driver/core/DataType.Name.html#asJavaClass%28%29
                    writer.addRow(line.get(0),
                                  new Integer(line.get(1)),
                                  new Double(line.get(2)));
                }
            }
            catch (InvalidRequestException | IOException e)
            {
                e.printStackTrace();
            }
        

        try
        {
            writer.close();
        }
        catch (IOException ignore) {}
    }
}
