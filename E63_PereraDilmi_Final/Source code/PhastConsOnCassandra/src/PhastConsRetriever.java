
/**
 * 
 */

/**
 * @author Dilmi
 *
 */
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.SocketOptions;


public class PhastConsRetriever {
	private Cluster cluster;
	private Session session;

	// Cassandra configuration parameters
	private int connectTimeoutMillis = 10000;
	private int readTimeoutMillis = 15000;
	// in real life, you would have more than one node, specified like this:
	// nodes = "mynode1,mynode2,mynode3"
	private String nodes = "localhost";
	private String keyspace = "dev";
	private int port = 9042;
	private String username = "cassandra";
	private String password = "cassandra";

	public void init() {
		SocketOptions socketOptions = new SocketOptions();
		socketOptions.setConnectTimeoutMillis(connectTimeoutMillis);
		socketOptions.setReadTimeoutMillis(readTimeoutMillis);
		Builder builder = Cluster.builder().withSocketOptions(socketOptions)
				.addContactPoints(nodes.trim().split(",")).withPort(port);
		if (username != null && password != null) {
			//if you have username pwd
			builder.withCredentials(username, password);
		}
		cluster = builder.build();
		try {
			session = cluster.connect();
		} catch (Exception e) {
			System.out.println("Failed to connect to Cassandra - exiting: "
					+ e.getMessage());
			throw new RuntimeException(
					"Failed to connect to Cassandra - exiting", e);
		}
		Metadata metadata = cluster.getMetadata();
		System.out.printf("Connected to cluster: %s\n",
				metadata.getClusterName());
		for (Host host : metadata.getAllHosts()) {
			System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n",
					host.getDatacenter(), host.getAddress(), host.getRack());
		}
	}

	

	

	public double getScore(String chrom, int start, int end ) {
		String query="SELECT * FROM dev.phastConsBulkLoad WHERE chrom='"+chrom+"' and base > "+start+" and base < "+(end+1)+";";
		//"SELECT * FROM dev.phastConsBulkLoad " + "WHERE chrom='"+chrom+"' and base="+base+";"
		System.out.println(query);
		ResultSet results = session.execute(query);
		//System.out.println("\n\nSELECT specific base:");
		double score=0;
		int count=0;
		for (Row row : results) {
			//System.out.println("[" +  row.getString("chrom") + ", " + row.getInt("base")
				//	+ ", " + row.getDouble("score") + "]\n");
			score += row.getDouble("score");
			count++;
		}
		//System.out.println("Score" + score);
		
		return score/count;
		
	}

	public void close() {
		cluster.close();
	}

	public static void main(String[] args) {
		PhastConsRetriever client = new PhastConsRetriever();
		client.init();
		//client.createSchema();
		//client.loadData();
		//client.querySchema();
		client.close();
	}

}
/*package cassandra.demo;



public class CRUDDemo {
	}
*/