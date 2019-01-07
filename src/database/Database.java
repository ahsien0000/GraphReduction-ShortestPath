package database;

import model.*;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Database {
	
	String dbUser = "neo4j";
	String dbPass = "uci";
	Driver driver = null;
	Session session = null;
	
	public Database() {
		// Connect to DB and open session
		driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic( dbUser, dbPass ) );
		session = driver.session();		
	}
	
	public void close() {
		// Close DB and session
		session.close();
		driver.close();		
	}

	public void showGraphInfo() {	
		// Get num of Nodes and Edges
		int numOfNodes = getNumOfNodes();
		int numOfEdges = getNumOfEdges();
		
		// show Graph Info
		System.out.println("********** Graph Info **********");
		System.out.println("  Num of Nodes = "+ numOfNodes);
		System.out.println("  Num of Edges = "+ numOfEdges);
		System.out.println("********************************");	
	}

	
	public void loadCSV(String inputFile) {
		
		StatementResult result = null;
		
		// Load Graph from CSV to DB
		result = session.run("load csv with headers from 'file:///" + inputFile + "' as csvLine \n"
				+ "merge (:Node {name:csvLine.start}) \n"
				+ "merge (:Node {name:csvLine.end})");//merge kills duplicates, create keeps all duplicated nodes&edges

		result = session.run("load csv with headers from 'file:///" + inputFile + "' as csvLine "
				+ "match (s:Node{name:csvLine.start}), (e:Node {name:csvLine.end}) "
				+ "merge (s)-[:Edge {weight:toInt(csvLine.weight)}]->(e)");
		
	}
	
	public int getNumOfNodes() {

		StatementResult result = null;	
		int numOfNodes = 0;
		
		// Get Num of Node from Graph DB
		result = session.run("match (n) return count(n)");
		while ( result.hasNext() )
		{
		    Record record = result.next();
		    numOfNodes = record.get("count(n)").asInt();
		}		
		return numOfNodes;
	}
	
	public int getNumOfEdges() {

		StatementResult result = null;	
		int numOfEdges = 0;
		
		// Get Num of Edges from Graph DB
		result = session.run("match ()-[r]->() return count(r) as numOfEdges");
		while ( result.hasNext() )
		{
		    Record record = result.next();
		    numOfEdges = record.get("numOfEdges").asInt();
		}		
		return numOfEdges;
	}

	
	public void getNodes(ArrayList nodeList) {

		StatementResult result = null;
		
		// Get Node Name from Graph DB
		result = session.run("match (n) return n.name");
		while ( result.hasNext() )
		{
		    Record record = result.next();
		    nodeList.add(record.get("n.name").asString());
		}

	}
	
	
	public void getEdges(ArrayList nodeList, Edge [] edges) {

		StatementResult result = null;
				
		// Get Distance between Nodes from Graph DB
		result = session.run("match (s:Node)-[ed:Edge]->(e:Node) return s.name, e.name, ed.weight");
		int i = 0;
		while ( result.hasNext() )
		{
		    Record record = result.next();	    
		    edges[i] = new Edge(nodeList.indexOf(record.get("s.name").asString()),
		    					nodeList.indexOf(record.get("e.name").asString()),
		    					record.get("ed.weight").asInt() );
		    i++;
		}

	}
	
	
	public void getNodesAndEdges(ArrayList nodeList, int [][] matrix) {
		
		int numOfNodes = nodeList.size();
		StatementResult result = null;	
			
		// Get Node Name from Graph DB
		result = session.run("match (n) return n.name");
		while ( result.hasNext() )
		{
		    Record record = result.next();
		    nodeList.add(record.get("n.name").asString());
		}		
		
		// Get Distance between Nodes from Graph DB
		result = session.run("match (s:Node)-[ed:Edge]->(e:Node) return s.name, e.name, ed.weight");
		while ( result.hasNext() )
		{
		    Record record = result.next();				
		    matrix[nodeList.indexOf(record.get("s.name").asString())][nodeList.indexOf(record.get("e.name").asString())] 
		    		= record.get("ed.weight").asInt();
		}

	}
	
	public void deleteAll() {

		StatementResult result = null;
		
		// Delete Edge from Graph DB
		result = session.run("match(n) detach delete n");
		
	}
	
	public void deleteEdge(String src, String dst) {

		StatementResult result = null;
		
		// Delete Edge from Graph DB
		result = session.run("match (s:Node)-[ed]->(d:Node) where s.name='"+ src +"' and d.name='"+ dst +"' delete ed");
		
	}
	
	public void deleteNode(String nodeName) {

		StatementResult result = null;
		
		// Delete Edge from Graph DB
		result = session.run("match(n) where n.name='"+ nodeName +"' delete n");
		
	}
	
	public void addEdge(String src, String dst, int weight) {

		StatementResult result = null;
		
		// Add Node on Graph DB
		result = session.run("merge (:Node {name:'"+src+"'})");
		result = session.run("merge (:Node {name:'"+dst+"'})");
		
		// Add Edge on Graph DB
		result = session.run("match (s:Node {name:'"+src+"'}), (d:Node {name:'"+dst+"'}) merge (s)-[:Edge {weight:"+ weight +"}]->(d)");
		
	}
	
	public void getNodesGtK(ArrayList violatingNodes, int k) {

		StatementResult result = null;
		
		// Get violating Nodes (degree>k) from Graph DB
		result = session.run("match (n)<-[]-() with n, count(n) as degree where degree>"+ k +" return n.name");
		while ( result.hasNext() )
		{
		    Record record = result.next();
		    violatingNodes.add(record.get("n.name").asString());
		}
		
	}
	
	public void getNeighborNodes(String nodeName, ArrayList neighbors) {

		StatementResult result = null;
		
		// Get violating Nodes (degree>k) from Graph DB
		result = session.run("match (v {name:'"+ nodeName +"'})<-[]-(n) return n.name");
		while ( result.hasNext() )
		{
		    Record record = result.next();
		    neighbors.add(record.get("n.name").asString());
		}
		
	}
	
	public int getDegree(String nodeName) {

		StatementResult result = null;
		int degree = 0;
		
		// Get Degree of Node
		result = session.run("match (n) where n.name='"+ nodeName +"' with n, size((n)-[]->()) as degree return degree");
		while ( result.hasNext() )
		{
		    Record record = result.next();
		    degree = record.get("degree").asInt();
		}
		return degree;
		
	}
	
	public void saveCSV(String outputFile) throws IOException {
				
		// Load Nodes
		ArrayList nodeList = new ArrayList();
		getNodes(nodeList);
		
		// Load Edges
		int numOfEdges = getNumOfEdges();
		Edge[] edges = new Edge[numOfEdges];
		getEdges(nodeList, edges);
		
		// Write CSV file
		FileWriter fw = new FileWriter(outputFile);
		fw.write("start,end,weight\n");
		for(int i=0; i<numOfEdges; i++) {
			fw.write(nodeList.get(edges[i].getSourceNode()).toString() + "," + nodeList.get(edges[i].getDestNode()).toString() + "," + edges[i].getWeight() + "\n");
		}
		fw.flush();
		fw.close();	
	}

	
}
