package model;

public class Edge implements Comparable<Edge> {
	
	private int sourceNode;
	private int destNode;
	private int weight;
	
	public Edge(int sNode, int dNode, int w) {
		sourceNode = sNode;
		destNode = dNode;
		weight = w;
	}
		
	public int getSourceNode() {
		return sourceNode;
	}
	
	public int getDestNode() {
		return destNode;
	}
	
	public int getWeight() {
		return weight;
	}

	public int compareTo(Edge compareEdge) {		
		return this.getWeight() - compareEdge.getWeight();
	}

}
