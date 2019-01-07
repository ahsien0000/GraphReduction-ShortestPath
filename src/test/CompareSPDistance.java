package test;

import java.io.*;
import java.util.ArrayList;
import database.*;
import utility.StopWatch;
import algorithm.*;

public class CompareSPDistance {

	public static void main(String[] args) throws IOException {

		System.out.println("********** Program successfully started !  **********");
		
	// ############################################################
	// #
	// #    I N I T I A L I Z A T I O N
	// #
	// ############################################################	
		
		StopWatch sw = new StopWatch("CompareSPDistance");
		sw.start();
						
		// CSV Input File
		String reducedFile  = "CSV_LGR-SP-OnePlusEpsilon(e=0.0)_Map_10k.csv";
		String originalFile = "map_SanFrancisco_10k.csv";
		
		// CSV Output File
		String fileName     = "diff_LGR-SP-OnePlusEpsilon(e=0.0)_Map_10k.csv";
		String outputFile   = "c:\\csv\\"+ fileName;
		
		// Sampling Size
		int samplingSize = 100000;
		
		// Create DB
		Database db = new Database();
					
		// Clean DB
		db.deleteAll();
		
		// Load CSV to DB
		db.loadCSV(reducedFile);
		System.out.println("Reduced Graph CSV file has been loaded.");
		
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
		
		// ##################################################
		//   Prepare Variables
		// ##################################################
		String sourceNode[] = new String [samplingSize];
		String destNode[] = new String [samplingSize];
		int originalSPDistance[] = new int [samplingSize];
		int reducedSPDistance[] = new int[samplingSize];
		int diffOfSPDistance[] = new int[samplingSize];
		double errorRatio[] = new double[samplingSize];
		double maxErrorRatio = 0.0;
		Dijkstra dj = new Dijkstra();
	
		// ##################################################
		//   Select Pairs (source, dest)
		// ##################################################
		for (int i=0; i<samplingSize; i++) {
			sourceNode[i] = nodeList.get((int)(Math.random() * numOfNodes)).toString();
			do {
				destNode[i] = nodeList.get((int)(Math.random() * numOfNodes)).toString();
			}while (sourceNode[i].equals(destNode[i]));
//			System.out.println("(source, dest) = (" + sourceNode[i] + ", " + destNode[i] +")");
		}
		
		// ##################################################
		//   Calculate SP Distance in Reduced Graph
		// ##################################################
		System.out.println("Calucate SP Distance in Reduced Graph");
		for(int i=0; i<samplingSize; i++) {
			if(i !=0 && i%100 == 0) {
				System.out.println("  i = " + i);
			}
			reducedSPDistance[i] = dj.dijkstraSP(nodeList, 
												 matrix, 
												 nodeList.indexOf(sourceNode[i]), 
												 nodeList.indexOf(destNode[i]), 
												 false);
		}
		
		// ##################################################
		//   Load Original Graph
		// ##################################################
		// Clean DB
		db.deleteAll();
		
		// Load CSV to DB
		db.loadCSV(originalFile);
		System.out.println("Original Graph CSV file has been loaded.");
		
		// Initialize List nodeList and Array matrix[][]
		nodeList.clear();
		for (int i=0; i<numOfNodes; i++) {
		    for (int j=0; j<numOfNodes; j++) {
		    	matrix[i][j] = (i==j) ? 0 : -1;
		    }
		}
		
		// Loading Nodes and Edges from DB
		db.getNodesAndEdges(nodeList,  matrix);
		
		// ##################################################
		//   Calculate SP Distance in Original Graph
		// ##################################################
		System.out.println("Calucate SP Distance in Original Graph");
		for(int i=0; i<samplingSize; i++) {
			if(i !=0 && i%100 == 0) {
				System.out.println("  i = " + i);
			}
			originalSPDistance[i] = dj.dijkstraSP(nodeList, 
												  matrix, 
												  nodeList.indexOf(sourceNode[i]), 
												  nodeList.indexOf(destNode[i]), 
												  false);
		}

		// ##################################################
		//   Calculate Difference between Reduced and Original
		// ##################################################
		for(int i=0; i<samplingSize; i++) {
			diffOfSPDistance[i] = reducedSPDistance[i] - originalSPDistance[i];
			errorRatio[i] = (double)diffOfSPDistance[i] / originalSPDistance[i] * 100;
			
			if(errorRatio[i] > maxErrorRatio) {
				maxErrorRatio = errorRatio[i];
			}
		}
		
		// ##################################################
		//   Output Difference in CSV File
		// ##################################################
		
		System.out.println("");
		System.out.println("Max Error Ratio : " + String.format("%.2f", maxErrorRatio) + " %");
		System.out.println("Sampling Size   : " + samplingSize);
		System.out.println("Original Graph  : " + originalFile);
		System.out.println("Reduced Graph   : " + reducedFile);
		System.out.println("");
		
		// Write CSV file
		FileWriter fw = new FileWriter(outputFile);
		fw.write("Max Error," + String.format("%.2f", maxErrorRatio) + ",%\n");
		fw.write("Sampling," + samplingSize + "\n");
		fw.write("Original," + originalFile + "\n");
		fw.write("Reduced," + reducedFile + "\n");
		fw.write("\n");
		fw.write("source,dest,distReduced,distOriginal,difference,errorRatio\n");
		for(int i=0; i<samplingSize; i++) {
			fw.write(sourceNode[i] + ","
					+ destNode[i] + ","
					+ reducedSPDistance[i] + ","
					+ originalSPDistance[i] + ","
					+ diffOfSPDistance[i] + ","
					+ errorRatio[i] + "\n");
		}
		fw.flush();
		fw.close();	

		
	// ############################################################
	// #
	// #    P O S T    P R O C E S S
	// #
	// ############################################################
		
		sw.stop();
						
		// Close DB
		db.close();
		
		System.out.println("********** Program successfully finished ! **********");

	}

}
