import java.util.ArrayList;


public class State implements Comparable<State>{
	
	ArrayList<String> path;
	int cost;
	double load = 0.0;
	boolean LLP = false;
	
	/**
	 * Default constructor
	 * 
	 * @param initial node 
	 */
	public State(String initial) {
		path = new ArrayList<String>();
		path.add(initial);
		cost = 0;
	}
	
	
	/**
	 * Path Constructor
	 * 
	 * @param oldPath		previous path
	 * @param newNode		node to add to path
	 * @param oldCost		previous path cost
	 * @param extraCost		new cost to add
	 */
	public State(ArrayList<String> oldPath, String newNode, int oldCost, int extraCost) {
		path = new ArrayList<String>(oldPath);
		path.add(newNode);
		cost = oldCost + extraCost;
	}
	
	
	/**
	 * Overloading constructor for LLP
	 * Takes in a double for the load
	 * 
	 * @param oldPath		previous path
	 * @param newNode		node to add to path
	 * @param leastLoad		load of link to add
	 */
	public State(ArrayList<String> oldPath, String newNode) {//, double leastLoad) {
		path = new ArrayList<String>(oldPath);
		path.add(newNode);
		LLP = true;
		//load = leastLoad;
	}
	
	public void setLoad(double maxLoad) {
		load = maxLoad;
	}
	
	/**
	 * 
	 * @return path
	 */
	public ArrayList<String> getPath() {
		return path;
	}
	
	
	/**
	 * 
	 * @return the current location in the path i.e. the last node in the path
	 */
	public String getLastNode() {
		return path.get(path.size() -1);
	}
	
	
	/**
	 * 
	 * @return path cost
	 */
	public int getCost() {
		return cost;
	}

	
	/**
	 * 
	 * @return current load
	 */
	public double getLoad() {
		return load;
	}
	
	
	/**
	 * Comparator used for priority queue
	 */
	@Override
	public int compareTo(State o) {
		
		//if(load != 0.0) {	//used to compare doubles for LLP
		if(LLP) {
			if(load < o.getLoad()) return -1;
			if(load > o.getLoad()) return 1;
			return 0;
		}
		
		return cost - o.getCost();	//used for SDP and SHP
		
	}
}
