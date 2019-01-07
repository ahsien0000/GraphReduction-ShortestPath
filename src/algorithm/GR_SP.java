package algorithm;

import java.util.*;
import database.*;
import utility.*;

public class GR_SP {

	public void reduce(ArrayList nodeList, int[][] matrix) {

		int numOfNodes = matrix.length;
		Dijkstra dj = new Dijkstra();
		Database db = new Database();
		StopWatch sw = new StopWatch("GR_SP");
		int[] distance = new int[numOfNodes];
		
		db.showGraphInfo();
		sw.start();
		
		for(int i=0; i<numOfNodes; i++) {
			if(i > 0 && i % 100 == 0) {
					System.out.println("    i = "+ i);
			}
			
			// Get Dijkstra from Node i to all
			distance = dj.dijkstraArray(matrix, i);
			
			for(int j=0; j<numOfNodes; j++) {
				if (matrix[i][j] > 0 && matrix[i][j] > distance[j]) {
					db.deleteEdge(nodeList.get(i).toString(),  nodeList.get(j).toString());
					System.out.println("----- (i = "+ i +") Deleted Edge [" + nodeList.get(i) + ", " + nodeList.get(j) +"] -----");
				}
			}
		}
		
		sw.stop();
		db.showGraphInfo();
		
		// Close DB
		db.close();
	}
		
}
