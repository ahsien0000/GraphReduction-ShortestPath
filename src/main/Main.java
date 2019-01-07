package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import database.*;
import utility.StopWatch;
import algorithm.*;

public class Main {


	public static void main(String[] args) throws IOException {

		System.out.println("********** Program successfully started !  **********");
		
// ############################################################
// #
// #    I N I T I A L I Z A T I O N
// #
// ############################################################		
		
		// CSV Input File
		String inputFile = "GSE48213_RAW_CN_EditedAll_first5000_bidirectional.csv";//file location:C:\Users\Bryan\AppData\Roaming\Neo4j Desktop\Application\neo4jDatabases\database-4a8987e2-469d-4d32-8aa5-775191ac570e\installation-3.3.3\import
		
		// CSV Output File
		String outputFile = "c:\\csv\\LGRSP_1PlusEpsilon_09_GSE48213_RAW_CN_EditedAll_first5000_bidirectional.csv";//"GRSP_textmining_GRN_demo_lung.csv";
		
		// Create DB
		Database db = new Database();
					
		// Clean DB
		db.deleteAll();
		
		// Load CSV to DB
		db.loadCSV(inputFile);
		
		// Prepare List and Array for Nodes and Edges
		ArrayList nodeList = new ArrayList();
		int numOfNodes = db.getNumOfNodes();
		int[][] matrix = new int[numOfNodes][numOfNodes];
		
		// Initialize Array matrix[][]
		for (int i=0; i<numOfNodes; i++) {
		    for (int j=0; j<numOfNodes; j++) {
		    	matrix[i][j] = (i==j) ? 0 : -1;
		    }
		}
		
		// Loading Nodes and Edges from DB
		db.getNodesAndEdges(nodeList,  matrix);
		
		
// ############################################################
// #
// #    A L G O R I T H M
// #
// ############################################################

        
        
		// ##### Graph Reduction for Shortest Path #####
		GR_SP gr_sp = new GR_SP();
		gr_sp.reduce(nodeList, matrix);
		
		// ##### One Plus Epsilon Lossy Graph Reduction for Shortest Path #####
//		LGR_SP_OnePlusEpsilon lgr_sp_ope = new LGR_SP_OnePlusEpsilon();
//		lgr_sp_ope.reduce(nodeList, matrix, 0.9);


		
// ############################################################
// #
// #    P O S T    P R O C E S S
// #
// ############################################################		
		
		// Save DB into CSV
        db.saveCSV(outputFile);
				
		// Close DB
		db.close();
		
		System.out.println("********** Program successfully finished ! **********");
	
			
	}
	
}
