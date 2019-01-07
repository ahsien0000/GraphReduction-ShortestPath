package algorithm;

import java.util.*;
import java.text.*;
import database.*;

public class Dijkstra {

	//public int[] via = new int[10000];
	public int[] dijkstraArray(int [][] matrix, int src) {

		int numOfNodes = matrix.length;
		int[] distance = new int[numOfNodes]; 
		Dijkstra dj = new Dijkstra();
			
		
		dj.dijkstra(matrix, src, distance);
		
		return distance;
	}
	
		
	public int dijkstraSP(ArrayList nodeList, int [][] matrix, int src, int dst, boolean showPath) {

		int numOfNodes = matrix.length;
		int[] distance = new int[numOfNodes]; 
		int[] via = new int[numOfNodes];
			
		
		dijkstra(matrix, src, distance, via);
				
		if(showPath == true) {
			System.out.println();
			System.out.println("######### Shortest Path ("+nodeList.get(src)+" -> "+nodeList.get(dst)+") ##########");
			
			if (distance[dst]==Integer.MAX_VALUE) {	
			    System.out.println("No Route !");
			} else {
			    System.out.println("distance = "+distance[dst]);

			    ArrayList nodeOfPath = new ArrayList<String>();
			    for (int i=dst; i!=src; i=via[i]) {
			    	nodeOfPath.add(nodeList.get(i));
			    }
			    nodeOfPath.add(nodeList.get(src));
			    
			    
			    System.out.print("ShortestPath = ");
			    for (int i=nodeOfPath.size()-1; i>0; i--) {
			    	System.out.print(nodeOfPath.get(i) + "->");
			    }
			    System.out.println(nodeOfPath.get(0));
			    
			}
		}
		
		return distance[dst];
	}
	
	public int[] dijkstraSPArray(ArrayList nodeList, int [][] matrix, int src, int dst, boolean showPath) {

		int numOfNodes = matrix.length;
		int[] distance = new int[numOfNodes]; 
		int[] via = new int[numOfNodes];
			
		
		dijkstra(matrix, src, distance, via);
				
		if(showPath == true) {
			System.out.println();
			System.out.println("######### Shortest Path ("+nodeList.get(src)+" -> "+nodeList.get(dst)+") ##########");
			
			if (distance[dst]==Integer.MAX_VALUE) {	
			    System.out.println("No Route !");
			} else {
			    System.out.println("distance = "+distance[dst]);

			    ArrayList nodeOfPath = new ArrayList<String>();
			    for (int i=dst; i!=src; i=via[i]) {
			    	nodeOfPath.add(nodeList.get(i));
			    }
			    nodeOfPath.add(nodeList.get(src));
			    
			    
			    System.out.print("ShortestPath = ");
			    for (int i=nodeOfPath.size()-1; i>0; i--) {
			    	System.out.print(nodeOfPath.get(i) + "->");
			    }
			    System.out.println(nodeOfPath.get(0));
			    
			}
		}
		
		return via;
	}
	
	
    public void dijkstra(int[][] matrix, int src, int[] distance) {

    	int nodeSize = distance.length;
		boolean[] fixed = new boolean[nodeSize]; 
		for (int i=0; i<nodeSize; i++) { 
		    distance[i] = Integer.MAX_VALUE; 
		    fixed[i] = false;
		}
		distance[src] = 0;
	    
		while (true) {
		    int marked = minIndex(distance, fixed);
		    if (marked < 0) return; 
		    if (distance[marked]==Integer.MAX_VALUE) return; 
		    fixed[marked] = true; 

		    for (int j=0; j<nodeSize; j++) { 
				if (matrix[marked][j]>0 && !fixed[j]) { 
				    int newDistance = distance[marked]+matrix[marked][j];
				    if (newDistance < distance[j]) {
				    	distance[j] = newDistance;
				    }
				}
		    }
		}
    }
	
    
    public void dijkstra(int[][] matrix, int src, int[] distance, int[] via) {
    	int nodeSize = via.length;
		boolean[] fixed = new boolean[nodeSize]; 
		for (int i=0; i<nodeSize; i++) {
		    distance[i] = Integer.MAX_VALUE;
		    fixed[i] = false;	
		    via[i] = -1;   
		}
		distance[src] = 0;
	    
		while (true) {
		    int marked = minIndex(distance, fixed);
		    if (marked < 0) return; 
		    if (distance[marked]==Integer.MAX_VALUE) return; 
		    fixed[marked] = true; 

		    for (int j=0; j<nodeSize; j++) { 
				if (matrix[marked][j]>0 && !fixed[j]) { 
				    int newDistance = distance[marked]+matrix[marked][j];
				    if (newDistance < distance[j]) {
				    	distance[j] = newDistance;
				    	via[j] = marked; 
				    }
				}
		    }
		}
    }
    
    

    int minIndex(int[] distance, boolean[] fixed) {
		int idx=0;
		for (; idx<fixed.length; idx++)	
		    if (!fixed[idx]) break;
		if (idx == fixed.length) return -1; 
		for (int i=idx+1; i<fixed.length; i++) 
		    if (!fixed[i] && distance[i]<distance[idx]) idx=i;
		return idx;
    }
  
    
	public int dijkstraAddPath(ArrayList nodeList, int [][] matrix, ArrayList rgNodeList, int [][] rgMatrix, int src, int dst, boolean showPath) {

		int numOfNodes = matrix.length;
		int[] distance = new int[numOfNodes]; 
		int[] via = new int[numOfNodes];
		Dijkstra dj = new Dijkstra();
		Database db = new Database();
		
		dj.dijkstra(matrix,src,distance, via);
		
		if(showPath == true) {
			System.out.println();
			System.out.println("######### Shortest Path ("+nodeList.get(src)+" -> "+nodeList.get(dst)+") ##########");
		}
			
		if (distance[dst]==Integer.MAX_VALUE) {	
			if(showPath == true) {
				System.out.println("No Route !");
			}
		} else {
			if(showPath == true) {
				System.out.println("distance = "+distance[dst]);
			}
		    int nodeCnt=0;
		    ArrayList nodeOfPath = new ArrayList<String>();
		    for (int i=dst; i!=src; i=via[i]) {
		    	nodeCnt++;
		    	nodeOfPath.add(nodeList.get(i));
		    }
		    nodeOfPath.add(nodeList.get(src));
		    

		    if(showPath == true) {
			    System.out.print("ShortestPath = ");
			    for (int i=nodeCnt; i>0; i--) {
			    	System.out.print(nodeOfPath.get(i) + "->");
			    }
			    System.out.println(nodeOfPath.get(0));
		    }
		    
		    for (int i=nodeCnt; i>0; i--) {

		    	if(rgMatrix[rgNodeList.indexOf(nodeOfPath.get(i))][rgNodeList.indexOf(nodeOfPath.get(i-1))] < 0) {
		    		db.addEdge(nodeOfPath.get(i).toString(), nodeOfPath.get(i-1).toString(), matrix[nodeList.indexOf(nodeOfPath.get(i))][nodeList.indexOf(nodeOfPath.get(i-1))]);
//		    		if(showPath == true) {
		    			System.out.println("Added: ("+nodeOfPath.get(i).toString()+")-["+matrix[nodeList.indexOf(nodeOfPath.get(i))][nodeList.indexOf(nodeOfPath.get(i-1))]+"]->("+nodeOfPath.get(i-1).toString()+")");
//		    		}
		    	}
		    }
		    System.out.println(nodeOfPath);
		    
		}
			
		return distance[dst];
	}	
    
    
}
