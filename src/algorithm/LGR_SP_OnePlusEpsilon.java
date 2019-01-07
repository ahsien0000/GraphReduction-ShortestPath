package algorithm;

import java.util.*;
import database.*;
import utility.*;
import model.*;

public class LGR_SP_OnePlusEpsilon {

	public void reduce(ArrayList nodeList, int[][] matrix, double epsilon) {

		Dijkstra dj = new Dijkstra();
		Database db = new Database();
		StopWatch sw = new StopWatch("LGR_SP_OnePlusEpsilon");
		int numOfEdges = db.getNumOfEdges();
		
		db.showGraphInfo();
		sw.start();
		
		// Load Edges
		Edge[] edges = new Edge[numOfEdges];
		db.getEdges(nodeList, edges);
		
		// Sort Edges	
		Arrays.sort(edges);
			
		// Delete all from DB
		db.deleteAll();
		
		// Initialize Array matrix[][]
		int numOfNodes = nodeList.size();
		for (int i=0; i<numOfNodes; i++) {
		    for (int j=0; j<numOfNodes; j++) {
		    	matrix[i][j] = (i==j) ? 0 : -1;
		    }
		}
		
		// Create new Graph on DB
		for(int i=0; i<numOfEdges; i++) {
			
			if(i >0 && i % 100 == 0) {
					System.out.println("    i = "+ i);
			}

			if (((1 + epsilon) * edges[i].getWeight()) < dj.dijkstraSP(nodeList,  matrix, edges[i].getSourceNode(),  edges[i].getDestNode(), false)) {
				// Update DB
				db.addEdge(nodeList.get(edges[i].getSourceNode()).toString(),  nodeList.get(edges[i].getDestNode()).toString(), edges[i].getWeight());
				
				// Update matrix[i][j]
				matrix[edges[i].getSourceNode()][edges[i].getDestNode()] = edges[i].getWeight();
				
				System.out.println("----- (i = "+ i +") Added Edge [" + nodeList.get(edges[i].getSourceNode()) + ", " + nodeList.get(edges[i].getDestNode()) +"] -----");
			}

		}
		
		sw.stop();
		db.showGraphInfo();
		
		// Close DB
		db.close();
	}
	
}
